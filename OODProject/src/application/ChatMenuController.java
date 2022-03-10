package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import classes.Chat;
import classes.Game;
import classes.ProgManager;
import classes.User;
import enums.SelectedChats;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ChatMenuController {

	private ProgManager progManager = ProgManager.getProgManager();
	
	private User user;
	private SelectedChats currentList;

	@FXML
	private AnchorPane apChatMenu;

	@FXML
	private Button bFriendChats;

	@FXML
	private Button bGameChats;

	@FXML
	private Button bGroupChats;

	@FXML
	private Button bOpenChat;

	@FXML
	private Button bReturn;

	@FXML
	private ListView<String> lvChats;

	@FXML
	void showFriendChats(MouseEvent event) {
		lvChats.setItems(null);
		try {
			lvChats.setItems((ObservableList<String>)user.getFriendsFromDB());
			currentList = SelectedChats.FRIENDS;
		}catch (NullPointerException npe) {

		}
	}

	@FXML
	public void showGameChats(MouseEvent event) {
		lvChats.setItems(null);
		try {
			lvChats.setItems((ObservableList<String>)user.loadFavoriteGamesFromDB());
			currentList = SelectedChats.GAMES;
		}catch (NullPointerException npe) {

		}
	}

	@FXML
	public void openMainProg(Event event) {
		try {
			String fxmlName = "mProg.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
			Parent root = loader.load();
			MainProgController mpCont = loader.getController();
			mpCont.setUser(user);
			int height = 110, width = 620;
			WindowFactory.newWindowOpenner(event, width, height,getClass(), root);
			WindowFactory.app_stage.setTitle("Welcome " + user.getUserName() + "!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setUser(User user) {
		this.user = user;
	}

	@FXML
	public void openChat(Event event) {
		String fxmlName;
		if(lvChats.getSelectionModel().getSelectedIndex() == -1)
			return;
		String selectedChat = lvChats.getSelectionModel().getSelectedItem().toString();
		int height = 600, width = 700;

		switch(currentList) {
		case FRIENDS:
			try {
				fxmlName = "Chat.fxml";
				FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
				Parent p = loader.load();
				ChatController cont = loader.getController();
				Chat newChat = cont.getChat();
				newChat.setUser(user);
				
				User friend = progManager.loadUser(selectedChat);
				List<User> users = new ArrayList<User>();
				newChat.setFriend(friend);
				newChat.setUserListInChat(users);
				newChat.setCID(user, friend);
				cont.loadChat();
				WindowFactory.newWindowOpenner(event, width, height, getClass(), p);
				WindowFactory.app_stage.setTitle("Chatting with " + friend.getUserName());
				
				user.setChatColor();
				cont.setUserColor(user.getChatColor());
				friend.setChatColor();
				cont.setFriendColor(friend.getChatColor());
				
				cont.setThread();
			

			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case GAMES:
			try {
				fxmlName = "Chat.fxml";
				FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
				Parent p = loader.load();
				ChatController cont = loader.getController();
				Chat newChat = cont.getChat();
				newChat.setUser(user);
				Game game = progManager.loadGame(selectedChat);
				
				newChat.setCID(game);
				List<User> gameUsers = progManager.getGameUsers(game.getName());
				newChat.setUserListInChat(gameUsers);
				newChat.setFriend(null);
				cont.loadChat();
				WindowFactory.newWindowOpenner(event, width, height, getClass(), p);
				WindowFactory.app_stage.setTitle(game.getName() + " game chat");
	
				user.setChatColor();
				cont.setUserColor(user.getChatColor());
				
				List<Color> usersColors = new ArrayList<Color>();
				for(User u : gameUsers) {
					u.setChatColor();
					usersColors.add(u.getChatColor());
				}
				cont.setUsersColors(usersColors);
				
				cont.setThread();
				

				
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	@FXML
	void keyPressed(KeyEvent event) {
		System.out.println("Key pressed:");
		switch(event.getCode()) {
		case ENTER:
			System.out.println("Enter");
			openChat(event);
			break;
		case ESCAPE:

			openMainProg(event);
			break;
		default:
			break;
		}
	}

}
