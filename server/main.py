from fastapi import FastAPI
from API.authentication import router as API_router
from API.comments import router as comments_router
from API.bookclub import router as bookclub_router
from API.reviews import router as reviews_router
from API.reactions import reactions_router
from API.review_comments import review_comments_router

app = FastAPI()
app.include_router(API_router, tags=["Authentication"])
app.include_router(comments_router, tags=["Chapter Comments"])
app.include_router(bookclub_router, tags=["Book Clubs"])
app.include_router(reviews_router, tags=["Reviews"])
app.include_router(reactions_router, tags=["Reactions"])
app.include_router(review_comments_router, tags=["Review Comments"])

@app.get("/")
def read_root():
    return {"status": "success"}