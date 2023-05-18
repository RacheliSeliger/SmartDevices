package net.codejava.javaee;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;



public class SqlHendler implements ISqlCrud {


	private static Connection connection = null;
	
	public SqlHendler(String url, String userName, String password) throws SQLException {
		
		DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
		connection = DriverManager.getConnection(url, userName, password);
	}

	
	@Override
	public int create(String query, Object... params) throws SQLException {
		
		return executeUpdate(query, params);
	}

	@Override
	public ResultSet read(String query, Object... params) throws SQLException {
		
		return executeQuery(query, params);
	}

	@Override
	public JSONObject update(String query, Object... params) throws SQLException {
		
		executeUpdate(query, params);
		
		return null;
	}

	@Override
	public JSONObject delete(String query, Object... params) throws SQLException {
		
		executeUpdate(query, params);
		
		return null;
	}

	public int executeUpdate(String query, Object... params) throws SQLException {

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			return statement.executeUpdate();
		}
	}

	public ResultSet executeQuery(String query, Object... params) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(query);
		for (int i = 0; i < params.length; i++) {
			statement.setObject(i + 1, params[i]);
		}
		return statement.executeQuery();
	}
	

}