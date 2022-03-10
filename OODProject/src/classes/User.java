package classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import SQLConnection.SQLConnector;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class User {

	private String userName;
	private String password;
	private int age;
	private int currentYear = LocalDate.now().getYear();
	private Image image;
	private Color chatColor;
	private List<User> friends;

	public User(String userName, String password, Image image, int birthYear) {
		setUserName(userName);
		setPassword(password);
		setAge(birthYear);
		//setImage(image);
	}

	private void setAge(int birthYear) {
		this.age = currentYear - birthYear;	
	}

	public int getAge() {
		return age;
	}

	public boolean setImage(Image image) {
		this.image = image;
		return true;
	}
	
	public Image getImage() {
		return image;
	}
	
	public boolean setUserName(String userName) {
		this.userName = userName;
		return true;
	}

	public String getUserName() {
		return userName;
	}
	
	public boolean setPassword(String password) {
		this.password = password;
		return true;
	}

	public String getPassword() {
		return password;
	}

	public void setChatColor() {
		if(chatColor == null) {
			double rangeMin = 0, rangeMax = 0.7;
			Random rand = new Random();
			double r = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
			double g = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
			double b = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
			this.chatColor =  new Color(r,g,b, 1);
		}
	}
	
	public Color getChatColor() {
		return chatColor;
	}
	
	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
	
	public List<User> getFriends() {
		return friends;
	}
	
	public List<String> getFriendsString() {
		List<String> friendsString = new ArrayList<String>();
		for(User user : friends)
			friendsString.add(user.getUserName());
		return friendsString;
	}
	
	public void updateNewImage(Image image) {
//		this.image = image;
		//updateImageToDB();
	}
	
	public void updateImageToDB(String location) {
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");
			File f = new File(location, this.getUserName());
			FileInputStream in = new FileInputStream(f);
			String query = "UPDATE users SET image =  ?  WHERE username = '" + this.userName + "'";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setBinaryStream(1, in, (int) f.length());
			int update = stmt.executeUpdate();
			if (update != 0)
				System.out.println("Successfully updated the image!!!!!!!!");
			else
				System.out.println("Update to image failed!");
			
			conn.close();
			System.out.println("Connection to database closed.\n");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addNewFriendToDB(User friend) {
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String query = "INSERT INTO chats values()";
			PreparedStatement pst = conn.prepareStatement(query);
			int update = pst.executeUpdate();
			if (update != 0)
				System.out.println("Successfully updated the database!");
			else
				System.out.println("Update to database failed!");

			query = "SELECT CID FROM chats ORDER BY CID DESC LIMIT 1";
			pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				int newCID = rs.getInt("CID");
				System.out.println(newCID);

				String friendUsername = friend.getUserName();

				query = "INSERT INTO friends_chats (cid,username) values  (?,?)";
				pst = conn.prepareStatement(query);
				pst.setInt(1, newCID);
				pst.setString(2, userName);

				update = pst.executeUpdate();				//For this user
				if (update != 0)
					System.out.println("Successfully updated the database!");
				else
					System.out.println("Update to database failed!");

				pst.setString(2, friendUsername);

				update = pst.executeUpdate();				//For friend
				if (update != 0)
					System.out.println("Successfully updated the database!");
				else
					System.out.println("Update to database failed!");
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
		friends.add(friend);
	}

	public int getFriendCID(User friend) {
		int CID = -1;
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String username = this.userName, friendName = friend.getUserName();

			String query = "SELECT CID FROM friends_chats WHERE username = '" + username +"' AND cid in "
					+ "	(SELECT CID FROM friends_chats WHERE username = '" + friendName +"')";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			while(rs.next())
				CID = rs.getInt("CID");
			conn.close();
			System.out.println("Connection to database closed.\n");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return CID;
	}

	public boolean addFavoriteGameToDB(String game) {
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String query = "INSERT INTO user_games values(?,?)";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, this.getUserName());
			pst.setString(2, game);
			int update = pst.executeUpdate();

			update = pst.executeUpdate();
			if (update != 0)
				System.out.println("Successfully updated the database!");
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

	public List<String> loadFavoriteGamesFromDB() {
		List<String> favGames = FXCollections.observableArrayList();
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String query = "SELECT gamename FROM user_games WHERE username = '" + this.userName + "'";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			while(rs.next()){
				favGames.add(rs.getString("gamename"));
			}
			conn.close();
			System.out.println("Connection to database closed.\n");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return favGames;
	}

	public List<String> getFriendsFromDB() {
		List<String> friends = FXCollections.observableArrayList();
		try {
			Connection conn = SQLConnector.setUpConnectionToServer();
			System.out.println("Successfully connected to database!");

			String username = getUserName();
			String query = "SELECT username FROM friends_chats WHERE username <> '" + username +
					"' AND CID IN (SELECT CID FROM friends_chats WHERE username = '" + username + "')";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			while(rs.next())
			{
				String friendUsername = rs.getString("username");
				friends.add(friendUsername);
			}
			conn.close();
			System.out.println("Connection to database closed.\n");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return friends;
	}

}
