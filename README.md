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

## RestaurantController

Handles management and retrieval of restaurant data.  
**Note:** Adding, updating, and deleting restaurants require `ADMIN` role authorization.

---

### 1. **Add a Restaurant** (Admin Only)

**POST** `/restaurants`

Adds a new restaurant. Requires `ADMIN` role.

## Register an Admin User

Before you can add, update, or delete restaurants, you need to register a user with the `ADMIN` role.

### **Register Admin Endpoint**

**POST** `/foodOrderingSystem/users/register`

Registers a new user as an admin.

#### Request Body Example

<img width="1777" height="763" alt="image" src="https://github.com/user-attachments/assets/8a605b94-5844-4cc8-a59e-b7e70e2530f6" />

#### Response Body Example

<img width="1769" height="715" alt="image" src="https://github.com/user-attachments/assets/eb3ac1a9-c86b-464a-9003-411b98b178ce" />

### **Login Admin Endpoint**

**POST** `/foodOrderingSystem/auth/login`
Login an user with role as admin.

#### Request Body Example
<img width="1769" height="503" alt="image" src="https://github.com/user-attachments/assets/906f18f0-8d4c-4c7c-9b1d-e86f1db07ed1" />

#### Response Body Example
<img width="1779" height="520" alt="image" src="https://github.com/user-attachments/assets/4c3e4575-937b-4d7d-9ea5-f4dd30dbb5a4" />

#### Add admin authorization 
<img width="1894" height="896" alt="image" src="https://github.com/user-attachments/assets/3f5fc3f8-c82b-4a93-89f9-7834a1ce08fa" />

#### Now Add Restaurant
#### Request Body
<img width="1768" height="737" alt="image" src="https://github.com/user-attachments/assets/29aabaee-32ff-467b-844f-1c19ed71e828" />

#### Response Body
<img width="1771" height="755" alt="image" src="https://github.com/user-attachments/assets/c398797f-c865-42b4-b655-91879a09d88e" />

## MenuCategoryController

Handles creation, update, retrieval, and deletion of menu categories for restaurants.

---

### 2. **Add Menu Category**

**POST** `/categories/addMenuCategory/{restaurantId}`

Adds a new menu category to a restaurant. 
**Authorization:** Requires `ADMIN` or `RESTAURANT_STAFF` role.

#### Request Body Example

<img width="1774" height="866" alt="image" src="https://github.com/user-attachments/assets/3d1f3aa0-ef6b-488c-91d1-5f7c241a485f" />

#### Response Body Example
<img width="1773" height="656" alt="image" src="https://github.com/user-attachments/assets/c31abbb7-13b0-432b-8c5b-33d1f5a10fb9" />

## MenuItemController

Handles creation, update, retrieval, and deletion of menu items within menu categories.

---

### 3. **Add Menu Item**

**POST** `/items/addMenuItem/{categoryId}`

Adds a new menu item to a specific category.  
**Authorization:** Requires `ADMIN` or `RESTAURANT_STAFF` role.

#### Request Body Example
<img width="1769" height="870" alt="image" src="https://github.com/user-attachments/assets/04224a5f-ee61-4d04-a4e7-0eaa1df51ba0" />

#### Response Body Example
<img width="1768" height="738" alt="image" src="https://github.com/user-attachments/assets/0ca07706-1b65-4bd6-b374-cbf422451fda" />

### 3. Get All Restaurants

**GET** `/restaurants`

Retrieves a list of all restaurants.  
This endpoint is **publicly accessible** (no role restriction).

#### Request Body Example
<img width="1775" height="269" alt="image" src="https://github.com/user-attachments/assets/4aa725e4-526a-424d-a1cb-49bc4e6cd513" />

#### Response Body Example
<img width="1765" height="904" alt="image" src="https://github.com/user-attachments/assets/99fcff96-f48d-47cc-b7b4-0abd1d3c9fa7" />

## CartController

Handles operations related to the user's shopping cart.

---

### 1. Add Item to Cart

**POST** `/cart/addItemToCart`

Adds a menu item to the authenticated user's cart, or updates its quantity if it already exists.

---
#### Request Parameters

| Parameter    | Type  | Description                     |
|--------------|-------|---------------------------------|
| `menuItemId` | Long  | The ID of the menu item to add  |
| `quantity`   | int   | Quantity of the menu item       |

#### Authorization

- Requires the user to be authenticated.
- The user is identified from the authentication token.

#### Request Body Example
<img width="1773" height="502" alt="image" src="https://github.com/user-attachments/assets/0a0b1922-2b02-4a32-93e0-fd2d2017006c" />

#### Response Body Example
<img width="1776" height="700" alt="image" src="https://github.com/user-attachments/assets/c0d4ae1c-2d93-4915-856e-42a4beac6d07" />

### 2. Update Cart Item Quantity

**PUT** `/cart/updateCartItem`

Updates the quantity of an existing item in the authenticated user’s cart.

#### Request Parameters

| Parameter    | Type  | Description                   |
|--------------|-------|-------------------------------|
| `cartItemId` | Long  | The ID of the cart item to update |
| `quantity`   | int   | The new quantity for this item |

#### Authorization

- User must be authenticated.
- Identified by JWT token in the Authorization header.

#### Request Body example
<img width="1775" height="515" alt="image" src="https://github.com/user-attachments/assets/2d500972-2e1c-42d4-bb27-2fc561a48341" />

#### Response Body example
<img width="1773" height="679" alt="image" src="https://github.com/user-attachments/assets/71cbe9fa-1166-4e14-8729-aed8d7c90953" />

### 3. Remove Cart Item

**DELETE** `/cart/removeCartItem/{cartItemId}`

Removes a specific item from the authenticated user’s cart by its cart item ID.

#### Path Parameter

| Parameter    | Type | Description                     |
|--------------|------|---------------------------------|
| `cartItemId` | Long | The ID of the cart item to remove |

#### Authorization

- User must be authenticated.
- Identified by JWT token in the Authorization header.

#### Request body example
<img width="1770" height="408" alt="image" src="https://github.com/user-attachments/assets/581083b7-583f-4b71-bca0-e1a14f12cd02" />

#### Response body example
<img width="1774" height="675" alt="image" src="https://github.com/user-attachments/assets/ca3b5588-31aa-47df-933f-285cd574ace3" />

### 4. Checkout

**POST** `/cart/checkout`

Completes the purchase by checking out the authenticated user’s cart using the specified payment method.

#### Request Body Example
<img width="1778" height="722" alt="image" src="https://github.com/user-attachments/assets/b43ba23e-c666-4aa8-a5f4-03be06c43527" />

#### Response Body Example
<img width="1772" height="844" alt="image" src="https://github.com/user-attachments/assets/36542aac-9039-4f5a-a8e3-69ad2e261bc8" />

## PaymentController

Handles payment processing using Razorpay integration.

---

### Start Razorpay Payment

**POST** `/payments/razorpay`

Initiates a Razorpay payment by creating an order with the given order ID and amount.

#### Request Body Example
<img width="1771" height="733" alt="image" src="https://github.com/user-attachments/assets/80ac0a22-220c-428f-9aa8-91ec9f979af5" />

## OrderController

---

### 1. Get Order Tracking

**GET** `/orders/{orderId}/tracking`

Retrieves the tracking history for a specific order.  
**Authorization:** Requires `CUSTOMER` role. The order must belong to the authenticated user.

#### Path Parameter

| Parameter   | Type | Description            |
|-------------|------|------------------------|
| `orderId`   | Long | The ID of the order to track |

#### Request body example
<img width="1774" height="408" alt="image" src="https://github.com/user-attachments/assets/76c1f43c-5298-4997-b9c0-95a4c681a8af" />

#### Response body example
<img width="1776" height="573" alt="image" src="https://github.com/user-attachments/assets/2f188e0e-0564-4d55-9a6e-b0fcde86d4ef" />

### 2. Get Order Status

**GET** `/orders/getOrderStatus/{orderId}/status`

Retrieves the current status of a specific order.  
**Authorization:** Accessible by users with `CUSTOMER`, `RESTAURANT_STAFF`, or `ADMIN` roles.

#### Path Parameter

| Parameter  | Type | Description               |
|------------|------|---------------------------|
| `orderId`  | Long | The ID of the order       |

#### Request Body example
<img width="1776" height="400" alt="image" src="https://github.com/user-attachments/assets/8b9226fc-bdab-4817-acdb-daa8141f2652" />

#### Response Body example
<img width="1780" height="543" alt="image" src="https://github.com/user-attachments/assets/3a16a613-b5b6-481d-ad5f-beeb52b17426" />

### 3. Update Order Status

**PUT** `/orders/updateOrderStatus/{orderId}/status`

Updates the status of a specific order.  
**Authorization:** Requires `RESTAURANT_STAFF` or `ADMIN` role.

#### Path Parameter

| Parameter  | Type | Description               |
|------------|------|---------------------------|
| `orderId`  | Long | The ID of the order to update |

#### Request Body Example
<img width="1771" height="863" alt="image" src="https://github.com/user-attachments/assets/732d2047-9ed3-4ecf-82be-82895a11bc25" />

#### Response Body Example
<img width="1766" height="825" alt="image" src="https://github.com/user-attachments/assets/3f8bf433-8c19-47ce-964d-89203f63c6da" />


---

## Technologies Used

- Spring Boot
- MySQL
- Spring Data JPA
- Spring Security (JWT)
- Docker & Docker Compose
- Swagger (springdoc-openapi)
- Razorpay (payments)

---

## Folder Structure
```
foodOrderingSystem/
├── .idea/
├── .mvn/
├── logs/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── online/
│   │   │           └── foodOrderingSystem/
│   │   │               ├── config/
│   │   │               ├── controller/
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── CartController.java
│   │   │               │   ├── FeedbackController.java
│   │   │               │   ├── MenuCategoryController.java
│   │   │               │   ├── MenuItemController.java
│   │   │               │   ├── OrderController.java
│   │   │               │   ├── PaymentController.java
│   │   │               │   ├── RestaurantController.java
│   │   │               │   └── RestaurantUserController.java
│   │   │               ├── dto/
│   │   │               ├── entity/
│   │   │               ├── exception/
│   │   │               ├── repository/
│   │   │               ├── security/
│   │   │               ├── service/
│   │   │               │   ├── CartService.java
│   │   │               │   ├── CustomUserDetailsService.java
│   │   │               │   ├── FeedbackService.java
│   │   │               │   ├── MenuCategoryService.java
│   │   │               │   ├── MenuItemService.java
│   │   │               │   ├── OrderService.java
│   │   │               │   ├── PaymentService.java
│   │   │               │   ├── RestaurantService.java
│   │   │               │   └── RestaurantUserService.java
│   │   │               └── util/
│   │   │                   └── FoodOrderingSystemApplication.java
│   │   └── resources/
│   └── test/
│       └── java/
│           └── com/
│               └── online/
│                   └── foodOrderingSystem/
│                       ├── controller/
│                       │   ├── AuthControllerTest.java
│                       │   ├── CartControllerTest.java
│                       │   ├── FeedbackControllerTest.java
│                       │   ├── MenuCategoryControllerTest.java
│                       │   ├── MenuItemControllerTest.java
│                       │   ├── OrderControllerTest.java
│                       │   ├── PaymentControllerTest.java
│                       │   ├── RestaurantControllerTest.java
│                       │   └── RestaurantUserControllerTest.java
│                       ├── security/
│                       ├── service/
│                       └── FoodOrderingSystemApplicationTest.java
│   └── resources/
├── target/
├── .gitattributes
├── .gitignore
├── docker-compose.yml
├── Dockerfile
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md

```

---

