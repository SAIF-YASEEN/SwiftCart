# Quick Setup Guide

## Prerequisites
- Java 21 JDK
- Node.js 18+
- MongoDB (Atlas or Local)

## Environment Setup

### 1. Backend (.env file)
Create/update `backend/.env`:
```env
MONGODB_URI=mongodb+srv://Shriram2005:Shriram2005@cluster0.sfh7knq.mongodb.net/ecommerce?retryWrites=true&w=majority
JWT_SECRET=mySecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLong
CORS_ORIGINS=http://localhost:5173,http://localhost:3000
PORT=8080
SPRING_PROFILES_ACTIVE=dev
```

### 2. Frontend (.env file)
Create `frontend/.env`:
```env
VITE_API_URL=http://localhost:8080/api
```

## Running the Project

### Terminal 1 - Backend
```bash
cd backend
.\mvnw.cmd spring-boot:run
```
✅ Backend starts at `http://localhost:8080`

### Terminal 2 - Frontend
```bash
cd frontend
npm install
npm run dev
```
✅ Frontend starts at `http://localhost:5173`

## Access the App
- **User App**: http://localhost:5173
- **API Server**: http://localhost:8080/api

Done! 🎉
