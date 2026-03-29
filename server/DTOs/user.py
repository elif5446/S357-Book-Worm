from uuid import UUID
from typing import Optional
from pydantic import BaseModel, EmailStr, Field

class Credentials(BaseModel): # The fields the user is presented with upon login or registration
    email: EmailStr # Identifier
    password: str # Secret
    username: Optional[str] = None # Optional and not always included in the request

class User(BaseModel):
    ID: UUID
    email: EmailStr
    username: Optional[str] = None
    # More...

    model_config = {
        "populate_by_name": True,
        "serialize_by_alias": True 
    }

class Account(BaseModel):
    token: Optional[str] = None
    user: Optional[User] = None

    model_config = {
        "from_attributes": True,
        "serialize_by_alias": True
    }