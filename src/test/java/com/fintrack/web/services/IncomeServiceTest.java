package com.fintrack.web.services;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fintrack.repository.Tables;
import com.fintrack.web.WebServer;
import com.fintrack.entity.*;


public class IncomeServiceTest {
	private static WebServer server;
    private static Tables tables;

	/**
     * Start the app server before all tests run
     */
    @BeforeAll
    public static void startServer() {
        server = new WebServer();
        server.setDatabaseManager("memory");
        server.start(0);
        tables = new Tables(server.getDBConnection());
    }

    @BeforeEach
    public void makeTable() {
        tables.createIncome();
    }

    @AfterEach
    public void discardTables() {
        tables.deleteTable("income");
    }

    /**
     * Stop the app server once all tests are run
     */
    @AfterAll
    public static void stopServer() {
    	server.stop();
    }

    @Test
    public void addIncomeTest() {
    	Income income = new Income(1, 10000.00, "salary");
    	TestUtilities.testAddIncome(server.getPort(), 1, income);
    }

    @Test
    public void removeIncomeByIdTest() {
        Income income = new Income(1, 10000.00, "salary");
        tables.insertInto(income);
        TestUtilities.testRemoveIncome(server.getPort(), 1);
    }

    @Test
    public void getUserIncomeTest() {
        Income income = new Income(1, 10000.00, "salary");
        Income income2 = new Income(1, 5000.00, "gift");
        tables.insertInto(income);
        tables.insertInto(income2);

        TestUtilities.testGetUserIncome(server.getPort(), 1, List.of(income, income2));
    }
}