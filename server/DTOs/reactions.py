from pydantic import BaseModel
from datetime import datetime
from typing import Optional


class ReactionCreate(BaseModel):
    user_id: str
    review_id: str
    reaction_type: str  # e.g., "like", "love", "dislike"


class Reaction(BaseModel):
    id: str
    user_id: str
    review_id: str
    reaction_type: str
    created_at: datetime
