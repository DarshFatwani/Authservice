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
# 🔑 JWT Service Revision Notes
### 1. Purpose
* Handles **creation, parsing, and validation of JWT tokens**.
* Works with Spring Security’s `UserDetails`.
### 2. Key Components
* **SECRET** → base64 string, used as signing key (never hardcode in prod).
* **Key** → generated from SECRET using `Decoders.BASE64.decode` + `Keys.hmacShaKeyFor`.
### 3. Token Creation Flow
* **Method:** `GenerateToken(username)`
* Uses → `createToken(Map claims, String username)`
  * `setClaims()` → extra data (empty now).
  * `setSubject(username)` → main identity stored in JWT.
  * `setIssuedAt()` → time token created.
  * `setExpiration()` → when token expires (here = 1 min).
  * `signWith(key, HS256)` → sign using HMAC-SHA256.
  * `.compact()` → returns final JWT string.
### 4. Token Parsing & Claim Extraction
* **Method:** `extractClaim(token, Function<Claims,T>)`
  * Generic extractor (username, expiration, etc.).
* **`extractUsername(token)`** → pulls subject (username).
* **`extractExpiration(token)`** → pulls expiration time.
* **`extractAllClaims(token)`**
  * `Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody()`
  * Verifies signature + returns `Claims`
### 5. Validation
* **Method:** `validateToken(token, userDetails)`
  * Compares `extractUsername(token)` with `userDetails.getUsername()`.
  * Ensures token is **not expired** (`isTokenExpired(token)`).
* Returns **true** only if:
  * Signature valid ✅
  * Username matches ✅
  * Token not expired ✅
### 6. Inbuilt Stuff Used
* **Spring Security:** `UserDetails` (to check username).
* **JJWT Library:**
  * `Claims` (payload)
  * `Jwts.builder()` (create)
  * `Jwts.parser()` (parse)
  * `SignatureAlgorithm.HS256`
  * `Decoders.BASE64`, `Keys.hmacShaKeyFor()`
* **Java:** `Date`, `Map`, `Function`
### 7. Pitfalls to Remember
* Expiration = **1 min** → good for demo, too short for real systems.
* SECRET must be **kept safe** → env variables, not hardcoded.
* Expired / invalid tokens → `parseClaimsJws` will throw exceptions (handle in filter).
* For real-world → also implement **Refresh Tokens**.
👉 Think of the flow like this:
**Login → GenerateToken → Client stores JWT → Sends JWT with request → Server extracts claims & validates → If valid, user authenticated.**