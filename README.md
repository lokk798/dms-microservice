

# Document Microservice

This microservice handles document management within a microservices-based system. It supports creating, searching, categorizing, and assigning documents to departments, with secure access based on JWT authentication.
📤 **File uploads are delegated to the S3 Storage Microservice via `StorageClient`.**


---

## Authentication

All secured endpoints require a valid JWT in the `Authorization` header:

```
Authorization: Bearer <token>
```

The token must include:

* `sub` (subject): user ID
* `roles`: list of user roles (e.g., `ROLE_USER`, `ROLE_ADMIN`)

---

##  Features

* Manage Documents (create, search, upload files)
* Categorize documents
* Assign users to departments
* Access control based on roles (`ROLE_USER`, `ROLE_ADMIN`)
* File upload support (via `StorageClient`)
* JWT authentication filter

---

## Getting Started

### Prerequisites

* Java 17+
* Gradle

### Configuration

In `application.yml` or `application.properties`:

```yaml
jwt:
  secret: your_jwt_secret_key
  tokenPrefix: "Bearer "
  headerName: "Authorization"
  expirationTime: 86400000
```

Set up DB and storage configuration as needed.

### Run the Service

```bash
./gradlew bootRun
```

Visit `http://localhost:8081`.

---

## Sample Endpoints

### Authentication (via JWT)

Handled externally (by the auth microservice).

---

### Documents

| Method | Endpoint                | Auth Role  | Description                         |
| ------ | ----------------------- | ---------- | ----------------------------------- |
| POST   | `/api/documents`        | ROLE\_USER | Create a new document               |
| GET    | `/api/documents`        | ROLE\_USER | Get documents by user/search/filter |
| POST   | `/api/documents/upload` | ROLE\_USER | Upload a file to a document         |

---

### Departments

| Method | Endpoint                                | Auth Role   | Description                   |
| ------ | --------------------------------------- | ----------- | ----------------------------- |
| GET    | `/api/departments`                      | ROLE\_ADMIN | List all departments          |
| POST   | `/api/departments`                      | ROLE\_ADMIN | Create department             |
| PUT    | `/api/departments/{id}`                 | ROLE\_ADMIN | Update department             |
| DELETE | `/api/departments/{id}`                 | ROLE\_ADMIN | Delete department             |
| POST   | `/api/departments/{deptId}/users/{uid}` | ROLE\_ADMIN | Assign user to department     |
| DELETE | `/api/departments/{deptId}/users/{uid}` | ROLE\_ADMIN | Unassign user from department |

---

### Categories

| Method | Endpoint               | Auth Role   | Description           |
| ------ | ---------------------- | ----------- | --------------------- |
| GET    | `/api/categories`      | ROLE\_ADMIN | List all categories   |
| POST   | `/api/categories`      | ROLE\_ADMIN | Create a new category |
| PUT    | `/api/categories/{id}` | ROLE\_ADMIN | Update category       |
| DELETE | `/api/categories/{id}` | ROLE\_ADMIN | Delete category       |



---

## 📄 Notes

* File storage is handled externally by the **S3 Storage Microservice**.
* This microservice uses `StorageClient` to communicate with the S3 Storage Microservice for upload functionality.
* User details beyond ID are managed by the **Authentication Microservice**.

---

## 📘 API Documentation

All endpoints are documented using **OpenAPI 3** with annotations via `springdoc-openapi`.

To view the Swagger UI:

```

[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

```

