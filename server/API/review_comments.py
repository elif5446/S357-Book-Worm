from fastapi import APIRouter, HTTPException
from DTOs.comments import CommentCreate, Comment
import uuid
from datetime import datetime
import os
from supabase import create_client, Client
from dotenv import load_dotenv

review_comments_router = APIRouter()
load_dotenv()


def get_supabase_db() -> Client:
    url = os.environ.get('SUPABASE_URL')
    key = os.environ.get('SUPABASE_SERVICE_KEY') or os.environ.get('SUPABASE_API_KEY')
    if not url or not key:
        raise HTTPException(status_code=500, detail="Supabase credentials missing.")
    return create_client(url, key)


@review_comments_router.post("/review-comments", response_model=Comment)
async def create_review_comment(comment: CommentCreate):
    """Create a comment on a review"""
    try:
        supabase = get_supabase_db()
        
        comment_id = str(uuid.uuid4())
        created_at = datetime.utcnow().isoformat()
        
        response = supabase.table("Comments").insert({
            "id": comment_id,
            "user_id": comment.user_id,
            "user_name": comment.user_name,
            "review_id": comment.review_id,
            "comment_text": comment.comment_text,
            "created_at": created_at
        }).execute()
        
        if response.data:
            return Comment(
                id=comment_id,
                user_id=comment.user_id,
                user_name=comment.user_name,
                review_id=comment.review_id,
                comment_text=comment.comment_text,
                created_at=datetime.fromisoformat(created_at)
            )
        raise HTTPException(status_code=400, detail="Failed to create comment")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error creating review comment: {str(e)}")


@review_comments_router.get("/reviews/{review_id}/review-comments", response_model=list[Comment])
async def get_review_comments(review_id: str):
    """Get all comments for a specific review"""
    try:
        supabase = get_supabase_db()
        
        response = supabase.table("Comments").select("*").eq("review_id", review_id).execute()
        
        if response.data:
            return [
                Comment(
                    id=c["id"],
                    user_id=c["user_id"],
                    user_name=c["user_name"],
                    review_id=c["review_id"],
                    comment_text=c["comment_text"],
                    created_at=datetime.fromisoformat(c["created_at"])
                )
                for c in response.data
            ]
        return []
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching review comments: {str(e)}")


@review_comments_router.delete("/review-comments/{comment_id}")
async def delete_review_comment(comment_id: str):
    """Delete a review comment"""
    try:
        supabase = get_supabase_db()
        
        supabase.table("Comments").delete().eq("id", comment_id).execute()
        return {"message": "Comment deleted"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting review comment: {str(e)}")
