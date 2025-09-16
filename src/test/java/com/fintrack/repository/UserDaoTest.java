package com.fintrack.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.*;
import java.util.*;

import com.fintrack.db.DatabaseManager;
import com.fintrack.entity.User;

public class UserDaoTest {
	private static DatabaseManager manager;

    /**
     * Connect to the in-memory SQLite database before each test runs
     * @throws SQLException failed to connect
     */
    @BeforeEach
    public void setup() throws SQLException {
        manager = new DatabaseManager();
    }

    /**
     * Close the connection to the database after each tests runs
     * @throws SQLException failed to close the connection
     */
    @AfterEach
    public void cleanup() throws SQLException {
        manager.shutdown();
    }

    @Test
    public void saveUserTest() throws SQLException {
        Tables tables = new Tables(manager.getConnection());
        UserDao userDao = new UserDao(manager.getConnection());
        User user = new User(1,"John", "john@doe.com");

        assertTrue(tables.createUsers());
        assertNotNull(userDao.save(user));
        assertNotNull(userDao.removeUserById(1));
    }

    @Test
    public void findAllUsersTest() throws SQLException {
        Tables tables = new Tables(manager.getConnection());
        UserDao userDao = new UserDao(manager.getConnection());
        User user = new User(1,"John", "john@doe.com");
        User userTwo = new User(2,"Jane", "jane@doe.com");

        assertTrue(tables.createUsers());
        assertNotNull(userDao.save(user));
        assertNotNull(userDao.save(userTwo));

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
        assertTrue(users.get(0).getName().equals("John"));
        assertTrue(users.get(users.size() - 1).getName().equals("Jane"));
        assertNotNull(userDao.removeUserById(1));
        assertNotNull(userDao.removeUserById(2));
        assertTrue(userDao.findAll().isEmpty());
    }

    @Test
    public void removeUserTest() throws SQLException {
        Tables tables = new Tables(manager.getConnection());
        UserDao userDao = new UserDao(manager.getConnection());
        User user = new User(1,"John", "john@doe.com");

        assertTrue(tables.createUsers());
        assertNotNull(userDao.save(user));
        assertNotNull(userDao.removeUserById(1));
    }
}