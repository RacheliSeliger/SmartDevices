package net.codejava.javaee;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.json.JSONObject;

/**
 * Servlet implementation class IotUpdate
 */
public class IotUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IotUpdate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		sendRequestToGateWay(request, response);	

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		sendRequestToGateWay(request, response);	
		
    }
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		sendRequestToGateWay(request, response);	
		
	}
	
	
 
	void sendRequestToGateWay(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonMessage = GenericIotUtils.CreateJsonObj(request);
		request.setAttribute("jsonData", jsonMessage.toString());

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/GateWayServer");
		dispatcher.forward(request, response);
	}
}
