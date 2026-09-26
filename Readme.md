
# 🛒 SwiftCart — E-Commerce Platform

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)]()
[![React](https://img.shields.io/badge/React-18-blue.svg)]()
[![MongoDB](https://img.shields.io/badge/MongoDB-Database-green.svg)]()

SwiftCart is a full-stack **e-commerce platform** designed for managing products, customers, shopping carts, orders, and administrative operations.

The project demonstrates modern web application development using **Java, Spring Boot, React, and MongoDB**, with a focus on clean architecture, REST APIs, authentication, and practical e-commerce workflows.

## ✨ Features

### 🛍️ Customer Features

* Browse and search products
* Product categories and filtering
* Product details and reviews
* Add/remove products from cart
* Update product quantities
* Place and manage orders
* User registration and authentication
* Customer profile management

### 👨‍💼 Admin Features

* Admin authentication
* Product management
* Category management
* User management
* Order management
* Inventory management
* View and manage customer orders

## 🏗️ Tech Stack

**Frontend**

* React
* JavaScript
* HTML5
* CSS3

**Backend**

* Java
* Spring Boot
* Spring Security
* REST APIs

**Database**

* MongoDB

**Authentication**

* JWT-based authentication

## 📁 Project Structure

```text
SwiftCart/
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── ...
│
└── README.md
```

## 🚀 Getting Started

### Prerequisites

Make sure you have installed:

* Java JDK 17+
* Node.js
* npm
* MongoDB
* Git

### Clone

```bash
git clone https://github.com/SAIF-YASEEN/SwiftCart.git
cd SwiftCart
```

### Backend

```bash
cd backend
mvn spring-boot:run
```

### Frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Configure your MongoDB connection and required environment variables before starting the application.

## 🔐 Security

SwiftCart includes authentication and authorization features using Spring Security and JWT.

Protected operations require appropriate authentication and user permissions.

## 📦 Core Modules

```text
Authentication
     ↓
Users → Products → Cart → Orders
                    ↓
                 Reviews

Admin
 ├── Products
 ├── Categories
 ├── Users
 └── Orders
```

## 🎯 Project Goals

SwiftCart was built to demonstrate practical experience with:

* Full-stack development
* Java and Spring Boot
* REST API development
* React frontend development
* MongoDB
* Authentication and authorization
* CRUD operations
* E-commerce business logic
* Client-server communication

## 👨‍💻 Developer

**Saif Yaseen**

Full-Stack Software Developer

GitHub: **SAIF-YASEEN**

Portfolio: **https://saifportfolios.vercel.app/**

## 📄 License

This project is available under the MIT License.

---

<div align="center">

### 🛒 SwiftCart

**A full-stack e-commerce platform built with Java, Spring Boot, React & MongoDB.**

</div>
