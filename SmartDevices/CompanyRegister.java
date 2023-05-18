package net.codejava.javaee;


import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import jakarta.servlet.RequestDispatcher;


/**
 * Servlet implementation class CompanyRegister
 */
public class CompanyRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String connectionName = "jdbc:mysql://localhost:3306/CompanyDataBase";
	private String userName = "Racheli";
	private String password = "423123";
	private Connection connName = null;
	private ISqlCrud sql = null;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CompanyRegister() {
        super();
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            connName = DriverManager.getConnection(connectionName,userName,password);
    		sql = new SqlHendler(connectionName, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/GateWayServer");
        dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JSONObject json = GenericIotUtils.CreateJsonObj(request);
      
		companyRegister(json, connName);

		request.setAttribute("jsonData", json.toString());
		
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/GateWayServer");
        dispatcher.forward(request, response);
        
    }
	
	public static int companyRegister(JSONObject json, Connection conn) {
		
		int rowsName = 0;

	    try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO Product (name, product_id, company_id) VALUES (?, ?, ?)")) {
	    	preparedStatement.setString(1, json.getString("name"));
	    	preparedStatement.setObject(2, json.getInt("product_id"));
	        preparedStatement.setObject(3, json.getInt("company_id"));
	        rowsName = preparedStatement.executeUpdate();   
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
		return rowsName;
		}


}
