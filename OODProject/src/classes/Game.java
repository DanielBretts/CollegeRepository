package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import SQLConnection.SQLConnector;

public class Game {

	private String gameName;
	private int cID;

	public Game(String gameName) {
		this.gameName = gameName;
		setCID(gameName);
	}

	private void setCID(String gameName) {
		int cID = -1;
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String query = "SELECT CID FROM games_chats WHERE gamename = \"" + gameName + "\"";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			while(rs.next())
				cID = rs.getInt("CID");
			conn.close();
			System.out.println("Connection to database closed.\n");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			if (ex instanceof SQLIntegrityConstraintViolationException)
				System.out.println("citi");
			else
				System.out.println(ex.getMessage());
		}
		this.cID = cID;
	}

	public String getName() {
		return gameName;
	}

	public int getCID(Game game) {
		return cID;
	}
	
	
}
