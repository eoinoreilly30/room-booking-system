package eoino;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Bookings")
public class Bookings extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DatabaseQuery query = new DatabaseQuery();
		ResultSet resultSet = null;
		String queryString = "SELECT * FROM EOINO_ROOMS";
		Vector<String> rooms = new Vector<String>();
		
		try {
			resultSet = query.makeQuery(queryString);
			while(resultSet.next()) {
				rooms.add(resultSet.getString("NAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String requestedDate = request.getParameter("date");

		queryString = "SELECT * FROM EOINO_BOOKINGS WHERE BOOKING_DATE='" + requestedDate + "'";
		HashMap<String, boolean[]> bookings = new HashMap<String, boolean[]>();
		try {
			resultSet = query.makeQuery(queryString);
			while(resultSet.next()) {
				String room = resultSet.getString("ROOM");
				int hour = (int) resultSet.getInt("BOOKING_HOUR");
				boolean[] row = bookings.get(room);
				if(row == null) {
					row = new boolean[23];
				}
				row[hour] = true;
				bookings.put(room, row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		int minHour = 9;
		int maxHour = 17;
		out.println("<table id=\"available-bookings-table\">");
		out.println("<tr><th>ROOM</th>");
		for(int i=minHour; i<=maxHour; i++) {
			out.println("<th>" + i + ":00</th>");
		}
		out.println("</tr>");

		for(String room : rooms) {
			out.println("<tr><td>" + room + "</td>");
			boolean[] row = bookings.get(room);
			if(row == null) {
				row = new boolean[23];
			}
			for(int j=minHour; j<=maxHour; j++) {
				if(row[j]) {
					out.println("<td>X</td>");
				}
				else {
					out.println("<td></td>");
				}
			}
			out.println("</tr>");
		}
		out.println("</table>");
		
    	query.closeConnections();
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			String queryString = "SELECT MAX(ID) FROM EOINO_BOOKINGS";
	        int bookingID = 1;
			try {
				ResultSet resultSet = query.makeQuery(queryString);
				while(resultSet.next()) {
					bookingID = resultSet.getInt("MAX(ID)") + 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			queryString = "INSERT INTO EOINO_BOOKINGS VALUES " +
	        		"(" + bookingID + ", '" + room + "', '" + date + "', '" + time + "', '" + UUID + "')";
	    	try {
				query.makeQuery(queryString);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	query.closeConnections();
		
			doGet(request, response);
		}
	}
}
