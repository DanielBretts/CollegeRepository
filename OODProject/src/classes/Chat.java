package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import SQLConnection.SQLConnector;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.paint.Color;

public class Chat {

	private User user;
	private User friend;
	private int cID;
	private List<Message> messages = new ArrayList<Message>();
	private List<User> userListInChat;

	
	public Chat() {
	}

	public void sendMessage(String msg) {
		if(!msg.isBlank())
			try {
				Connection conn = SQLConnector.setUpConnectionToServer();
				System.out.println("Successfully connected to database!");

				Timestamp time = new Timestamp(System.currentTimeMillis());
				String username = user.getUserName(), message = msg;
				int CID = this.cID;
				
				String query = "INSERT INTO messages (CID, username, time, message) values  (?,?,?,?)";
				PreparedStatement pst = conn.prepareStatement(query);
				pst.setInt(1, CID);
				pst.setString(2, username);
				pst.setTimestamp(3, time);
				pst.setString(4, message);

				int update = pst.executeUpdate();
				if (update != 0)
					System.out.println("Successfully updated the database!");
				else
					System.out.println("Update to database failed!");

				conn.close();
				System.out.println("Connection to database closed.\n");
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			} catch (SQLException ex) {
				if (ex instanceof SQLIntegrityConstraintViolationException)
					System.out.println("Citizen exists in database");
				else
					System.out.println(ex.getMessage());
			}
	}


	public int getCID() {
		return cID;
	}

	public void setCID(User user, User friend) {
		this.cID = user.getFriendCID(friend);
	}

	public void setCID(Game game) {
		this.cID = game.getCID(game);
	}

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserListInChat(List<User> game_Users) {
		userListInChat = game_Users;
	}
	
	public List<User> getUserListInChat() {
		return userListInChat;
	}
	
	public List<Message> loadChat() {
		
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");
			System.out.println(cID);
			String query = "SELECT * FROM messages WHERE cid = " + cID + " ORDER BY time ASC";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			while(rs.next())
			{
				Timestamp t = rs.getTimestamp("time");
				String date = (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(t.getTime());
				String username = rs.getString("username");
				String message = rs.getString("message");
				Message m = new Message(cID,date,username,message);
				messages.add(m);
			}
			conn.close();
			System.out.println("Connection to database closed.\n");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			if (ex instanceof SQLIntegrityConstraintViolationException)
				System.out.println("");
			else
				System.out.println(ex.getMessage());
		}
		return messages;
	}

}
