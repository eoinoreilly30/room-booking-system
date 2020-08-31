package eoino;

import java.sql.*;
import oracle.jdbc.driver.*;

public class JDBCexample {
		
    public static void main(String[] args)
    {
         Connection con = null;
         Statement stmt = null;
         ResultSet rs = null;
         String JDBCUrl = "jdbc:oracle:thin:@ee417.c7clh2c6565n.eu-west-1.rds.amazonaws.com:1521:EE417";
         String username = "ee_user";
         String password = "ee_pass";
         
         try {
             System.out.println("\nConnecting to the SSD Database......");
             Class.forName("oracle.jdbc.driver.OracleDriver");
             con = DriverManager.getConnection(JDBCUrl, username, password);
         }
         catch (Exception e) {
             System.out.println("\nAn error has occurred during the connection phase!  This is most likely due to your CLASSPATH being set wrong and the"
                     + " Oracle classes unable to be found.  Otherwise the database itself may be down.  Try telneting to port 1521 and see if it is up!");
             e.printStackTrace();
             System.exit(0);
         }   

 	 try {
	     System.out.println("\nConnection Successful..... creating statement....");
      	     stmt = con.createStatement();
	     rs = stmt.executeQuery("SELECT * FROM eoino_users");

	     while (rs.next())
                System.out.println("\nName=" + rs.getString("username") + " " + rs.getString("password"));
	 }
         catch (SQLException e) {
	     System.out.println("\nAn error has occurred during the Statement/ResultSet phase.  Please check the syntax and study the Exception details!");
             while (e != null) {
	         System.out.println(e.getMessage());
                 e = e.getNextException();
	     }
             System.exit(0);
         }

         finally {
	     try {    
	         if (rs != null) rs.close();
		 if (stmt != null) stmt.close();
		 if (con != null) con.close();
	     }
	     catch (Exception ex) {
	         System.out.println("An error occurred while closing down connection/statement"); 
             }
         }
    }
  
}