from fastapi import APIRouter, HTTPException
import os
from supabase import create_client, Client
from DTOs.user import Credentials, User, Account
from dotenv import load_dotenv

load_dotenv()
router = APIRouter()
def get_supabase() -> Client:
    url = os.environ.get('SUPABASE_URL')
    key = os.environ.get('SUPABASE_API_KEY')
    if not url or not key:
        raise HTTPException(status_code=500, detail="Supabase credentials missing.")
    return create_client(url, key)

@router.post("/register/")
def register(credentials: Credentials):
    supabase = get_supabase()

    try:
        response = supabase.auth.sign_up({
            "email": credentials.email,
            "password": credentials.password,
            "options": {
                "data": {
                    "full_name": credentials.username
                }
            }
        })

        if response.user:
            supabase.table('Users').insert({
                'ID': response.user.id,
                'email': response.user.email,
                'username': response.user.user_metadata.get("full_name")
            }).execute()

            user = User(
                ID = response.user.id,
                email = response.user.email,
                username = response.user.user_metadata.get("full_name")
            )
            return Account(token = response.session.access_token, user = user)
    
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Registration failed: {str(e)}")

    raise HTTPException(status_code=400, detail="Registration failed")

@router.post("/login/")
def login(credentials: Credentials):
    supabase = get_supabase()

    try:
        response = supabase.auth.sign_in_with_password({
            "email": credentials.email,
            "password": credentials.password
        })

        if response.user:
            profile = supabase.table('Users').select("*").eq("ID", response.user.id).single().execute().data
            if profile:
                user = User(
                    ID = profile['ID'],
                    email = profile['email'],
                    username = profile.get('username')
                )
                return Account(token = response.session.access_token, user = user)
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Login failed: {str(e)}")

    raise HTTPException(status_code=400, detail="Registration failed")