\# Vehicle Service Center Management System



\## Project Overview

The Vehicle Service Center Management System is a Spring Boot REST API application designed to manage the daily operations of a vehicle service center. It provides APIs to manage customers, vehicles, mechanics, service bookings, spare parts, and invoices.



\## Key Features

\- Customer management with CRUD operations

\- Vehicle registration and management

\- Service booking and status management

\- Mechanic management

\- Spare parts inventory and low-stock monitoring

\- Invoice generation and payment status management

\- File upload and download

\- Input validation and centralized exception handling

\- Spring Data JPA and MySQL integration

\- Transaction management and auditing

\- Pagination, sorting, and custom database queries

\- Caching and scheduled stock monitoring

\- Spring Security authentication

\- Swagger/OpenAPI documentation

\- Spring Boot Actuator monitoring

\- Unit testing with JUnit and Mockito



\## Technologies Used

\- Java 25

\- Spring Boot 4.1.1

\- Spring Web

\- Spring Data JPA

\- Hibernate

\- MySQL

\- Maven

\- Spring Security

\- Swagger/OpenAPI

\- Spring Boot Actuator

\- JUnit 5 and Mockito

\- Git and GitHub



\## Project Architecture

The application follows a layered architecture:



Controller → Service → Service Implementation → Repository → MySQL Database



DTOs are used to handle request and response data, while entity classes represent database records.



\## Prerequisites

\- Java 25 or a compatible configured JDK

\- MySQL Server

\- Git

\- Maven, or the included Maven Wrapper



\## Database Configuration

1\. Start your MySQL server.

2\. Create the database `vehicle\_service\_center`.

3\. Create the required tables and relationships using the project's database schema or JPA configuration.

4\. Configure the database connection using environment variables.



Example Windows Command Prompt configuration:



```cmd

set DB\_URL=jdbc:mysql://localhost:3306/vehicle\_service\_center

set DB\_USERNAME=root

set DB\_PASSWORD=your\_mysql\_password

```



Replace `your\_mysql\_password` with your local MySQL password. Do not commit real passwords to GitHub.



\## How to Run

1\. Clone the repository:



```bash

git clone https://github.com/shahithyarajavelu/Vehicle\_service\_management.git

```



2\. Open the project folder:



```bash

cd Vehicle\_service\_management

```



3\. Configure the database environment variables in the terminal where you will run the application.



4\. Run the application using the Maven Wrapper on Windows:



```cmd

mvnw.cmd spring-boot:run

```



Alternatively, import the project into Spring Tool Suite (STS) as an existing Maven project and run `VehicleServiceCenterApplication.java`.



\## API Documentation

After starting the application, open:



http://localhost:8080/swagger-ui/index.html



\## Health Monitoring

Spring Boot Actuator health endpoint:



http://localhost:8080/actuator/health



\## Testing

The project includes unit tests using JUnit 5 and Mockito. Run the test suite using:



```cmd

mvnw.cmd test

```



\## Sample File

The repository includes a sample vehicle service report in the `uploads` directory for file-management demonstration.



\## Author

Developed as a Spring Boot project demonstrating REST API development, database integration, validation, security, testing, and monitoring.

