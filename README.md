### FinTrack API

FinTrack is a lightweight and robust **Java-based RESTful API** for managing personal finances. Built with **Javalin**, it provides a set of endpoints for tracking income, expenses, and overall financial summaries. The API uses a **SQLite database** to store financial data, making it easy to set up and run without complex configurations.

-----

### Key Features

  * **User Management**: Easily create, retrieve, and delete user profiles.
  * **Transaction Tracking**: Log both income and expenses with detailed information.
  * **Financial Summaries**: Get categorized summaries of spending, monthly balance trends, and a comprehensive view of all transactions.
  * **Database Integration**: Utilizes SQLite for a lightweight, file-based database solution.
  * **Static File Serving**: Serves a user-friendly HTML dashboard to interact with the API.

-----

### Technology Stack

  * **Java**: The core language for the backend.
  * **Javalin**: A modern, lightweight web framework for building the REST API.
  * **SQLite**: A self-contained, serverless database engine for data persistence.

-----

### Getting Started

#### Prerequisites

  * **Java Development Kit (JDK) 11** or higher.
  * **Maven** (if you are managing dependencies).

#### Running the Application

1.  **Clone the repository**:

    ```bash
    git clone https://github.com/your-username/FinTrack.git
    cd FinTrack
    ```

2.  **Build the project**:

    ```bash
    mvn clean install
    ```

3.  **Run the application**:
    The server will start on port `8080` by default.

    ```bash
    java -jar target/fintrack.jar
    ```

    You can also run the `main` method directly from your IDE.

-----

### API Endpoints

The API is accessible at `http://localhost:8080` by default. All requests and responses are in JSON format.

#### User Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/users` | Creates a new user. |
| `GET` | `/users/{id}` | Retrieves a user by their ID. |
| `GET` | `/all` | Retrieves all users. |
| `DELETE` | `/users/{id}` | Deletes a user by their ID. |

#### Expense Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/users/{id}/expenses` | Adds a new expense for a user. |
| `GET` | `/users/{id}/expenses` | Retrieves all expenses for a user. |
| `DELETE` | `/users/{id}/expenses` | Deletes an expense for a user. |
| `GET` | `/users/{id}/summary/categories` | Retrieves a summary of expenses by category. |

#### Income Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/users/{id}/income` | Adds a new income entry for a user. |
| `GET` | `/users/{id}/income` | Retrieves all income entries for a user. |
| `DELETE` | `/users/{id}/income` | Deletes an income entry for a user. |

#### Summary & Balance Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/users/{id}/transactions` | Retrieves all transactions (income and expenses) for a user. |
| `GET` | `/users/{id}/summary/trends` | Retrieves financial trends over time. |
| `GET` | `/users/{id}/balance` | Gets the current balance for a user. |
| `GET` | `/users/{id}/summary/monthly` | Retrieves a monthly financial summary. |

-----

### Dashboard

The project includes a simple **HTML dashboard** that is served from the `/public` directory. You can access it by navigating to `http://localhost:8080/dashboard.html` in your web browser. This dashboard provides a basic interface for interacting with the API and visualizing financial data.

-----

### Contribution

Feel free to fork the repository and contribute to FinTrack\! Whether you're fixing bugs, adding new features, or improving documentation, all contributions are welcome.