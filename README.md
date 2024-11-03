# Cinema-API
This project is a comprehensive solution designed to manage movie data for cinema businesses or movie rental platforms. Developed using Spring Boot, this API allows for seamless interaction with movie records and incorporates a robust role-based access control (RBAC) system to ensure secure and appropriate access levels for different users.
Key Features:
1. Movie Management: Users can perform CRUD operations on movie records. This includes adding new movies with metadata (e.g., title, director, genre, release date), updating details, viewing movie information, and deleting entries.
2. Role-Based Access Control: The system assigns specific roles (e.g., admin, manager, viewer) to users. This ensures that only users with the appropriate permissions can perform certain operations. Admins can execute full CRUD functions, managers may only update or add, and viewers can only read data.
3. Secure Authentication and Authorization: The API integrates with Spring Security and supports JWT tokens  for enhanced security, ensuring that only authenticated and authorized users can access or modify movie data.
4. Extensive Data Handling: The API supports advanced querying, sorting, and filtering of movie records to facilitate efficient data retrieval and management.
5. Scalability: The API's modular design allows it to be scaled and integrated into larger systems, such as cinema booking platforms or streaming services.
6. Logging and Monitoring: Incorporates logging mechanisms to track user actions and ensure transparency, with options for audit trails to monitor CRUD activity.

Use Cases:
- Cinema Chains: A central API for managing movie listings across multiple theaters.
- Online Movie Databases: Allows controlled access for contributors to update movie details while maintaining data security.
- Internal Movie Archive: A tool for production companies or distributors to manage their movie catalogs.

