package eoino;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Signup")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		
		String newUUID = UUID.randomUUID().toString();
	    
		DatabaseQuery query = new DatabaseQuery();
        String queryString = "INSERT INTO EOINO_USERS VALUES ('" + newUUID + "', '" + username + "', '"
        		+ password + "', '" + email + "', 'FALSE')";
    	try {
			query.makeQuery(queryString);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("signup.html");
			return;
		}
    	
    	query.closeConnections();
    	
    	session.setAttribute("UUID", newUUID);
    	response.sendRedirect("accountHome.html");
	}

}
