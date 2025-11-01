# Library Frontend (Vite + React)

This frontend is tailored to your `LibraryManagementRESTProject` endpoints:

- `GET    /books` → list books
- `GET    /books/{id}` → book details
- `GET    /search?title=&author=&genre=` → search with filters
- `POST   /register` (JSON: {firstName, lastName, email, password}) → create member
- `POST   /login`    (JSON: {email, password}) → sign in
- `POST   /borrow/{memberId}/{bookId}` → borrow a book
- `POST   /return/{memberId}/{bookId}` → return a book
- `GET    /history/{memberId}` → member history

## Quick start

1. Install deps
   ```bash
   npm i
   ```
2. Configure backend base URL:
   ```bash
   cp .env.example .env
   # edit VITE_API_BASE=...
   ```
3. Run the app
   ```bash
   npm run dev
   ```

By default, it points to `http://localhost:8080/LibraryManagementRESTProject/rest/Library`.
