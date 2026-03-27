from uuid import UUID
from typing import Optional
from pydantic import BaseModel, EmailStr, Field

class Credentials(BaseModel):
    email: EmailStr
    password: str

class User(BaseModel):
    ID: UUID
    email: EmailStr
    first_name: Optional[str] = Field(default=None, alias='firstName')
    last_name: Optional[str] = Field(default=None, alias='lastName')

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