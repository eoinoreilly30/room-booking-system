package eoino;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Rooms")
public class Rooms extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			response.setStatus(401);
		}
		else if(!Authorise.isValidUUID(session.getAttribute("UUID").toString())) {
			response.setStatus(401);
		}
		else {
			String UUID = session.getAttribute("UUID").toString();
			DatabaseQuery query = new DatabaseQuery();
			String queryString = "SELECT ROOM, BOOKING_HOUR, BOOKING_DATE FROM EOINO_BOOKINGS WHERE BOOKING_OWNER='" + UUID + "'";
	        
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<table id=\"rooms-table\">");
			out.println("<tr><th>ROOM</th><th>DATE</th><th>TIME</th><th></th></tr>");
			
			try {
				ResultSet resultSet = query.makeQuery(queryString);
				while(resultSet.next()) {
					String room = resultSet.getString("ROOM");
					String date = resultSet.getString("BOOKING_DATE");
					String time = resultSet.getString("BOOKING_HOUR") + ":00";
					out.println("<tr><td>" + room + "</td><td>" + date + "</td><td>" + time + "</td><td><button class=\"cancelButton\">Cancel</button></td></tr>");
				}
				out.println("</table>");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			out.close();			
			query.closeConnections();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		if(session == null) {
			response.setStatus(401);
		}
		else if(!Authorise.isValidUUID(session.getAttribute("UUID").toString())) {
			response.setStatus(401);
		}
		else if (!Authorise.isAdminAccount(session.getAttribute("UUID").toString())) {
			response.setStatus(401);
		}
		else {
			String name = request.getParameter("name");
			String location = request.getParameter("location");
			int capacity = Integer.parseInt(request.getParameter("capacity"));
			
			DatabaseQuery query = new DatabaseQuery();
			String queryString = "INSERT INTO EOINO_ROOMS VALUES ('" + name + "', '" + location + "', " + capacity + ")";
			
			try {
				query.makeQuery(queryString);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	query.closeConnections();
	    	response.sendRedirect("accountHome.html");
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			response.setStatus(401);
		}
		else if(!Authorise.isValidUUID(session.getAttribute("UUID").toString())) {
			response.setStatus(401);
		}
		else {
			String room = request.getParameter("room");
			String date = request.getParameter("date");
			Integer time = Integer.parseInt(request.getParameter("time").split(":")[0]);
			String UUID = session.getAttribute("UUID").toString();
			
			DatabaseQuery query = new DatabaseQuery();
			String queryString = "DELETE FROM EOINO_BOOKINGS WHERE BOOKING_OWNER='" + UUID + "' AND ROOM='" + room + 
						"' AND BOOKING_HOUR='" + time + "' AND BOOKING_DATE='" + date + "'";
			
			try {
				query.makeQuery(queryString);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	query.closeConnections();
		}
	}
}
