Perfect 👍 If we swap **Spring Boot** for **Javalin**, the approach changes a bit:

* Javalin is a **lightweight web framework** (you’ll code more directly, less magic than Spring Boot).
* You’ll still connect to **MySQL** (using JDBC or JPA/Hibernate — but for learning, I’ll suggest **JDBI** or plain JDBC\*\* so you see what’s happening).
* This makes Iteration 1 a great learning experience because you’ll touch the **HTTP layer** and the **SQL layer** directly.

Here’s the **Iteration 1 doc rewritten for Javalin** with the same teaching-first tone 👇

---

# 📘 Iteration 1 – Java + MySQL Foundations with Javalin

## 🎯 Goal

In this first iteration, you will:

1. Set up a **Java project** with Javalin.
2. Install and configure **MySQL locally**.
3. Connect Java to MySQL using **JDBC** (direct SQL queries).
4. Build your first REST endpoints (`/health`, `/users`).

By the end, you’ll have a running **Javalin + MySQL API**, with the ability to create and fetch users.

---

## 🧠 Concepts to Learn First

1. **Javalin**: A lightweight Java web framework. Unlike Spring Boot, it doesn’t auto-generate much — you’ll define routes (`app.get`, `app.post`) yourself. Great for learning HTTP fundamentals.

2. **MySQL Database**: A relational database for storing data. You’ll create a schema, connect from Java, and run SQL queries.

3. **JDBC (Java Database Connectivity)**: The standard way Java apps connect to SQL databases. You’ll write SQL queries directly in your code.

4. **CRUD**: Basic database operations: Create, Read, Update, Delete. We’ll start with Create (POST) and Read (GET).

---

## 📌 User Stories for Iteration 1

1. **As a developer**, I want to run a Javalin app so that I can build my finance tracker.

   * Acceptance: Running `mvn exec:java` starts the app.

2. **As a developer**, I want a health check endpoint so that I can confirm my app is alive.

   * Acceptance: `GET /health` returns `{ "status": "UP" }`.

3. **As a developer**, I want to store and retrieve users from MySQL so that I know Java ↔ MySQL works.

   * Acceptance: I can `POST` a user (`name`, `email`) and `GET` it back.

---

## 🛠️ Step-by-Step Tasks

### 1. Install Tools

* **Java**: Install JDK 17 or higher. Verify with:

  ```bash
  java -version
  ```

* **Maven**: Verify with:

  ```bash
  mvn -v
  ```

* **MySQL**: Install MySQL locally. Create a user and database:

  ```sql
  CREATE DATABASE fintrack;
  CREATE USER 'finuser'@'localhost' IDENTIFIED BY 'finpass';
  GRANT ALL PRIVILEGES ON fintrack.* TO 'finuser'@'localhost';
  FLUSH PRIVILEGES;
  ```

---

### 2. Create Javalin Project

Create a Maven project with this `pom.xml` (minimal):

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.fintrack</groupId>
    <artifactId>fintrack</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <dependencies>
        <!-- Javalin -->
        <dependency>
            <groupId>io.javalin</groupId>
            <artifactId>javalin</artifactId>
            <version>6.1.3</version>
        </dependency>

        <!-- MySQL Driver -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.33</version>
        </dependency>
    </dependencies>
</project>
```

---

### 3. Create the App Skeleton

```java
import io.javalin.Javalin;
import java.util.*;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);

        app.get("/health", ctx -> {
            ctx.json(Map.of("status", "UP"));
        });
    }
}
```

Run:

```bash
mvn compile exec:java -Dexec.mainClass="com.fintrack.App"
```

Visit [http://localhost:8080/health](http://localhost:8080/health).

---

### 4. Connect to MySQL with JDBC

Create a helper class for DB connection:

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/fintrack";
    private static final String USER = "finuser";
    private static final String PASSWORD = "finpass";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

---

### 5. Create User Model and Repository

**Model:**

```java
public class User {
    private int id;
    private String name;
    private String email;

    // constructor, getters, setters
}
```

**Repository (direct SQL):**

```java
import java.sql.*;
import java.util.*;

public class UserRepository {
    public void save(User user) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.executeUpdate();
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id, name, email FROM users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email")
                ));
            }
        }
        return users;
    }
}
```

Make sure you create the table in MySQL:

```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);
```

---

### 6. Add Endpoints

In `App.java`:

```java
UserRepository repo = new UserRepository();

app.post("/users", ctx -> {
    User user = ctx.bodyAsClass(User.class);
    repo.save(user);
    ctx.status(201).json(user);
});

app.get("/users", ctx -> {
    ctx.json(repo.findAll());
});
```

---

### 7. Test It

Run:

```bash
mvn compile exec:java -Dexec.mainClass="com.fintrack.App"
```

Test:

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com"}'

curl http://localhost:8080/users
```

You should see Alice appear from MySQL! 🎉

---

## 📅 Iteration 1 Suggested Timeline (1 Week)

* **Day 1–2**: Install tools (Java, Maven, MySQL). Create DB and `users` table.
* **Day 3**: Create Javalin skeleton with `/health`.
* **Day 4–5**: Implement JDBC connection + repository.
* **Day 6**: Build `/users` POST + GET.
* **Day 7**: Demo to yourself: show `/health`, add/list users. Write learnings.

---

## ✅ Definition of Done (Iteration 1)

* App runs with `mvn exec:java`.
* `/health` returns `{ "status": "UP" }`.
* MySQL database exists with `users` table.
* I can `POST` and `GET` users.
* Repo has a README with **step-by-step setup instructions for a beginner**.

---

👉 In **Iteration 2**, we’ll extend this by adding **expenses & incomes** (new tables, new endpoints, simple reports with SQL queries).

---

Would you like me to go ahead and **draft Iteration 2 in the same teaching style with Javalin + MySQL**?
