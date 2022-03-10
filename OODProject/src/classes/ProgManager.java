package classes;

//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;

import SQLConnection.SQLConnector;

//import java.util.Observable;
//import java.util.Vector;
//
//import javax.imageio.ImageIO;
//import javax.imageio.stream.FileImageOutputStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.scene.image.Image;

public class ProgManager {

	
	private int currentYear = LocalDate.now().getYear();
	private static ProgManager _instance = null;	
//	private static List<Game> games = null;				//to hold the favorite games

	private ProgManager() {
//		games = new ArrayList<Game>();
	}


	public static ProgManager getProgManager() {
		if(_instance == null)
			_instance = new ProgManager();
		return _instance;
	}


	// LOGIN SCREEN //
	public boolean checkUserLogin(String username) {
		List<User> users = loadUsers();
		if(users.contains(loadUser(username)))
			return true;
		return false;			
	}

	// REGISTER SCREEN //


	// FUNCTIONS OF PROG //

	public void addNewUserToDB(String newUsername, String newPassword, int birthYear) {
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String username = newUsername, password = newPassword;

			String query = "INSERT INTO users (username, password,age) values  (?,?,?)";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, username);
			pst.setString(2, password);
			pst.setInt(3, currentYear - birthYear);

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

	public ObservableList<User> loadUsers() {
		ObservableList<User> users = FXCollections.observableArrayList();//FXCollections.observableArrayList();
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String username , password;
			int age;
			String query = "SELECT * FROM users";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			while(rs.next())
			{
				username = rs.getString("username");
				password = rs.getString("password");
				age = rs.getInt("age");
				//File image = new File(username + " profile img.png");
				//Resize The ImageIcon
				//FileOutputStream output = new FileOutputStream(image);
				User user = new User(username,password,null, age);
				//				InputStream input = rs.getBinaryStream("image");
				//				Image image = new Image(input);
				//				Blob b = rs.getBlob("image");
				//				if(b != null) {
				//					File f = createImageFile(username,b);
				//					System.out.println(f.getAbsolutePath());
				//					Image imgProfile = new Image(f.getAbsolutePath());
				//					user.setImage(imgProfile);
				//				}
				users.add(user);
			}
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
		return users;
	}


	public ObservableList<User> loadFriends(User user) {
		ObservableList<User> friends = FXCollections.observableArrayList();
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String username = user.getUserName();
			String query = "SELECT username FROM friends_chats WHERE username <> '" + username +
					"' AND CID IN (SELECT CID FROM friends_chats WHERE username = '" + username + "')";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			while(rs.next())
			{
				String friendUsername = rs.getString("username");
				User friend = loadUser(friendUsername);
				if(!friendUsername.equals(username)) {
					friends.add(friend);
				}
			}
			conn.close();
			System.out.println("Connection to database closed.\n");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			if (ex instanceof SQLIntegrityConstraintViolationException)
				System.out.println("Users already friends!");
			else
				System.out.println(ex.getMessage());
		}
		return friends;
	}


//	private File createImageFile(String username, Blob blob) {
//		byte b[];
//		File f=new File("C:\\Users\\danie\\eclipse-workspace\\OODProject\\src\\Images\\profilePic"+username);
//		FileImageOutputStream fs;
//		try {
//			fs = new FileImageOutputStream(f);
//			Blob bl=blob;
//			b=blob.getBytes(1, (int)blob.length());
//			fs.write(b);
//			fs.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return f;
//	}

//	private boolean loadGames() {
//		try {
//			Connection conn = SQLConnector.setUpConnectionToServer();
//			System.out.println("Successfully connected to database!");
//
//			String gameNames;
//			
//
//			String query = "SELECT * FROM games";
//			PreparedStatement pst = conn.prepareStatement(query);
//			ResultSet rs = pst.executeQuery(query);
//			while(rs.next())
//			{
//				gameNames = rs.getString("name");
//				
//				Game g = new Game(gameNames);
//				games.add(g);
//			}
//			conn.close();
//			System.out.println("Connection to database closed.\n");
//		} catch (ClassNotFoundException ex) {
//			ex.printStackTrace();
//		} catch (SQLException ex) {
//			if (ex instanceof SQLIntegrityConstraintViolationException)
//				System.out.println("Citizen exists in database");
//			else
//				System.out.println(ex.getMessage());
//		}
//		return true;
//	}

	public ObservableList<String> loadGamesNamesFromDB() {
		ObservableList<String> gameNameList = FXCollections.observableArrayList();
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			
			//File image;
			
			String query = "SELECT name FROM games";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			while(rs.next())
			{
				String gameName = rs.getString("name");
				//image = (File) rs.getBlob("image");
				gameNameList.add(gameName);
			}
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
		return gameNameList;
	}

	public User loadUser(String name) {
		User user = null;
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String username , password;
			int age;
			
			String query = "SELECT * FROM users WHERE username = '" + name + "'";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			while(rs.next()) {
				username = rs.getString("username");
				password = rs.getString("password");
				age = rs.getInt("age");
				//File image = new File(username + " profile img.png");
				//Resize The ImageIcon
				//FileOutputStream output = new FileOutputStream(image);
				user = new User(username,password,null, age);
				//				InputStream input = rs.getBinaryStream("image");
				//				Image image = new Image(input);
				//				Blob b = rs.getBlob("image");
				//				if(b != null) {
				//					File f = createImageFile(username,b);
				//					System.out.println(f.getAbsolutePath());
				//					Image imgProfile = new Image(f.getAbsolutePath());
				//					user.setImage(imgProfile);
				//				}
			}
			conn.close();
			System.out.println("Connection to database closed.\n");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.getMessage();
		}
		return user;
	}

	public void addUserFavGamesToDB(String newUsername, List<String> gameList) {
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String username = newUsername;

			for(String gameName : gameList) {
				String query = "INSERT INTO user_games (username, gamename) VALUES  (?,?)";
				PreparedStatement pst = conn.prepareStatement(query);
				pst.setString(1, username);
				pst.setString(2, gameName);

				int update = pst.executeUpdate();
				if (update != 0)
					System.out.println("Successfully updated the database!\n");
				else
					System.out.println("Update to database failed!\n" + gameName + " not added to " + username + " game list!\n");
			}
			conn.close();
			System.out.println("Connection to database closed.\n");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			if (ex instanceof SQLIntegrityConstraintViolationException)
				System.out.println("User already has that game!");
			else
				System.out.println(ex.getMessage());
		}
	}
	
	public Game loadGame(String gameName) {

		Game game = null;
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String gamename;
			
			String query = "SELECT * FROM games WHERE name = \"" + gameName + "\"";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			while(rs.next()) {
				gamename = rs.getString("name");
				game = new Game(gamename);
			}
			conn.close();
			System.out.println(gameName);
			System.out.println("Connection to database closed.\n");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
				System.out.println(ex.getMessage());
		}
		return game;
	}

	public ObservableList<User> getGameUsers(String gamename) {
		
		ObservableList<User> gameUsers = FXCollections.observableArrayList();
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			String query = "SELECT username FROM user_games WHERE gamename = \"" + gamename + "\"";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			while(rs.next()) {
				User user = loadUser(rs.getString("username"));
				gameUsers.add(user);
			}
			conn.close();
			System.out.println("Connection to database closed.\n");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
				ex.getMessage();
		}
		return gameUsers;
	}

	public boolean removeChatFromDB(int cid) {
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			String query = "DELETE FROM chats WHERE cid =" + cid;
			PreparedStatement pst = conn.prepareStatement(query);
			int update = pst.executeUpdate();
			if (update != 0)
				System.out.println("Successfully deleted the chat from the database!");
			else
				System.out.println("Update to database failed!");
			conn.close();
			System.out.println("Connection to database closed.\n");
			if(update != 0)
				return true;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return false;
	}

	public boolean removeFavGameFromDB(User user, String gameName) {
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			String username = user.getUserName();
			
			String query = "DELETE FROM user_games WHERE username = ? AND gamename = ? ";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, username);
			pst.setString(2, gameName);
			int update = pst.executeUpdate();
			if (update != 0)
				System.out.println("Successfully deleted the chat from the database!");
			else
				System.out.println("Could not remove\find the chat.");
			conn.close();
			System.out.println("Connection to database closed.\n");
			if(update != 0)
				return true;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.getMessage();
		}
		return false;
	}




}
