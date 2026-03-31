from fastapi import APIRouter, HTTPException, Header
from typing import Optional, List
import os
from supabase import create_client, Client
from pydantic import BaseModel
from dotenv import load_dotenv

load_dotenv()
router = APIRouter()


def get_supabase() -> Client:
    url = os.environ.get('SUPABASE_URL')
    key = os.environ.get('SUPABASE_API_KEY')  # anon key for token validation
    if not url or not key:
        raise HTTPException(status_code=500, detail="Supabase credentials missing.")
    return create_client(url, key)

def get_supabase_db() -> Client:
    url = os.environ.get('SUPABASE_URL')
    key = os.environ.get('SUPABASE_SERVICE_KEY') or os.environ.get('SUPABASE_API_KEY')
    if not url or not key:
        raise HTTPException(status_code=500, detail="Supabase credentials missing.")
    return create_client(url, key)


class CommentCreate(BaseModel):
    chapter: int
    content: str


class Comment(BaseModel):
    id: Optional[int] = None
    user_id: Optional[str] = None
    username: Optional[str] = None
    chapter: int
    content: str


@router.get("/comments/{chapter}")
def get_comments(chapter: int):
    db = get_supabase_db()
    try:
        response = db.table('Comments').select("*").eq('chapter', chapter).execute()
        return response.data
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Failed to fetch comments: {str(e)}")


@router.post("/comments/")
def post_comment(comment: CommentCreate, authorization: Optional[str] = Header(None)):
    supabase = get_supabase()
    db = get_supabase_db()
    try:
        token = None
        if authorization and authorization.startswith("Bearer "):
            token = authorization.split(" ", 1)[1]

        user_id = None
        username = None
        if token:
            user_response = supabase.auth.get_user(token)
            if user_response and user_response.user:
                user_id = str(user_response.user.id)
                profile = db.table('Users').select("username").eq("ID", user_id).single().execute().data
                if profile:
                    username = profile.get('username')

        data = {
            "chapter": comment.chapter,
            "content": comment.content,
        }
        if user_id:
            data["user_id"] = user_id
        if username:
            data["username"] = username

        response = db.table('Comments').insert(data).execute()
        return response.data
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Failed to post comment: {str(e)}")


@router.delete("/comments/{comment_id}")
def delete_comment(comment_id: int, authorization: Optional[str] = Header(None)):
    db = get_supabase_db()
    try:
        response = db.table('Comments').delete().eq('id', comment_id).execute()
        return {"detail": "Comment deleted"}
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Failed to delete comment: {str(e)}")




