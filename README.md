# Online-Food-Ordering
This is the backend REST API for the Online Food Ordering System, built using Spring Boot, MySQL, and Docker.   It includes endpoints for authentication, restaurant management, menu, cart, feedback, payment (Razorpay), and more.

---
### **Development & Deployment**

1. **Clone the repo:**
   git clone https://github.com/Swizaljanice/Online-Food-Ordering
2. **Build the app:**
   mvn clean package
3. **Run with Docker Compose:**
   docker compose up --build -d
4. **API Documentation (Swagger UI):**  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---
# API Endpoints Documentation

Below are the major controllers for the Online Food Ordering System backend. Each section includes endpoint details, sample requests/responses, and a screenshot of the API in action.

## RestaurantUserController
Handles user registration and fetching all registered users.

---

### 1. **Register User**

**POST** `/foodOrderingSystem/users/register`

Registers a new user with email, role, and name.

#### Request Example
<img width="1798" height="795" alt="image" src="https://github.com/user-attachments/assets/58d3117c-a35c-487b-8780-b4a4e6449e74" />

#### Success Response

<img width="1773" height="738" alt="image" src="https://github.com/user-attachments/assets/7d927dc3-9471-4d90-a82f-4f1416a143e5" />



