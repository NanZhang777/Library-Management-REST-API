# Library Management System

A full-stack library management web application with a **Jakarta EE / JAX-RS REST backend** and a **React + Vite frontend**.

---

## Features

- Browse all available books
- Search books by title, author, or genre
- Member registration and login (session persisted in `localStorage`)
- Borrow and return books (available copies updated in real time)
- View personal borrowing history with borrow date, due date, and return date

---

## Tech Stack

| Layer    | Technology                                      |
|----------|-------------------------------------------------|
| Backend  | Java, Jakarta EE, JAX-RS (RESTful Web Services) |
| Frontend | React 18, React Router v6, Vite 5               |

---

## Project Structure

```
library-frontend/          # React frontend (this repo)
│
├── src/
│   ├── main.jsx           # App entry point
│   ├── App.jsx            # Root component, routing, session management
│   ├── Books.jsx          # Book list, search, borrow/return
│   ├── Auth.jsx           # Login and registration forms
│   ├── History.jsx        # Borrowing history page
│   └── api.js             # Centralized API client (fetch wrapper)
│
├── index.html
├── vite.config.js         # Dev server + proxy config
└── package.json

LibraryManagementRESTProject/   # Jakarta EE backend (separate project)
└── src/main/java/libraryManagementREST/
    ├── LibraryResource.java    # JAX-RS REST controller
    ├── LibraryService.java     # Business logic (Singleton)
    ├── Book.java               # Book entity
    ├── Member.java             # Member entity
    └── BorrowRecord.java       # Borrow record entity
```

---

## REST API Endpoints

Base URL: `http://localhost:8080/LibraryManagementRESTProject/rest/Library`

| Method | Path                            | Description                      |
|--------|---------------------------------|----------------------------------|
| GET    | `/books`                        | Get all books                    |
| GET    | `/books/{id}`                   | Get book by ID                   |
| GET    | `/search?title=&author=&genre=` | Search books by query params     |
| POST   | `/register`                     | Register a new member            |
| POST   | `/login`                        | Login                            |
| POST   | `/borrow/{memberId}/{bookId}`   | Borrow a book                    |
| POST   | `/return/{memberId}/{bookId}`   | Return a book                    |
| GET    | `/history/{memberId}`           | Get member's borrowing history   |
| GET    | `/booksList`                    | Get books list (plain text)      |

---

## Getting Started

### Prerequisites

- Java application server (e.g., Apache Tomcat 10+) running on port `8080`
- Node.js (v18+) and npm

### 1. Deploy the Backend

Deploy `LibraryManagementRESTProject` to your Java application server.

Verify it is running:
```
GET http://localhost:8080/LibraryManagementRESTProject/rest/Library/books
```

**Pre-loaded sample data:**

| Type   | Data                                               |
|--------|----------------------------------------------------|
| Books  | The Great Gatsby, 1984, To Kill a Mockingbird      |
| Member | John Doe — `john.doe@example.com` / `password123` |

> Note: Data is stored in-memory and resets on each server restart.

### 2. Run the Frontend

```bash
cd library-frontend
npm install
npm run dev
```

The app will be available at `http://localhost:5173`.

The Vite dev server automatically proxies all `/api/*` requests to the backend:

```
/api/login  →  http://localhost:8080/LibraryManagementRESTProject/rest/Library/login
```

No extra configuration is needed for local development.

---

## Environment Variables

For production builds, create a `.env` file in the `library-frontend` root:

```
VITE_API_BASE=http://your-backend-host/LibraryManagementRESTProject/rest/Library
```

In development, the Vite proxy in `vite.config.js` handles routing automatically.

---

## How It Works

### Session Management

User login state is stored in `localStorage` as a JSON object. The session survives page refreshes and is cleared on logout.

```json
{ "memberId": 1, "firstName": "John", "lastName": "Doe", "email": "john.doe@example.com" }
```

### Backend Architecture

`LibraryService` is implemented as an **eager Singleton** — one instance is shared across all requests, holding books, members, and borrow records in `HashMap` collections in memory.

Borrow records track:
- Member ID and Book ID
- Borrow date and due date (2 weeks from borrow date)
- Return date (set when book is returned)
