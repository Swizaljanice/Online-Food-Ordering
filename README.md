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


## AuthController

Handles user authentication and JWT token generation.

---
### 1. **Login Endpoint**

**POST** `/foodOrderingSystem/auth/login`

Authenticates a user with email and password, and returns a JWT token upon success.

#### Request Parameters
- `email` (string) — User’s email address (as query parameter)
- `password` (string) — User’s password (as query parameter)
<img width="1774" height="561" alt="image" src="https://github.com/user-attachments/assets/f53d8150-77f3-43ec-81bb-dbce2307e05c" />

#### Success Response
<img width="1772" height="533" alt="image" src="https://github.com/user-attachments/assets/a46bd632-1263-4cf0-8425-4d448519a4e4" />

#### Fetch the given token for the user and add it in "Authorize"

After receiving the token in the login response:

1. **Copy the token string.**
2. In Swagger UI, click the **"Authorize"** button.
3. Paste your token with the prefix `Bearer ` (including the space):
4. Click **"Authorize"** to use this token for all subsequent protected requests.

<img width="1893" height="912" alt="image" src="https://github.com/user-attachments/assets/bf308a5a-2c19-491a-83b4-c73ca6ccd1fa" />
<img width="1894" height="907" alt="image" src="https://github.com/user-attachments/assets/88ff38f1-ab67-4f49-8cdb-5b851f8a2d26" />







