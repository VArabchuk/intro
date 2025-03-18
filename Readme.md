# Book-store

## Introduction
Welcome to the BookStore API! This is a simple API made with Spring Boot. It helps manage books, 
categories, and orders. Users can sign up, log in, browse books, and make orders safely. 
The project is created to be easy to use and secure, with different roles for users.

## Project structure
- **User Registration & Authentication**: Users can register, log in, and use the system safely.
- **Book Management**: Operations with books: adding, updating, and deleting.
- **Order Management**: Users can create orders, view history, and make payments.
- **Role-based Access Control**: Different access levels for users and admins.
- **Swagger UI**: Easy interface to explore the API and test endpoints.

## Technologies & Tools
- **MySQL**: Database for storing the application's data.
- **JUnit**: For writing unit and integration tests.
- **Spring Boot**: Main framework used for building the application.
- **Spring Data JPA**: Used for easy database interaction and ORM support.
- **Spring Security**: Provides security, user authentication, and authorization.
- **Swagger**: API documentation and testing tool.
- **Liquibase**: For automatic database schema management.
- **Docker Compose**: For managing multi-container applications.
- **Postman**: For testing API endpoints.
- **Docker**: For containerization and running the application in a containerized environment.

## Endpoints & User Roles:
### For admin:
* Create a new book
* Update book by ID
* Delete book by ID
* Create book category
* Update book category by ID
* Delete book category by ID
* Change order status

### For authorized users:
* Get all books (maintaining Pageable and Sorting tools)
* Get all books by category ID (maintaining Pageable and Sorting tools)
* Get book by ID
* Search for books based on specific parameters (title, author, ISBN)
* Get all categories (maintaining Pageable and Sorting tools)
* Get category by ID
* Registration with proper eMail, creating an eight-character password
* User Login authenticates the user and returns a JWT token
* Add item to shopping cart
* Update item in shopping cart
* Delete item from shopping cart
* Get information about shopping cart content
* Create order (all shopping cart items transfer into new order)
* Get user orders
* Get item from order by ID
* Get all order items (maintaining Pageable and Sorting tools)

## Models and relations
![Model Image](Model.jpg)

## Technologies Required
### Backend:
- Spring Boot – A framework for building Java applications
- Spring Security – Manages authentication and authorization
- Spring Web – Tools for developing web applications
- Spring Data JPA – Simplifies database operations
- JWT (JSON Web Tokens) – Used for authentication
- Lombok – Reduces boilerplate code with annotations
- MapStruct – Simplifies object mapping
- Swagger – API documentation and testing
### Database:
- MySQL – Primary database
- Liquibase – Automates database change management
### Containerization:
- Docker – Runs the application in containers

## How to Use
- **Clone the Repository**  
You can visit the repository [here](https://github.com/VArabchuk/intro.git),
or clone the project using the following command:
  ```bash
  git clone https://github.com/VArabchuk/intro.git
 
- Rename and edit the **.env.template** File: Rename a file to **.env** in the root directory of the project and update the following variables:
   ```dotenv
  MYSQLDB_USER=yourname
  MYSQLDB_PASSWORD=yourpassword
  MYSQLDB_DATABASE=yourdatabase
  MYSQLDB_ROOT_PASSWORD=yourrootpassword
  MYSQL_LOCAL_PORT=3307
  MYSQL_DOCKER_PORT=3306

  SPRING_LOCAL_PORT=8081
  SPRING_DOCKER_PORT=8080
  DEBUG_PORT=5005
- Run Docker Compose:
   ```bash
  docker-compose up --build
- Access the Application: Open your browser and go to http://localhost:8080.
- You can find the Postman collection for this API [here](BookStore.postman_collection.json).
