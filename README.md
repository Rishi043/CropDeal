# 🌾 Crop Deal – Microservices-Based Agricultural Marketplace

Crop Deal is an end-to-end agricultural marketplace platform built using **Java Spring Boot** and **React.js**, enabling seamless interaction between farmers and dealers. Designed with a focus on scalability, reliability, and efficient communication, it follows a robust **microservices architecture**.

---

## 📌 Key Highlights

- 🔐 JWT-Based Authentication with role management
- 📦 Microservices architecture with centralized configuration
- 📬 Email notifications via SMTP integration
- 💳 Payment confirmation through email 
- 🛠️ Circuit Breaker pattern for fault tolerance

---

## 🧩 Microservices Breakdown

| Service                | Description                                                                 |
|------------------------|-----------------------------------------------------------------------------|
| **User Service**        | Manages user registration, login, profile updates, role-based access, and password reset via secure email. |
| **Crop Service**        | Allows farmers to manage crop listings and dealers to view and subscribe. |
| **Order Service**       | Handles crop purchase workflow and Real-time order status tracking.          |
| **Notification Service**| Sends real-time alerts to farmers and dealers via email using SMTP.         |
| **Payment Service**     | Sends payment success confirmation via email to users.                     |
| **API Gateway**         | Routes client requests, with integrated circuit breaker mechanism.         |
| **Config Server**       | Centralized configuration source for all services.                         |
| **Eureka Server**       | Service discovery across microservices.                                    |

---

## 🚀 Features

### 👥 User Service
- Farmer & Dealer Registration / Login
- Role-based access control (Admin / Farmer / Dealer)
- JWT Authentication
- Profile management (Update / Edit / Delete)
- Secure password reset link via email (expires in 15 minutes)

### 🌱 Crop Service
- Farmers can add, update, delete crop listings
- Dealers can browse crops and receive notifications on new listings

### 📦 Order Service
- Dealers can place orders for crops with quantity validation
- Real-time order status tracking (Pending → Success)

### 📬 Notification Service
- Sends email notifications using SMTP
- Dealers notified when new crops are added
- Farmers notified when a dealer shows interest

### 💳 Payment Service
- Payment metadata stored securely
- Sends email confirmation to users after successful transaction
- Includes transaction ID, amount, crop details in the email

---

## 💡 Tech Stack

### 🛠 Backend
- Java 17, Spring Boot
- Spring Security & JWT
- Spring Cloud Gateway
- Spring Data JPA
- MySQL
- Lombok
- Eureka Discovery Server
- Spring Config Server
- JavaMailSender (SMTP for emails)

### 💻 Frontend
- React.js, Tailwind CSS
- Axios for REST API communication
- React Router DOM

---

## 🗺 Architecture Overview

![Microservices Architecture Diagram](Microservices%20Architecture%20Diagram.png)

---

## 🧪 Recent Updates

- 🔁 Added Forgot and Reset Password Feature with Email Expiry Logic
- ✉️ Integrated SMTP for Notification & Payment Confirmation
- 🧱 Circuit Breaker Added for API Gateway Resilience

---

## 📁 Repository Structure

| Directory             | Description                                 |
|-----------------------|---------------------------------------------|
| `.idea/`              | Project settings and IDE metadata            |
| `apigateway/`         | Manages routing and resilience features      |
| `configserver/`       | Central config repository                    |
| `cropservice/`        | Crop-related CRUD operations                 |
| `eurekaserver/`       | Microservices registration and discovery     |
| `notificationservice/`| Email notifications and subscription logic   |
| `orderservice/`       | Order creation and order status tracking     |
| `paymentservice/`     | Payment confirmation and transaction record  |
| `userservice/`        | User authentication, profile and reset logic |
| `README.md`           | Project documentation                        |


