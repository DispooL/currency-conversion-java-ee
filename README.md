# Currency Exchange Rates API

### Overview
Currency Exchange Rates API is a test task implemented as a Java EE-based RESTful API. It delivers functionalities for managing currencies and exchange rates, including the ability to view, add, and edit currency details, retrieve and update exchange rates, and calculate currency conversions. This service-oriented application is architected to operate as a backend system and does not come with a client-facing web interface.

The application's design and implementation are centered on modern enterprise application standards, utilizing Java EE features for robust backend services. For this test task, we've paid special attention to the clarity of the code, adherence to RESTful principles, and a clean, well-organized repository structure.

### Technologies & Libraries:
- Java EE: The application is built using Java Enterprise Edition which provides a powerful platform for building robust, scalable enterprise applications.
- JDBC: Java Database Connectivity (JDBC) is used to connect and execute queries with the MySQL database.
- HikariCP: Hikari Connection Pool is a high-performance JDBC connection pool library.
- MySQL: The application data is stored in a MySQL database.
- Gson: A Java serialization/deserialization library used to convert Java Objects into JSON and back.
- MapStruct: A code generator that simplifies the implementation of mappings between Java bean types.

### Maven Dependencies:
The following Maven dependencies are included in the `pom.xml` file for the necessary functionalities:

1. **javax.servlet-api**: To use Servlets for handling HTTP requests and responses.
2. **mysql-connector-java**: MySQL JDBC driver for connecting to MySQL database.
3. **com.zaxxer:HikariCP**: For database connection pooling which significantly improves performance by maintaining a pool of connections.
4. **com.google.code.gson**: For converting Java objects into JSON format and vice versa.
5. **org.mapstruct**: For automatically generating mapping code to convert between different object models (DTOs to Entities).
6. **javax.annotation-api**: Provides annotations for specifying the semantics of a Java program.

Each dependency serves a specific purpose - the servlet API for HTTP request handling, the MySQL connector for database connections, HikariCP for managing the connection pool, Gson for handling JSON, and MapStruct for object mapping.

### Architecture
The project follows a typical multi-tier architecture:

1. **Servlets (Controller Layer)**: Serve as the entry point for HTTP requests. Each servlet is designated for specific operations (e.g., adding a currency, getting exchange rates).
2. **Services (Service Layer)**: Encapsulate the business logic, making calls to the Repository layer for data access/manipulation.
3. **Repositories (Data Access Layer)**: Abstraction over the database operations. They use JDBC for querying the database.
4. **Entities/DTOs (Data Layer)**: Representations of database tables (Entities) and the format of data exchanged with the API clients (DTOs).

### Project Structure
The application is structured into multiple packages, each with its specific responsibility:

- **entities**: Contains domain models mapping to database tables (`Currency`, `ExchangeRate`).
- **repositories**: Contains `JdbcCurrencyRepository` and `JdbcExchangeRateRepository` for data access.
- **services**: Contains business logic (`CurrencyService`, `ExchangeRateService`).
- **servlets**: Contains Servlet classes handling HTTP requests.
- **dto**: Contains Data Transfer Objects for API responses.
- **utils**: Utility classes (`CurrencyUtils`).
- **exceptions**: Custom exception classes for specific error cases.
- **mappers**: Interfaces using MapStruct to convert between Entities and DTOs.
- **DatabaseConnectionFactory**: A singleton class providing a method to initialize and retrieve a database connection.

### Code Approach
Given the RESTful nature of the application, the code structure reflects the HTTP operations that can be performed. Each servlet class is annotated with `@WebServlet` and handles a specific path and HTTP method.

Servlets are divided by their functionality to comply with the Single Responsibility Principle, making the code more maintainable. For instance, `AddCurrencyServlet` is solely responsible for adding a new currency. This separation also provides clarity since one servlet can only have one URL mapping, as per the servlet specification.

### Handling Database Connections
Database connections are managed using HikariCP for efficiency and performance. The `DatabaseConnectionFactory` class is responsible for initializing the connection pool and providing connections to the repositories.

### Data Access and Business Layers Separation
The application separates concerns by having distinct layers for data access (`JdbcCurrencyRepository` and `JdbcExchangeRateRepository`) and business logic (`CurrencyService` and `ExchangeRateService`). This enhances the modularity and testability of the code.

### Exception Handling
Custom exceptions are thrown to signify specific business logic errors (e.g., `CurrencyAlreadyExistsException`). This makes error handling more granular and the application flow clearer.

### DTOs and Entity Mapping
DTOs are used to define the structure of the data being sent in API requests and responses. MapStruct mappers minimize the boilerplate code for converting between entities and DTOs.

### Error Handling in Servlets
Servlets handle various error conditions by responding with appropriate HTTP status codes (e.g., `HttpServletResponse.SC_BAD_REQUEST` for an invalid request).

### Servlet Lifecycle Methods
Lifecycle methods like `init()` and `destroy()` are used where necessary. For instance, `InitServlet` uses `init()` to set up the database connection when the servlet is first loaded.

### API Endpoints

The API provides endpoints for managing currencies and exchange rates:

- **POST `/currencies/add`**: Add a new currency.
- **GET `/currencies`**: Retrieve a list of all currencies.
- **GET `/currencies/{code}`**: Retrieve a specific currency by its code.
- **POST `/exchangeRate/add`**: Add a new exchange rate.
- **GET `/exchangeRates`**: Retrieve a list of all exchange rates.
- **GET `/exchangeRate/{pair}`**: Retrieve a specific exchange rate by currency pair (e.g., `USDEUR`).
- **PATCH `/exchangeRate/update`**: Update an existing exchange rate.
- **GET `/exchange`**: Calculate the conversion of an arbitrary amount from one currency to another.

Each servlet is mapped to the corresponding endpoint and handles the logic for that specific operation.

### Database Schema

The application interacts with two primary tables:

- **Currencies**
    - `id` (int): Auto-increment, primary key.
    - `code` (varchar): Currency code (e.g., USD).
    - `name` (varchar): Full currency name (e.g., US Dollar).
    - `sign` (varchar): Currency symbol (e.g., $).

- **ExchangeRates**
    - `id` (int): Auto-increment, primary key.
    - `baseCurrencyId` (int): Foreign key on `currencies.id`.
    - `targetCurrencyId` (int): Foreign key on `currencies.id`.
    - `rate` (int): Exchange rate of base currency unit to target currency unit. Stored as an integer for precision and to avoid floating-point issues.

### Conclusion

The Currency Exchange Rates API leverages Java EE technologies to provide a robust and scalable service for currency and exchange rate management. The codebase is designed to be modular and maintainable, with clear separation between the MVC layers. The use of JDBC and HikariCP optimizes database interaction, while MapStruct simplifies object mapping. The API is structured around RESTful principles, offering clear endpoints for client interaction.