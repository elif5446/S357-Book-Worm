from fastapi import FastAPI
from API.authentication import router as API_router

app = FastAPI()
app.include_router(API_router, tags=["Authentication"])

@app.get("/")
def read_root():
    return {"status": "success"}