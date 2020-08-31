package eoino;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Authorise")
public class Authorise extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			response.setStatus(401);
		}
		else if(!isValidUUID(session.getAttribute("UUID").toString())) {
			response.setStatus(401);
		}
		else if(isAdminAccount(session.getAttribute("UUID").toString())){
			response.setHeader("Admin", "true");
			response.setStatus(200);
		}
		else {
			response.setStatus(200);
		}
	}
	
	protected static boolean isValidUUID(String UUID) {
		DatabaseQuery query = new DatabaseQuery();
        boolean validated = false;
        String queryString = "SELECT UUID FROM EOINO_USERS WHERE UUID='" + UUID + "'";
    	try {
			ResultSet resultSet = query.makeQuery(queryString);
			while(resultSet.next()) {
				if(resultSet.getString("UUID").equals(UUID)) {
					validated = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	query.closeConnections();
    	
    	return validated;
    }
	
	protected static boolean isAdminAccount(String UUID) {
		DatabaseQuery query = new DatabaseQuery();
        boolean isAdmin = false;
        String queryString = "SELECT IS_ADMIN FROM EOINO_USERS WHERE UUID='" + UUID + "'";
    	try {
			ResultSet resultSet = query.makeQuery(queryString);
			while(resultSet.next()) {
				if(resultSet.getString("IS_ADMIN").equalsIgnoreCase("TRUE")) {
					isAdmin = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	query.closeConnections();
    	
    	return isAdmin;
	}
}
