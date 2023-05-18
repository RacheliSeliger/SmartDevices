package net.codejava.javaee;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public interface ISqlCrud {

	public int create(String query, Object... params) throws SQLException;

	public Object read(String query, Object... params) throws SQLException;

	public JSONObject update(String query, Object... params) throws SQLException;

	public JSONObject delete(String query, Object... params) throws SQLException;

}