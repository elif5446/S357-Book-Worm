from fastapi import APIRouter, HTTPException, Header
from typing import Optional, List
import os
from supabase import create_client, Client
from pydantic import BaseModel
from dotenv import load_dotenv

load_dotenv()
router = APIRouter()


def get_supabase_db() -> Client:
    """Service role client — bypasses RLS, used for all DB reads/writes."""
    url = os.environ.get('SUPABASE_URL')
    key = os.environ.get('SUPABASE_SERVICE_KEY') or os.environ.get('SUPABASE_API_KEY')
    if not url or not key:
        raise HTTPException(status_code=500, detail="Supabase credentials missing.")
    return create_client(url, key)


def get_supabase_auth() -> Client:
    """Anon/publishable client — used only to validate user JWT tokens."""
    url = os.environ.get('SUPABASE_URL')
    key = os.environ.get('SUPABASE_API_KEY')
    if not url or not key:
        raise HTTPException(status_code=500, detail="Supabase credentials missing.")
    return create_client(url, key)


def get_user_id_from_token(token: str) -> Optional[str]:
    """Validate the user's JWT and return their user_id."""
    try:
        auth_client = get_supabase_auth()
        user_response = auth_client.auth.get_user(token)
        if user_response and user_response.user:
            return str(user_response.user.id)
    except Exception:
        pass
    return None


# ── DTOs ──────────────────────────────────────────────────────────────────────

class BookClubOut(BaseModel):
    id: str
    name: str
    memberCount: int = 0
    bookCount: int = 1
    activeSince: str = ""
    currentlyReading: str = ""
    imageUrl: Optional[str] = None


class ReadingProgressOut(BaseModel):
    currentPages: int
    goalPages: int
    clubId: str


class MemberProgressOut(BaseModel):
    userId: str
    username: Optional[str]
    currentPages: int
    goalPages: int


class UpdateProgressRequest(BaseModel):
    currentPages: int
    clubId: str


class ChapterCommentOut(BaseModel):
    id: str
    clubId: str
    chapterNumber: int
    username: str
    content: str
    createdAt: str = ""


class PostCommentRequest(BaseModel):
    clubId: str
    chapterNumber: int
    content: str


# ── Book Club routes ───────────────────────────────────────────────────────────

@router.get("/bookclubs/mine/", response_model=List[BookClubOut])
def get_my_book_clubs(authorization: Optional[str] = Header(None)):
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Missing token")
    token = authorization.split(" ", 1)[1]
    user_id = get_user_id_from_token(token)
    if not user_id:
        raise HTTPException(status_code=401, detail="Invalid token")
    db = get_supabase_db()
    try:
        memberships = db.table('ClubMembers').select("club_id").eq("user_id", user_id).execute().data
        club_ids = [m['club_id'] for m in memberships]
        if not club_ids:
            return []
        clubs_data = db.table('BookClubs').select("*").in_("id", club_ids).execute().data
        result = []
        for c in clubs_data:
            member_count = db.table('ClubMembers').select("user_id", count='exact').eq("club_id", c['id']).execute().count or 0
            result.append(BookClubOut(
                id=str(c['id']),
                name=c.get('name', ''),
                memberCount=member_count,
                bookCount=c.get('book_count', 1),
                activeSince=c.get('active_since', ''),
                currentlyReading=c.get('currently_reading', ''),
                imageUrl=c.get('image_url')
            ))
        return result
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Failed to fetch clubs: {str(e)}")


# ── Progress routes ────────────────────────────────────────────────────────────

@router.get("/bookclubs/{club_id}/progress/", response_model=ReadingProgressOut)
def get_club_progress(club_id: str, authorization: Optional[str] = Header(None)):
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Missing token")
    token = authorization.split(" ", 1)[1]
    user_id = get_user_id_from_token(token)
    if not user_id:
        raise HTTPException(status_code=401, detail="Invalid token")
    db = get_supabase_db()
    try:
        rows = db.table('MemberProgress').select("current_pages, goal_pages").eq("club_id", club_id).eq("user_id", user_id).limit(1).execute().data
        if rows:
            return ReadingProgressOut(currentPages=rows[0]['current_pages'], goalPages=rows[0].get('goal_pages', 3300), clubId=club_id)
        # First time — create a 0-page row
        db.table('MemberProgress').upsert({"club_id": club_id, "user_id": user_id, "current_pages": 0, "goal_pages": 3300}, on_conflict="club_id,user_id").execute()
        return ReadingProgressOut(currentPages=0, goalPages=3300, clubId=club_id)
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Failed to fetch progress: {str(e)}")


@router.post("/bookclubs/{club_id}/progress/", response_model=ReadingProgressOut)
def update_club_progress(club_id: str, body: UpdateProgressRequest, authorization: Optional[str] = Header(None)):
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Missing token")
    token = authorization.split(" ", 1)[1]
    user_id = get_user_id_from_token(token)
    if not user_id:
        raise HTTPException(status_code=401, detail="Invalid token")
    db = get_supabase_db()
    try:
        db.table('MemberProgress').upsert({
            "club_id": club_id,
            "user_id": user_id,
            "current_pages": body.currentPages,
            "goal_pages": 3300
        }, on_conflict="club_id,user_id").execute()
        return ReadingProgressOut(currentPages=body.currentPages, goalPages=3300, clubId=club_id)
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Failed to update progress: {str(e)}")


@router.get("/bookclubs/{club_id}/members/progress/", response_model=List[MemberProgressOut])
def get_all_members_progress(club_id: str, authorization: Optional[str] = Header(None)):
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Missing token")
    token = authorization.split(" ", 1)[1]
    user_id = get_user_id_from_token(token)
    if not user_id:
        raise HTTPException(status_code=401, detail="Invalid token")
    db = get_supabase_db()
    try:
        members = db.table('ClubMembers').select("user_id").eq("club_id", club_id).execute().data
        member_ids = [m['user_id'] for m in members]
        if not member_ids:
            return []
        profiles = db.table('Users').select("ID, username").in_("ID", member_ids).execute().data
        username_map = {p['ID']: p.get('username') for p in profiles}
        progress_rows = db.table('MemberProgress').select("user_id, current_pages, goal_pages").eq("club_id", club_id).execute().data
        progress_map = {r['user_id']: r for r in progress_rows}
        result = []
        for uid in member_ids:
            prog = progress_map.get(uid)
            result.append(MemberProgressOut(
                userId=uid,
                username=username_map.get(uid),
                currentPages=prog['current_pages'] if prog else 0,
                goalPages=prog['goal_pages'] if prog else 3300
            ))
        return result
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Failed to fetch member progress: {str(e)}")


# ── Chapter comments ───────────────────────────────────────────────────────────

@router.get("/bookclubs/{club_id}/comments/{chapter_number}/", response_model=List[ChapterCommentOut])
def get_chapter_comments(club_id: str, chapter_number: int, authorization: Optional[str] = Header(None)):
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Missing token")
    token = authorization.split(" ", 1)[1]
    user_id = get_user_id_from_token(token)
    if not user_id:
        raise HTTPException(status_code=401, detail="Invalid token")
    db = get_supabase_db()
    try:
        rows = db.table('ClubComments').select("*").eq("club_id", club_id).eq("chapter_number", chapter_number).order("created_at", desc=False).execute().data
        return [
            ChapterCommentOut(
                id=str(r['id']),
                clubId=str(r['club_id']),
                chapterNumber=r['chapter_number'],
                username=r.get('username') or 'Anonymous',
                content=r['content'],
                createdAt=str(r.get('created_at', ''))
            )
            for r in rows
        ]
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Failed to fetch comments: {str(e)}")


@router.post("/bookclubs/{club_id}/comments/", response_model=ChapterCommentOut)
def post_chapter_comment(club_id: str, body: PostCommentRequest, authorization: Optional[str] = Header(None)):
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Missing token")
    token = authorization.split(" ", 1)[1]
    user_id = get_user_id_from_token(token)
    if not user_id:
        raise HTTPException(status_code=401, detail="Invalid token")
    db = get_supabase_db()
    try:
        rows = db.table('Users').select("username").eq("ID", user_id).limit(1).execute().data
        username = rows[0].get('username') if rows else "Anonymous"
    except Exception:
        username = "Anonymous"
    try:
        row = db.table('ClubComments').insert({
            "club_id": club_id,
            "chapter_number": body.chapterNumber,
            "user_id": user_id,
            "username": username,
            "content": body.content
        }).execute().data[0]
        return ChapterCommentOut(
            id=str(row['id']),
            clubId=str(row['club_id']),
            chapterNumber=row['chapter_number'],
            username=row.get('username') or 'Anonymous',
            content=row['content'],
            createdAt=str(row.get('created_at', ''))
        )
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Failed to post comment: {str(e)}")