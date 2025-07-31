# 🧱 3D Model Store – Spring Boot App

A web application for managing and selling 3D-printable models. Users can browse categories, view product details, and register, log in, add models to a cart, and place orders.

### Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Hibernate
- Maven

---

### 🛠 How to Run

1. Clone the repository

```
git clone https://github.com/chupre/modelstore.git
cd modelstore
```
2. Configure the database
Create a PostgreSQL database named "modelshop" and add your DB credentials in .env according to .env.example.

3. Run the app
```
./mvnw spring-boot:run
```
---

## 📝 TODO

### Near Term

1. ~~Refactor package structure towards a feature-based architecture~~
2. ~~Add authentication and authorization (Spring Security)~~
3. Add shopping cart functionality
4. Implement orders and checkout process
5. Add seller functionality
6. Authentication enhancements (email confirmation, password reset, brute-force defense)

### Later

7. Integrate payment gateway (e.g. Stripe)
8. Add likes for models
9. Add reviews and ratings
10. Build a frontend (React or Vue)

### Other

11. Docker
12. Documentation ( OpenAPI + Postman collection (?) )
13. Unit testing
