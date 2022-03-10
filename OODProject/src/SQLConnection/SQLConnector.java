package SQLConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnector {
	
	public static Connection setUpConnectionToServer() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String dbUrl = "jdbc:mysql://localhost:3306/discord_afeka";
		Connection conn = DriverManager.getConnection(dbUrl, "root", "123456");
		return conn;
	}
	
}
