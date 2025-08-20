![](/Users/darsh/Desktop/er_diag.webp)
# Authentication Database Schema
This project uses a relational database schema to handle **users, roles, and tokens** for authentication and authorization.
## 🗄️ Tables
### `users`
* `user_id` (PK, UUID, NOT NULL) – unique identifier for each user
* `user_name` (VARCHAR(30), NOT NULL) – username
* `password` (VARCHAR(255), NOT NULL) – securely hashed password
### `roles`
* `role_id` (PK, AUTO INCREMENT, NOT NULL) – unique identifier for each role
* `name` (VARCHAR(30), NOT NULL) – role name (e.g., `ADMIN`, `USER`)
### `users_roles`
* Join table for mapping users ↔ roles (many-to-many).
* `user_id` (FK → users.user\_id)
* `role_id` (FK → roles.role\_id)
### `tokens`
* `id` (PK, AUTO INCREMENT, NOT NULL) – unique identifier for each token
* `token` (VARCHAR(255), NOT NULL) – authentication token string (e.g., UUID/JWT)
* `expiry_date` (TIMESTAMP, NOT NULL) – expiry timestamp of the token
## 🔗 Relationships
* **Users ↔ Roles**: Many-to-Many
    * Implemented via the `users_roles` join table.
    * One user can have multiple roles, and one role can belong to multiple users.
* **Users ↔ Tokens**: One-to-One
    * Each user is linked to exactly one token entry.
