# TaskNest Frontend (ReactJS)

This folder now contains a ReactJS frontend built with Vite.

## Tech Stack

- React 18
- React Router DOM
- Vite

## Pages

- `/register` - User registration page
- `/login` - User login page
- `/dashboard` - Dashboard after successful login

## Run Frontend

1. Open a terminal in `frontend`
2. Install dependencies:
   - `npm install`
3. Start development server:
   - `npm run dev`
4. Open:
   - `http://localhost:5173/login`

## Backend Requirement

Make sure your Spring Boot backend is running at `http://localhost:8080` since the React app calls:

- `POST /api/auth/register`
- `POST /api/auth/login`
