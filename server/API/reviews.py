from fastapi import APIRouter, HTTPException
import os
from uuid import UUID, uuid4
from supabase import create_client, Client
from dotenv import load_dotenv
from DTOs.reviews import ReviewCreate, Review
from datetime import datetime

load_dotenv()
router = APIRouter()


def get_supabase_db() -> Client:
    """Initialize Supabase client for database operations"""
    url = os.environ.get('SUPABASE_URL')
    key = os.environ.get('SUPABASE_SERVICE_KEY') or os.environ.get('SUPABASE_API_KEY')
    if not url or not key:
        raise HTTPException(status_code=500, detail="Supabase credentials missing.")
    return create_client(url, key)


@router.post("/reviews", response_model=Review)
async def create_review(review: ReviewCreate):
    """
    Create a new review for a book.
    
    - **user_id**: UUID of the user creating the review
    - **user_name**: Display name of the user
    - **book_id**: ISBN or internal book identifier
    - **rating**: Rating from 1-5
    - **comment**: Review comment text
    """
    try:
        supabase = get_supabase_db()
        
        # Create review record
        review_id = str(uuid4())
        review_data = {
            "id": review_id,
            "user_id": str(review.user_id),
            "user_name": review.user_name,
            "book_id": review.book_id,
            "rating": review.rating,
            "comment": review.comment,
            "created_at": datetime.utcnow().isoformat()
        }
        
        # Insert into Supabase
        response = supabase.table("Reviews").insert(review_data).execute()
        
        if response.data:
            return response.data[0]
        else:
            raise HTTPException(status_code=400, detail="Failed to create review")
            
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Error creating review: {str(e)}"
        )


@router.get("/reviews/book/{book_id}")
async def get_book_reviews(book_id: str):
    """
    Get all reviews for a specific book.
    
    - **book_id**: ISBN or internal book identifier
    """
    try:
        supabase = get_supabase_db()
        
        response = supabase.table("Reviews").select("*").eq("book_id", book_id).execute()
        
        return response.data
        
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Error fetching reviews: {str(e)}"
        )


@router.get("/reviews/{review_id}")
async def get_review(review_id: str):
    """
    Get a specific review by ID.
    
    - **review_id**: UUID of the review
    """
    try:
        supabase = get_supabase_db()
        
        response = supabase.table("Reviews").select("*").eq("id", review_id).execute()
        
        if response.data:
            return response.data[0]
        else:
            raise HTTPException(status_code=404, detail="Review not found")
            
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Error fetching review: {str(e)}"
        )
