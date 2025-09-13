package com.fintrack.web;

import io.javalin.http.Context;
import java.util.Map;

import com.fintrack.dao.User;
import com.fintrack.db.DatabaseManager;
import com.fintrack.repository.*;
import java.sql.SQLException;

public class APIHandler {

	static DatabaseManager manager = new DatabaseManager();
	static UserDao repo = new UserDao(manager.getConnection());
	static Tables userTable = new Tables(manager.getConnection());
	
	public static void initializeDB() {
		userTable.createUsers();
	}

	public static void getHealth(Context context) {
		context.json(Map.of("status", "UP"));
	}

	public static void saveUser(Context context) {
		try {
			User user = context.bodyAsClass(User.class);
			User userInDb = repo.save(user);
			context.status(201).json(userInDb);
		} catch (Exception e) {
			context.status(404);
		}
	}

	public static void findAllUsers(Context context) {
		context.status(201).json(repo.findAll());
	}

	public static void removeUser(Context context) {
		try {
			int id = Integer.parseInt(context.pathParam("id"));
			User user = repo.removeUserById(id);
			context.status(200).json(user);
		} catch (Exception e) {
			context.status(404);
		}
	}
}