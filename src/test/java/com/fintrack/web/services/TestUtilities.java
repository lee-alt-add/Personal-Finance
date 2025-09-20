package com.fintrack.web.services;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import com.fintrack.entity.*;

public class TestUtilities {

    /*
    * ---------
    * Requests
    * ---------
    */
	public static HttpResponse<JsonNode> addUserRequest(int port, User user) {
        String userInfo = "{\"name\":\"%s\", \"email\":\"%s\"}".formatted(user.getName(), user.getEmail());
        String url = "http://localhost:%d/users".formatted(port);
        return Unirest.post(url).body(userInfo).asJson();
    }

    public static HttpResponse<JsonNode> getUserRequest(int port, int id) {
        String url = "http://localhost:%d/users/%d".formatted(port, id);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> findAllUsersRequest(int port) {
        String url = "http://localhost:%d/all".formatted(port);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> removeUserRequest(int port, int id) {
        String url = "http://localhost:%d/users/%d".formatted(port, id);
        return Unirest.delete(url).asJson();
    }

    public static HttpResponse<JsonNode> addExpenseRequest(int port, int userId, Expense expense) {
    	String expenseInfo = "{\"amount\":%.2f%n,\"category\":\"%s\",\"description\":\"%s\"}"
                .formatted(
                    expense.getAmount(), 
                    expense.getCategory(), 
                    expense.getDescription()
                );

        String url = "http://localhost:%d/users/%d/expenses".formatted(port, userId);
        return Unirest.post(url).body(expenseInfo).asJson();
    }

    public static HttpResponse<JsonNode> removeExpenseRequest(int port, int expenseId) {
        String url = "http://localhost:%d/users/%d/expenses".formatted(port, expenseId);
        return Unirest.delete(url).asJson();
    }

    public static HttpResponse<JsonNode> getUserExpensesRequest(int port, int userId) {
        String url = "http://localhost:%d/users/%d/expenses".formatted(port, userId);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> addIncomeRequest(int port, int userId, Income income) {
        String expenseInfo = "{\"amount\":%.2f%n,\"source\":\"%s\"}"
                             .formatted(income.getAmount(), income.getSource());

        String url = "http://localhost:%d/users/%d/income".formatted(port, userId);
        return Unirest.post(url).body(expenseInfo).asJson();
    }

    public static HttpResponse<JsonNode> removeIncomeRequest(int port, int incomeId) {
        String url = "http://localhost:%d/users/%d/income".formatted(port, incomeId);
        return Unirest.delete(url).asJson();
    }

    public static HttpResponse<JsonNode> getUserIncomeRequest(int port, int userId) {
        String url = "http://localhost:%d/users/%d/income".formatted(port, userId);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> getUserTransactionsRequest(int port, int userId) {
        String url = "http://localhost:%d/users/%d/transactions".formatted(port, userId);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> getUserBalanceRequest(int port, int userId) {
        String url = "http://localhost:%d/users/%d/balance".formatted(port, userId);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> getUserMonthlySummaryRequest(int port, int userId) {
        String url = "http://localhost:%d/users/%d/summary/monthly".formatted(port, userId);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> getCategoryExpenditureRequest(int port, int userId) {
        String url = "http://localhost:%d/users/%d/summary/categories".formatted(port, userId);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> getTrendsRequest(int port, int userId) {
        String url = "http://localhost:%d/users/%d/summary/trends".formatted(port, userId);
        return Unirest.get(url).asJson();
    }
    /*
    * ---------
    * Testers
    * ---------
    */
    public static void testGetTrends(int port, int userId, List<IncomeAndExpensesPerMonth> trends) {
        HttpResponse<JsonNode> response = getTrendsRequest(port, userId);
        assertEquals(200, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();

        assertEquals(trends.size(), jsonArray.length());

        JSONObject jsonObject = jsonArray.getJSONObject(0);
        IncomeAndExpensesPerMonth trend = trends.get(0);

        assertEquals(trend.getMonth(), jsonObject.get("month"));
        assertEquals(trend.getIncome(), jsonObject.get("income"));
        assertEquals(trend.getExpenses(), jsonObject.get("expenses"));
    }

    public static void testGetCategoryExpenditure(int port, int userId, Map<String, Double> expenseCategory) {
        HttpResponse<JsonNode> response = getCategoryExpenditureRequest(port, userId);
        assertEquals(200, response.getStatus());
        JSONObject jsonObject = response.getBody().getObject();

        assertEquals(expenseCategory.size(), jsonObject.length());
    }

    public static void testGetUserMonthlySummary(int port, int userId, MonthlySummary monthlySummary) {
        HttpResponse<JsonNode> response = getUserMonthlySummaryRequest(port, userId);
        assertEquals(200,response.getStatus());
        JSONObject jsonObject = response.getBody().getObject();

        assertEquals(monthlySummary.getIncome(), jsonObject.get("income"));
        assertEquals(monthlySummary.getExpenses(), jsonObject.get("expenses"));
        assertEquals(monthlySummary.getBalance(), jsonObject.get("balance"));
    }

    public static void testGetUserBalance(int port, int userId, double expectedBalance) {
        HttpResponse<JsonNode> response = getUserBalanceRequest(port, userId);
        assertEquals(200, response.getStatus());
        JSONObject jsonObject = response.getBody().getObject();
        assertEquals(expectedBalance, jsonObject.getDouble("amount"));
    }

    public static void testGetUserTransactions(int port, int userId) {
        HttpResponse<JsonNode> response = getUserTransactionsRequest(port, userId);
        assertEquals(200, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(2, jsonArray.length());

        JSONObject jsonObject = jsonArray.getJSONObject(0);
        assertEquals("salary", jsonObject.get("source"));
    }

    public static void testGetUserIncome(int port, int userId, List<Income> itemsAdded) {
        HttpResponse<JsonNode> response = getUserIncomeRequest(port, userId);
        assertEquals(200, response.getStatus());

        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(itemsAdded.size(), jsonArray.length());
    }

    public static void testRemoveIncome(int port, int incomeId) {
        HttpResponse<JsonNode> removalResponse = removeIncomeRequest(port, incomeId);
        assertEquals(200, removalResponse.getStatus());
    }

    public static void testRemoveExpense(int port, int expenseId) {
        HttpResponse<JsonNode> removalResponse = removeExpenseRequest(port, expenseId);
        assertEquals(200, removalResponse.getStatus());
    }

    public static void testAddIncome(int port, int userId, Income income) {
        HttpResponse<JsonNode> response = addIncomeRequest(port, userId, income);
        assertEquals(200, response.getStatus());

        JSONObject jsonObject = response.getBody().getObject();
        assertTrue(income.getSource().equals(jsonObject.get("source")));
    }

    public static void testGetUserExpenses(int port, int userId, List<Expense> itemsAdded) {
        HttpResponse<JsonNode> response = getUserExpensesRequest(port, userId);
        assertEquals(200, response.getStatus());

        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(itemsAdded.size(), jsonArray.length());
    }

    public static HttpResponse<JsonNode> testAddUser(int port, User user) {
        HttpResponse<JsonNode> response = addUserRequest(port, user);
        assertEquals(200, response.getStatus());

        JSONObject jsonObject = response.getBody().getObject();

        assertEquals(user.getName(), jsonObject.get("name"));
        assertEquals(user.getEmail(), jsonObject.get("email"));
        return response;
    }

    public static void testGetUser(int port, int userId, User user) {
        HttpResponse<JsonNode> response = getUserRequest(port, userId);
        assertEquals(200, response.getStatus());

        JSONObject userData = response.getBody().getObject();

        assertEquals(user.getName(), userData.get("name"));
        assertEquals(user.getEmail(), userData.get("email"));
        assertEquals(userId, userData.getInt("id"));
    }

    public static void testRemoveUser(int port, int id) {
        // Get id (as per the database) of user that was saved
        HttpResponse<JsonNode> removalResponse = removeUserRequest(port, id);
        assertEquals(200, removalResponse.getStatus());
    }

    public static HttpResponse<JsonNode> testAddExpense(int port, int userId, Expense expense) {
        HttpResponse<JsonNode> response = addExpenseRequest(port, userId, expense);
        assertEquals(200, response.getStatus());

        JSONObject jsonObject = response.getBody().getObject();
        assertEquals(expense.getCategory(), jsonObject.get("category"));
        assertEquals(expense.getDescription(), jsonObject.get("description"));

        return response;
    }

    public static int getDatabaseId(HttpResponse<JsonNode> response) {
        JSONObject jsonObject = response.getBody().getObject();

        if (jsonObject == null) {
            jsonObject =  response.getBody().getArray().getJSONObject(0);
        }
        
        return jsonObject.getInt("id");
    }
}