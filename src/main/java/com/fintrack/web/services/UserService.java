package com.fintrack.web.service;

import io.javalin.http.Context;
import java.util.*;
import java.sql.Connection;

import com.fintrack.entity.*;
import com.fintrack.repository.*;
import com.fintrack.db.DatabaseManager;


public class UserService {
	private static UserDao userDao = new UserDao(new DatabaseManager().getConnection());

	public static void setDatabaseConnection(Connection connection) {
		userDao = new UserDao(connection);
	}

	public static void saveUser(Context context) {
		try {
			User user = context.bodyAsClass(User.class);
			User userInDb = userDao.save(user);
			context.status(200).json(userInDb);
		} catch (Exception e) {
			context.status(404);
		}
	}

	public static void findAllUsers(Context context) {
		context.status(200).json(userDao.findAll());
	}

	public static void removeUser(Context context) {
		try {
			int id = Integer.parseInt(context.pathParam("id"));
			User user = userDao.removeUserById(id);
			context.status(200).json(user);
		} catch (Exception e) {
			context.status(404);
			context.result(e.getMessage());
		}
	}
}