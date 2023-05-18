package net.codejava.javaee;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bson.types.ObjectId;
import org.json.JSONObject;


import com.mongodb.MongoClientURI;
import com.mongodb.MongoClient;


/**
 * Servlet implementation class GateWayServer
 */
public class GateWayServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SingletonCommandFactory commandFactory  = SingletonCommandFactory.getFactory();
	private String connectionUpdate = "jdbc:mysql://localhost:3306/IOTInfo";
	private MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017/"));
	private mongoDBHendler mongo_DB =  new mongoDBHendler(mongoClient.getDatabase("end_user_db"));
	
	private String userName = "Racheli";
	private String password = "423123";
	
	private Connection connUpdate = null;

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GateWayServer() {
        super();
        commandFactory.addCommand("CR", this::createNewCompanyRegisterion);
        commandFactory.addCommand("PR", this::createNewProdactRegisterion);
        commandFactory.addCommand("IOTR", this::createNewIotDeviceRegisterion);
        commandFactory.addCommand("UR", this::updateIotDevice);

        commandFactory.addCommand("UP", this::updateIotDevice);
       
	       
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            connUpdate = DriverManager.getConnection(connectionUpdate,userName,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		GenericIotUtils.GetCommandFromJson(request, response);
				
		response.getWriter().println("\n INSERT request");  

	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		GenericIotUtils.GetCommandFromJson(request, response);
		
				
		response.getWriter().println(" \n Update request");  
	}
	
	


	public ObjectId createNewCompanyRegisterion(JSONObject json) {
		return mongo_DB.create(json, "Companies");

	}

	public ObjectId createNewProdactRegisterion(JSONObject json) {

		return mongo_DB.create(json, "Products");
	}

	public ObjectId createNewIotDeviceRegisterion(JSONObject json) {

		return mongo_DB.create(json, "IOTDevice");

	}

	public ObjectId updateIotDevice(JSONObject json) {

		return mongo_DB.update(json, "IOTDevice");

	}
	
}
