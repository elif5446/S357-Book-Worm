from pydantic import BaseModel
from datetime import datetime


class CommentCreate(BaseModel):
    user_id: str
    user_name: str
    review_id: str
    comment_text: str


class Comment(BaseModel):
    id: str
    user_id: str
    user_name: str
    review_id: str
    comment_text: str
    created_at: datetime
