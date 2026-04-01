from fastapi import APIRouter, HTTPException
import os
from supabase import create_client, Client
from DTOs.user import Credentials, User, Account
from dotenv import load_dotenv

load_dotenv()
router = APIRouter()

def get_supabase() -> Client:
    url = os.environ.get('SUPABASE_URL')
    key = os.environ.get('SUPABASE_API_KEY')  # anon key for auth operations
    if not url or not key:
        raise HTTPException(status_code=500, detail="Supabase credentials missing.")
    return create_client(url, key)

def get_supabase_db() -> Client:
    url = os.environ.get('SUPABASE_URL')
    key = os.environ.get('SUPABASE_SERVICE_KEY') or os.environ.get('SUPABASE_API_KEY')
    if not url or not key:
        raise HTTPException(status_code=500, detail="Supabase credentials missing.")
    return create_client(url, key)

@router.post("/register/")
def register(credentials: Credentials):
    supabase = get_supabase()       # anon key — for auth.sign_up
    db = get_supabase_db()          # service key — for Users table insert

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
            db.table('Users').insert({
                'ID': response.user.id,
                'email': response.user.email,
                'username': response.user.user_metadata.get("full_name")
            }).execute()

            db.table('ClubMembers').insert({
                'club_id': '00000000-0000-0000-0000-000000000001',
                'user_id': response.user.id
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
    supabase = get_supabase()       # anon key — for auth.sign_in_with_password
    db = get_supabase_db()          # service key — for Users table select

    try:
        response = supabase.auth.sign_in_with_password({
            "email": credentials.email,
            "password": credentials.password
        })

        if response.user:
            # Use limit(1) instead of .single() so it never crashes on 0 rows
            rows = db.table('Users').select("*").eq("ID", str(response.user.id)).limit(1).execute().data

            if rows:
                profile = rows[0]
                profile_username = profile.get('username') or profile.get('usernamen')
            else:
                # User exists in Auth but not in Users table — create the row now
                username = response.user.user_metadata.get("full_name") or credentials.email.split("@")[0]
                db.table('Users').insert({
                    'ID': str(response.user.id),
                    'email': response.user.email,
                    'username': username
                }).execute()
                profile = {'ID': str(response.user.id), 'email': response.user.email, 'username': username}
                profile_username = username

            user = User(
                ID=response.user.id,
                email=response.user.email,
                username=profile_username
            )
            return Account(token=response.session.access_token, user=user)

    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Login failed: {str(e)}")

    raise HTTPException(status_code=400, detail="Login failed")
