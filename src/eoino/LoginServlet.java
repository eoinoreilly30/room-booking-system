package eoino;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		String UUID = validateUser(username, password);
	    
		if (UUID == null) {
			response.sendRedirect("loginFailed.html");
		} else {
			session.setAttribute("UUID", UUID);
			response.sendRedirect("accountHome.html");
		}
	}
	
    private String validateUser(String username, String password) {
    	DatabaseQuery query = new DatabaseQuery();
    	String UUID = null;
    	
    	try {
			String queryString = "SELECT * FROM EOINO_USERS";
			ResultSet rs = query.makeQuery(queryString);
			while(rs.next()) {
				if(rs.getString("USERNAME").equals(username) && rs.getString("PASSWORD").equals(password)) {
					UUID = rs.getString("UUID");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	query.closeConnections();
    	
    	return UUID;
    }
}