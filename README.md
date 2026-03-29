# S357-Book-Worm
## Frontend
```
./gradlew installDebug && adb shell am start -n "com.example.book_worm/.MainActivity"
```
## Backend
```
source venv/bin/activate
uvicorn main:app --reload
```