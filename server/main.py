from fastapi import FastAPI
from API.authentication import router as API_router
from API.comments import router as comments_router
from API.bookclub import router as bookclub_router

app = FastAPI()
app.include_router(API_router, tags=["Authentication"])
app.include_router(comments_router, tags=["Chapter Comments"])
app.include_router(bookclub_router, tags=["Book Clubs"])

@app.get("/")
def read_root():
    return {"status": "success"}