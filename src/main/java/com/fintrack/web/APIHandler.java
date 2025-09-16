package com.fintrack.web;

import io.javalin.http.Context;
import java.util.*;
import java.sql.Connection;

import com.fintrack.entity.*;
import com.fintrack.db.DatabaseManager;
import com.fintrack.repository.*;

public class APIHandler {
	public static void getHealth(Context context) {
		context.json(Map.of("status", "UP"));
	}
}