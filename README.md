# NewBank

NewBank is a full-stack web application that simulates the core
operations of a modern digital banking system.\
The project includes modules for **account management**,
**transactions**, **payments**, **authentication**, and **user data
updates**, following real-world financial standards in security,
architecture, and usability.

This repository is part of an academic and practical development
initiative to demonstrate backend and frontend skills applied to banking
systems.

------------------------------------------------------------------------

## 📌 Features

### **Accounts & Transactions**

-   Create new accounts\
-   Check account balance\
-   Deposit, withdraw, and transfer funds\
-   Generate account statements\
-   Update personal data\
-   Authentication and authorization (secure access control)\
-   Notifications for critical operations

### **Payments**

-   Bill payment\
-   Manual payment processing\
-   Scheduled payments\
-   Payment history tracking

------------------------------------------------------------------------

## 🏗️ System Architecture

The project follows a layered architecture to maintain organization,
scalability, and maintainability:

-   **Frontend:** React + Tailwind CSS (or Next.js if applicable)\
-   **Backend:** Java + Spring Boot\
-   **Database:** PostgreSQL\
-   **API:** RESTful service designed with DTOs, services, entities,
    repositories and controllers\
-   **ORM:** Hibernate / JPA\
-   **Security:** Authentication headers, input validation, error
    handling, logging

------------------------------------------------------------------------

## 🛠️ Technologies Used

### **Frontend**

-   React.js\
-   Tailwind CSS\
-   Axios\
-   Next.js (optional)

### **Backend**

-   Java 17+\
-   Spring Boot\
-   Spring Web\
-   Spring Data JPA\
-   PostgreSQL\
-   HikariCP\
-   Lombok\
-   Hibernate

### **Development Tools**

-   Git and GitHub\
-   VS Code / IntelliJ / Rider\
-   Postman / Insomnia\
-   Docker (optional)

------------------------------------------------------------------------

## 🚀 Getting Started

Follow the steps below to run the project locally.

### **1. Clone the repository**

``` bash
git clone https://github.com/your-username/bank-account-transactions-payment.git
cd bank-account-transactions-payment
```

------------------------------------------------------------------------

# 📦 Backend Setup (Spring Boot)

### **2. Navigate to the backend folder**

``` bash
cd backend
```

### **3. Configure environment variables**

Create an `.env` file or update `application.properties`:

``` properties
spring.datasource.url=jdbc:postgresql://localhost:5432/newbank
spring.datasource.username=postgres
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### **4. Start the backend server**

Using Maven wrapper:

``` bash
./mvnw spring-boot:run
```

Or with Maven installed:

``` bash
mvn spring-boot:run
```

Backend available at:

    http://localhost:8080

------------------------------------------------------------------------

# 🎨 Frontend Setup (React)

### **5. Navigate to the frontend folder**

``` bash
cd front-end
```

### **6. Install dependencies**

``` bash
npm install
```

### **7. Start the development server**

``` bash
npm run dev
```

Frontend available at:

    http://localhost:5173

------------------------------------------------------------------------

## 📁 Project Folder Structure

    newbank/
    │
    ├── backend/
    │   ├── src/main/java/com/backend/newbank/
    │   ├── src/main/resources/
    │   ├── pom.xml
    │   └── ...
    │
    └── front-end/
        ├── src/
        ├── public/
        ├── package.json
        └── ...

------------------------------------------------------------------------

## 🔌 API Overview

Example endpoints (adjust to your implementation):

  Method   Endpoint                     Description
  -------- ---------------------------- -----------------------
  POST     /api/accounts                Create new account
  GET      /api/accounts/{id}           Get account details
  POST     /api/transactions/deposit    Deposit funds
  POST     /api/transactions/withdraw   Withdraw funds
  POST     /api/transactions/transfer   Transfer funds
  GET      /api/statements/{id}         Get account statement
  POST     /api/payments                Process a payment
  GET      /api/payments/history/{id}   Payment history

------------------------------------------------------------------------

## 🧪 Testing

Run backend tests:

``` bash
mvn test
```

Run frontend tests (if configured):

``` bash
npm test
```

------------------------------------------------------------------------

## 🤝 Contributing

Contributions are welcome.\
Follow these steps:

1.  Fork the repository\
2.  Create a new branch (`git checkout -b feature-name`)\
3.  Commit your changes\
4.  Push to your branch\
5.  Open a Pull Request

------------------------------------------------------------------------

## 📜 License

This project is for educational purposes and does not represent a real
banking institution.
