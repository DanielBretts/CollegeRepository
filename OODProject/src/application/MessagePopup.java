package application;

import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import SQLConnection.SQLConnector;
import classes.Chat;
import classes.Message;
import classes.ProgManager;
//import javafx.application.Platform;

public class MessagePopup implements Runnable {

	private ProgManager progManager = ProgManager.getProgManager();
	
	private Chat chat;
	private ChatController cController;
	private boolean exit;

	public MessagePopup(Chat chat, ChatController controller) {
		this.chat = chat;
		this.cController = controller;
		this.exit = false;
	}

	@Override
	public void run() {
		while(!exit) {
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			Message recentMessage = getRecentMessage();
			if(recentMessage != null) {
				Message m = cController.getLastMsg();
				if(!(recentMessage.equals(m))) {		//m != null is for the first message in a chat
					//System.out.println(recentMessage.getTime() + "  " + m.getTime());
					cController.sendMessageToChat(recentMessage, progManager.loadUser(recentMessage.getUsername()));
				}
			}
		}
	}

	private Message getRecentMessage() {
		Message m = null;
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();

			String query = "SELECT * FROM messages WHERE cid = " + chat.getCID() + " ORDER BY time desc limit 1";
			Statement pst = conn.createStatement();
			ResultSet rs = pst.executeQuery(query);
			while(rs.next()) {
				Timestamp time = rs.getTimestamp("time");
				String date = (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(time.getTime());
				String msg = rs.getString("message");
				String username = rs.getString("username");
				m = new Message(chat.getCID(), date, username, msg);
			}
			conn.close();

		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
		}
		return m;
	}
	
    public void stop() {
        exit = true;
    }

}
