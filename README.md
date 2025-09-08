Crystal clear ✅ — you want me to **write each iteration document as if I’m mentoring someone who is learning step by step while coding the project**. That means:

* Explaining concepts before tasks.
* Using **MySQL** instead of PostgreSQL.
* Keeping a **teaching tone** (why we’re doing something, not just what).
* Every iteration builds on knowledge.

Let’s redo **Iteration 1** in that style.

---

# 📘 Iteration 1 – Java + MySQL Foundations

## 🎯 Goal

In this first iteration, we want to:

1. Set up a **Java project** using Spring Boot.
2. Install and configure **MySQL locally**.
3. Connect Java to MySQL using **Spring Data JPA**.
4. Implement our first simple REST endpoints (`/health` and `/users`).

By the end of this sprint, you’ll have written your first **Java + SQL application**, tested it, and laid the foundation for all future iterations.

---

## 🧠 Concepts to Learn First

Before coding, here are the key things you need to understand:

1. **Spring Boot**: A Java framework that makes it easier to build APIs without writing too much boilerplate code. It gives you a quick way to create REST endpoints.

2. **MySQL Database**: A relational database where we’ll store our data. Think of it like a set of connected Excel sheets where we can run queries.

3. **JPA (Java Persistence API)**: A way for Java to talk to the database using classes (`User`) instead of writing SQL manually every time. Under the hood, it still generates SQL for you.

4. **CRUD**: Short for **Create, Read, Update, Delete** — the basic operations for working with data. We’ll start with Create (POST) and Read (GET).

---

## 📌 User Stories for Iteration 1

1. **As a developer**, I want to run a Spring Boot app so that I can start building my finance tracker.

   * Acceptance: `mvn spring-boot:run` starts the app.

2. **As a developer**, I want a health check endpoint so that I can confirm my app is running.

   * Acceptance: `GET /health` returns `{ "status": "UP" }`.

3. **As a developer**, I want to store and retrieve users from a MySQL database so that I know Java ↔ MySQL works.

   * Acceptance: I can `POST` a user (`name`, `email`) and `GET` it back.

---

## 🛠️ Step-by-Step Tasks

### 1. Install Tools

* **Java**: Install JDK 17 or higher. Verify with:

  ```bash
  java -version
  ```

* **Maven**: This is our build tool. Verify with:

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

### 2. Create Spring Boot Project

Go to [Spring Initializr](https://start.spring.io/). Choose:

* **Project**: Maven
* **Language**: Java
* **Spring Boot**: Latest stable
* **Dependencies**:

  * Spring Web
  * Spring Data JPA
  * MySQL Driver

Download, unzip, and open in your IDE (IntelliJ or Eclipse).

---

### 3. Configure Database Connection

Open `src/main/resources/application.yml` (create it if it doesn’t exist):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fintrack
    username: finuser
    password: finpass
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

* `ddl-auto: update` means Hibernate will create/update tables automatically.
* `show-sql: true` lets you see generated SQL in the console (great for learning).

---

### 4. Implement Health Check

Create a simple controller:

```java
@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
```

Try running the app:

```bash
mvn spring-boot:run
```

Visit [http://localhost:8080/health](http://localhost:8080/health).

---

### 5. Create `User` Entity + Repository

**Entity (table mapping):**

```java
import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // Getters and setters
}
```

**Repository (auto CRUD):**

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
```

**Controller:**

```java
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return repo.save(user);
    }

    @GetMapping
    public List<User> list() {
        return repo.findAll();
    }
}
```

---

### 6. Test Your Work

Run the app again:

```bash
mvn spring-boot:run
```

Test with curl:

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com"}'

curl http://localhost:8080/users
```

You should see the user stored and retrieved from MySQL! 🎉

---

## 📅 Iteration 1 Suggested Timeline (1 Week)

* **Day 1–2**: Install tools (Java, Maven, MySQL). Create DB and user.
* **Day 3**: Scaffold Spring Boot project. Verify `/health`.
* **Day 4–5**: Configure MySQL connection. Implement `User` entity.
* **Day 6**: Test endpoints. Debug SQL errors if any.
* **Day 7**: Demo to yourself: show `/health`, create/list users. Write learnings in README.

---

## ✅ Definition of Done (Iteration 1)

* App runs locally with `mvn spring-boot:run`.
* `/health` works.
* Can `POST` and `GET` users with MySQL persistence.
* Repo has README with **setup instructions for a beginner**.

---

👉 In **Iteration 2**, we’ll add **expenses & incomes**, plus write **SQL queries for reporting** (like “top spending categories”).

---

Do you want me to go ahead and **write Iteration 2 in the same beginner-teaching style**, or do you want to digest Iteration 1 first before moving on?
