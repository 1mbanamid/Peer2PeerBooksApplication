# Peer2PeerBooks

This repository contains a Java Spring Boot application called Peer2PeerBooks. It is designed to manage financial transactions and calculate profits based on these transactions. The application provides RESTful API endpoints to perform CRUD operations on transactions and calculate profits.

## Features

- **Transaction Management**: Users can save, delete, and retrieve financial transactions.
- **Profit Calculation**: The application automatically calculates profits based on the transactions and updates the database.
- **SEPA Instant Remaining**: Provides information about the remaining SEPA Instant limit for the day.

## Technologies Used

- Java
- Spring Boot
- Hibernate
- RESTful API
- MySQL

## Setup

To run this application locally, follow these steps:

1. Clone the repository to your local machine.
2. Set up a MySQL database and update the `application.properties` file with your database configuration.
3. Build the project using Maven: `mvn clean install`.
4. Run the application: `java -jar target/peer2peerbooks.jar`.
5. Access the application using the provided API endpoints.

## API Endpoints

- `POST /transactions/save`: Save a new transaction.
- `DELETE /transactions/delete/{id}`: Delete a transaction by ID.
- `GET /transactions/all`: Get all transactions.
- `GET /transactions/calculate-profits`: Calculate profits for all transactions.

## Screenshots

Include some screenshots of the application in action to provide a visual overview of its functionality.

## Contributors

- [@imbanamid](https://github.com/imbanamid)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
