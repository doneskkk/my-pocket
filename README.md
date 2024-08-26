# My Pocket API

Welcome to the **My Pocket** API documentation! This service is designed to help you track and manage your finances efficiently.

## Table of Contents
- [Introduction](#introduction)
- [Execution Instructions](#execution-instructions)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- Soon 
## Introduction

Introducing **My Pocket** —your ultimate solution for managing finances effortlessly! Our API offers a seamless and intuitive way to handle budgets, categories, and transactions, designed with simplicity and flexibility in mind.

## Execution Instructions

To use this API, follow these steps:

1. **Install a REST Client:** Use tools like [Postman](https://www.postman.com/) or [cURL](https://curl.se/) to send HTTP requests.
2. **Set the Base URL:**
   - For local development: `http://localhost:3040`
3. **Add Authorization Header:** For endpoints that require authentication, include the `Authorization` header with your JWT token.
   - Header Format: `Authorization: Bearer YOUR_JWT_TOKEN`
4. **Send Requests:** Use the specified HTTP methods (GET, POST, DELETE) to interact with the API.

## API Endpoints
 - You can visit also SWAGGER - > http://localhost:3040/swagger-ui/index.html
### Categories

| Method  | Endpoint                  | Description                                |
|---------|---------------------------|--------------------------------------------|
| **POST**  | `/api/v1/categories`      | Create a new category                     |
| **DELETE**| `/api/v1/categories`      | Delete an existing category by ID          |
| **GET**   | `/api/v1/categories/{id}`  | Retrieve a category by ID                  |

### Budgets

| Method  | Endpoint                           | Description                                   |
|---------|------------------------------------|-----------------------------------------------|
| **POST**  | `/api/v1/budgets`                  | Create a new budget                          |
| **GET**   | `/api/v1/budgets`                  | Get a list of all budgets                    |
| **GET**   | `/api/v1/budgets/{id}`             | Retrieve a budget by ID                      |

### Transactions

| Method  | Endpoint                                          | Description                                   |
|---------|---------------------------------------------------|-----------------------------------------------|
| **POST**  | `/api/v1/budgets/{budgetId}/transactions`       | Add a new transaction to a specific budget    |
| **GET**   | `/api/v1/budgets/{budgetId}/transactions`       | Get all transactions for a specific budget    |
| **GET**   | `/api/v1/budgets/{budgetId}/transactions/{id}`  | Retrieve details of a specific transaction by ID |
| **DELETE**| `/api/v1/budgets/{budgetId}/transactions/{id}`  | Delete a specific transaction from a budget   |

### Authentication

| Method  | Endpoint            | Description                             |
|---------|---------------------|-----------------------------------------|
| **POST**  | `/api/auth/signup` | Register a new user                     |
| **POST**  | `/api/auth/signin` | Authenticate a user and generate a JWT  |

### Soon
- To implement pagination
- To optimize codebase
- To implement UI for easy interaction with our service

# If you want to contact me, write on chirildones@gmail.com
