package com.fintrack.web;

import io.javalin.http.Context;
import java.util.Map;
import java.sql.Connection;

import com.fintrack.dao.User;
import com.fintrack.db.DatabaseManager;
import com.fintrack.repository.*;

public class APIHandler {

	static MainDao mainRepo = null;
	
	public static void initializeDB(Connection connection) {
		mainRepo = new MainDao(connection);
	}

	public static void getHealth(Context context) {
		context.json(Map.of("status", "UP"));
	}

	public static void saveUser(Context context) {
		try {
			User user = context.bodyAsClass(User.class);
			User userInDb = mainRepo.userDao.save(user);
			context.status(201).json(userInDb);
		} catch (Exception e) {
			context.status(404);
		}
	}

	public static void findAllUsers(Context context) {
		context.status(201).json(mainRepo.userDao.findAll());
	}

	public static void removeUser(Context context) {
		try {
			int id = Integer.parseInt(context.pathParam("id"));
			User user = mainRepo.userDao.removeUserById(id);
			context.status(200).json(user);
		} catch (Exception e) {
			context.status(404);
		}
	}
}