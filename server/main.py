from fastapi import FastAPI
from API.authentication import router as API_router
from API.comments import router as comments_router

app = FastAPI()
app.include_router(API_router, tags=["Authentication"])
app.include_router(comments_router, tags=["Chapter Comments"])

@app.get("/")
def read_root():
    return {"status": "success"}