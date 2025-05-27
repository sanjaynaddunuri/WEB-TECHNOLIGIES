import java.sql.*;
public class ConnectionDemo {public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		 DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521","SYSTEM","admin");
		
		System.out.println("Connection Established successfully");
		}
		catch(Exception e)
		{
			System.err.println(e);
		}

	}


	
}
