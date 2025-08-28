# 3D Model Store

A full-stack 3D model store web application where users can browse, purchase, and sell 3D models. Supports buyer, seller, and admin roles, with secure authentication, email verification, and integrated Yookassa payment gateway.

---

## Table of Contents

* [Features](#features)
* [Tech Stack](#tech-stack)
* [Getting Started](#getting-started)

  * [Prerequisites](#prerequisites)
  * [Environment Setup](#environment-setup)
  * [Running with Docker](#running-with-docker)
* [Yookassa Payment Gateway Setup](#yookassa-payment-gateway-setup)
* [API Documentation](#api-documentation)
* [Frontend Demo](#frontend-demo)
* [Postman Collection](#postman-collection)
* [Project Structure](#project-structure)
  
---

## Features

* User roles: **BUYER**, **SELLER**, and **ADMIN**
* Authentication & authorization
* Email verification and password reset
* Product categories and listings
* Shopping carts and order management
* Product comments and likes
* User profiles with purchase history and products
* Yookassa payment gateway integration
* Dockerized backend and frontend
* Swagger API documentation

---

## Tech Stack

* **Backend:** Spring Boot, Java 17, Hibernate, Maven
* **Database:** PostgreSQL 16
* **Frontend:** React, Tailwind CSS, shadcn
* **Payment Gateway:** [Yookassa](https://yookassa.ru/)
* **Containerization:** Docker, Docker Compose

---

## Getting Started

### Prerequisites

* Docker & Docker Compose
* Java 17, PostgreSQL 16 (if running backend without Docker)
* Node.js 20+ & npm/yarn (if running frontend without Docker)

### Environment Setup

1. Copy the example `.env` file in root directory:

```bash
cp .env.example .env
```

2. Update the `.env` files with your environment-specific variables (database credentials, JWT secret, etc.).
When using `dev` spring profile, database is getting populated with mock data.
To view test user credentials, go to `populate_users.sql`.
To set placeholder for images and models, create `./uploads/images/placeholder.jpg` and `./uploads/models/placeholder.obj`. 

---

### Running with Docker

Build and start both backend and frontend containers:

```bash
docker compose up --build
```

The app should be accessible at `http://localhost:5432` (frontend) and backend API at `http://localhost:8080` (default port).

---

## Yookassa Payment Gateway Setup

1. Create an account at [Yookassa](https://yookassa.ru/) and set up a store.
2. For testing, create a **test store** and **test payout gateway**.
3. Configure webhooks:

* Store notifications: `server_url/webhook/yookassa`
* Payout notifications: `server_url/webhook/yookassa/payout`

4. For local testing, expose your local server using `localtunnel`:

```bash
npx localtunnel --port SERVER_PORT
```

Replace `SERVER_PORT` with your backend port.

---

## API Documentation

* Swagger docs available at: `http://localhost:8080/swagger-ui.html`

---

## Frontend Demo
[Demo on YT](https://youtu.be/av4kPbWWIpk)

---

## Postman Collection

[Collection](https://www.postman.com/webmonkeys-7454/my-workspace/collection/1jlimhv/modelstore?action=share&source=copy-link&creator=37892243)

---

## Project Structure

```
root/
├─ src/               # Spring Boot backend
├─ frontend/              # React frontend
├─ docker-compose.yml     # Docker compose setup
├─ .env.example           # Backend environment variables example
└─ frontend/.env.example  # Frontend environment variables example
```

---
