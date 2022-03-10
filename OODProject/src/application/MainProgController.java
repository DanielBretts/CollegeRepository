package application;

import java.io.IOException;

import java.util.List;

import classes.User;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class MainProgController {

	private User user;

	@FXML
	private Button bChats;

	@FXML
	private Button bLogOut;

	@FXML
	private MenuButton bPlay;

	@FXML
	private Button bProfile;

	@FXML
	private Button bSearch;

	private List<String> games;

	public void setGames(List<String> games) {
		this.games = games;
	}

	public User getUser() {
		return user;
	}

	@FXML
	void openGamesMenu(Event event) {
		bPlay.getItems().clear();
		games = user.loadFavoriteGamesFromDB();
		for(String gamename : games) {
			MenuItem m = new MenuItem(gamename);
			bPlay.getItems().add(m);
		}
	}

	@FXML
	void openChatMenu(ActionEvent event) {
		try {
			String fxmlName = "ChatMenu.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
			Parent p = loader.load();
			ChatMenuController cont = loader.getController();
			cont.setUser(user);
			int height = 400, width = 400;
			WindowFactory.newWindowOpenner(event, width, height, getClass(), p);
			WindowFactory.app_stage.setTitle("Welcome " + user.getUserName() + "!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openSearchMenu(ActionEvent event) {
		try {
			String fxmlName = "Search.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
			Parent p = loader.load();
			SearchController cont = loader.getController();
			cont.setUser(user);
			int height = 350, width = 680;
			WindowFactory.newWindowOpenner(event, width, height, getClass(), p);
			WindowFactory.app_stage.setTitle("Search Menu");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openProfileMenu(ActionEvent event) {
		try {
			String fxmlName = "Profile.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
			Parent p = loader.load();
			ProfileController cont = loader.getController();
			cont.setUser(user);
			//cont.setImage();
			int height = 400, width = 630;
			WindowFactory.newWindowOpenner(event, width, height, getClass(), p);
			WindowFactory.app_stage.setTitle("Welcome " + user.getUserName() + "!");
			cont.Initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openLoginMenu(ActionEvent event) {
		String fxmlName = "LogIn.fxml";
		int height = 500, width = 800;
		WindowFactory.openWindow(event,fxmlName, width, height,getClass());
		WindowFactory.app_stage.setTitle("Discord Afeka");
	}

	public void setUser(User user) {
		this.user = user; 
	}


}
