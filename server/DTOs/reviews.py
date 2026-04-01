from uuid import UUID
from typing import Optional
from pydantic import BaseModel, Field
from datetime import datetime


class ReviewCreate(BaseModel):
    """Model for creating a new review"""
    user_id: UUID
    user_name: str
    book_id: str  # ISBN or internal book ID
    rating: int = Field(..., ge=1, le=5)  # Rating must be 1-5
    comment: str


class Review(BaseModel):
    """Model for review response"""
    id: UUID
    user_id: UUID
    user_name: str
    book_id: str
    rating: int
    comment: str
    created_at: datetime

    model_config = {
        "from_attributes": True,
        "serialize_by_alias": True
    }
