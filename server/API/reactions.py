from fastapi import APIRouter, HTTPException
from DTOs.reactions import ReactionCreate, Reaction
import uuid
from datetime import datetime
import os
from supabase import create_client, Client
from dotenv import load_dotenv

reactions_router = APIRouter()
load_dotenv()


def get_supabase_db() -> Client:
    url = os.environ.get('SUPABASE_URL')
    key = os.environ.get('SUPABASE_SERVICE_KEY') or os.environ.get('SUPABASE_API_KEY')
    if not url or not key:
        raise HTTPException(status_code=500, detail="Supabase credentials missing.")
    return create_client(url, key)


@reactions_router.post("/reactions", response_model=Reaction)
async def create_reaction(reaction: ReactionCreate):
    """Create a reaction to a review"""
    try:
        supabase = get_supabase_db()
        
        reaction_id = str(uuid.uuid4())
        created_at = datetime.utcnow().isoformat()
        
        response = supabase.table("Reactions").insert({
            "id": reaction_id,
            "user_id": reaction.user_id,
            "review_id": reaction.review_id,
            "reaction_type": reaction.reaction_type,
            "created_at": created_at
        }).execute()
        
        if response.data:
            return Reaction(
                id=reaction_id,
                user_id=reaction.user_id,
                review_id=reaction.review_id,
                reaction_type=reaction.reaction_type,
                created_at=datetime.fromisoformat(created_at)
            )
            raise HTTPException(status_code=400, detail="Failed to create reaction")
    except Exception as e:
            raise HTTPException(status_code=500, detail=f"Error creating reaction: {str(e)}")


@reactions_router.get("/reviews/{review_id}/reactions", response_model=list[Reaction])
async def get_reactions_for_review(review_id: str):
    """Get all reactions for a specific review"""
    try:
        supabase = get_supabase_db()
        
        response = supabase.table("Reactions").select("*").eq("review_id", review_id).execute()
        
        if response.data:
            return [
                Reaction(
                    id=r["id"],
                    user_id=r["user_id"],
                    review_id=r["review_id"],
                    reaction_type=r["reaction_type"],
                    created_at=datetime.fromisoformat(r["created_at"])
                )
                for r in response.data
            ]
        return []
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching reactions: {str(e)}")


@reactions_router.delete("/reactions/{reaction_id}")
async def delete_reaction(reaction_id: str):
    """Delete a reaction"""
    try:
        supabase = get_supabase_db()
        
        supabase.table("Reactions").delete().eq("id", reaction_id).execute()
        return {"message": "Reaction deleted"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting reaction: {str(e)}")
