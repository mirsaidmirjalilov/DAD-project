# 🍕 Food Delivery Marketplace

A containerized, polyglot‑persistence backend for a food delivery platform.  
Built with **Spring Boot**, **PostgreSQL**, **MongoDB**, **Redis**, and **NGINX** – orchestrated with Docker Compose.

---

## 📦 Features

- User authentication & role‑based access (Customer, Restaurant Owner, Admin)
- Restaurant & menu management
- Shopping cart & order placement
- Order tracking with event logging (MongoDB)
- Rate limiting (token‑bucket) to prevent abuse
- JWT security
- Caching with Redis
- API Gateway with NGINX

All services are defined in `docker-compose.yml` and can be started with one command.

---

## 🚀 Quick Start (Local Development)

### Prerequisites
- Docker & Docker Compose (or Docker Desktop)
- Git

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/mirsaidmirjalilov/DAD-project.git
   cd DAD-project
   docker compose up -d --build

   Configuration

Environment variables (if any) can be set in the backend service inside docker-compose.yml or via a .env file.
Default database credentials (for development only):
Service	Username	Password	Database
PostgreSQL	postgres	postgres	fooddelivery
MongoDB	-	-	fooddelivery
Redis	-	-	-
📡 API Endpoints (Main)

All endpoints are prefixed with /api/v1/.
Endpoint	Method	Description	Auth
/auth/register	POST	Register new user	None
/auth/login	POST	Login → JWT token	None
/restaurants	GET	List all restaurants	None
/restaurants/{id}/menu	GET	Get restaurant menu	None
/orders	POST	Place a new order	JWT
/orders/my	GET	View my orders	JWT
/orders/{id}/tracking	GET	Get tracking events	JWT
/orders/{id}/cancel	PUT	Cancel order	JWT
/admin/users	GET	List all users (Admin)	JWT (Admin)

For a complete list, see the Swagger UI (when running): http://localhost:8080/swagger-ui.html
🐳 Docker Compose Services
Service	Container name	Port (host)	Purpose
nginx	api-gateway	8080:80	Reverse proxy & gateway
backend	backend-app	- (internal)	Spring Boot application
postgres	postgres-db	5432:5432	Relational store
mongodb	mongodb-db	27017:27017	Tracking events
redis	redis-cache	6379:6379	Caching
