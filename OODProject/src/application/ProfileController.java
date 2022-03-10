package application;

import java.io.File;
//import java.io.FileFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import SQLConnection.SQLConnector;
import classes.FriendContext;
import classes.GameContext;
import classes.OperationAddGame;
import classes.OperationRemoveFriend;
import classes.OperationRemoveGame;
import classes.ProgManager;
import classes.User;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;


public class ProfileController {

	private User user;

	private ProgManager progManager = ProgManager.getProgManager();

	private FriendContext context;

	private GameContext gContext;

	@FXML
	private Label lProfile;

	@FXML
	private Label lFriends;

	@FXML
	private Label lFavoriteGames;

	@FXML
	private ImageView ivProfile;

	@FXML
	private Label lReturn;

	@FXML
	private Label lStatus;


	@FXML
	private AnchorPane apProfile;

	@FXML
	private ScrollPane spUserDetails;

	@FXML
	private VBox vbProfileDetailsArea;

	@FXML
	private Text detailsArea;


	@FXML
	private AnchorPane apUserGames;

	@FXML
	private ImageView addGame;

	@FXML
	private ListView<String> lvGames;


	@FXML
	private AnchorPane apSearchGame;

	@FXML
	private ImageView bSearch;

	@FXML
	private TextField tfSearch;

	@FXML
	private Button bAddToFavorites;

	@FXML
	private ListView<String> lvSearchOutput;


	@FXML
	private AnchorPane apFriends;

	@FXML
	private ListView<String> lvFriends;

	@FXML
	private ImageView imgRemoveFriend;


	void Initialize() {
		//ivProfile.setImage(user.getImage());
		spUserDetails.setContent(detailsArea);
		context = new FriendContext(new OperationRemoveFriend());
	}

	public void setUser(User user) {
		this.user = user;
	}

//	private void setImage() {
//		Image img = new Image(user.getImage().getAbsolutePath());
//		this.img.setImage(img);
//	}

	@FXML
	private void chooseImageButton(MouseEvent event) {
		FileChooser fileChooser = new FileChooser();
		// Set extension filter
		FileChooser.ExtensionFilter extFilter = 
				new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(ivProfile.getScene().getWindow());
		String location =   (file.getAbsoluteFile().toURI().toString());
		Image imgProfile = new Image(location);
		ivProfile.setImage(imgProfile);
		user.updateImageToDB(location);
	}

	@FXML
	private void showProfile(MouseEvent event) {
//		try {
			apFriends.setVisible(false);
			apSearchGame.setVisible(false);
			apUserGames.setVisible(false);
			apProfile.setVisible(true);
			detailsArea.setText(null);
			String name = user.getUserName();
			int age = user.getAge();
			StringBuffer sb = new StringBuffer();
			sb.append("name:\t" + name + "\nage:\t" + age);
			detailsArea.setText(sb.toString());
			
//			Connection conn = SQLConnector.setUpConnectionToServer();
//			PreparedStatement pst;
//			String query = "SELECT * FROM users WHERE username ='" + user.getUserName() + "'";
//			pst = conn.prepareStatement(query);
//			ResultSet rs = pst.executeQuery(query);
//			while(rs.next()) {
//				String name = rs.getString("username");
//				int age = rs.getInt("age");
//			}
//			conn.close();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}

	@FXML
	private void showFriends(Event event) {
		apSearchGame.setVisible(false);
		apProfile.setVisible(false);
		apUserGames.setVisible(false);
		apFriends.setVisible(true);
		lStatus.setVisible(false);
		detailsArea.setText(null);
		ObservableList<String> friends = (ObservableList<String>) user.getFriendsString();
		lvFriends.setItems(friends);
	}

	@FXML
	private void showFavGames(MouseEvent event) {
		apProfile.setVisible(false);
		apFriends.setVisible(false);
		apSearchGame.setVisible(false);
		apUserGames.setVisible(true);
		lvGames.setItems((ObservableList<String>) user.loadFavoriteGamesFromDB());
	}

	@FXML
	private void removeFriend(Event event) {
		MultipleSelectionModel<String> selectedModel = lvFriends.getSelectionModel();
		int index = selectedModel.getSelectedIndex();
		if(index == -1)
			return;
		String friendSelected = selectedModel.getSelectedItem().toString();
		User friend = progManager.loadUser(friendSelected);
		lStatus.setVisible(true);
		lStatus.setText(null);
		if(context.executeStrategy(user, friend)) {
			lStatus.setText("Friend removed!");
			user.getFriends().remove(friend);
		} else {
			lStatus.setText("Friend already removed.");
			lStatus.setTextFill(Color.INDIANRED);
		}
		ObservableList<String> friends = (ObservableList<String>) user.getFriendsString();
		lvFriends.setItems(friends);
	}

	@FXML
	private void openAddNewGame(MouseEvent event) {
		apUserGames.setVisible(false);
		apSearchGame.setVisible(true);
		lvSearchOutput.getItems().clear();
		tfSearch.clear();
	}

	@FXML
	private void updateSearchOutput(Event event) {
		String selected = tfSearch.getText();
		if(selected != null) {
			lvSearchOutput.getItems().clear();
			try {
				Connection conn = SQLConnector.setUpConnectionToServer();
				System.out.println("Successfully connected to database!");
				String query = "SELECT name FROM games WHERE name LIKE '%" + tfSearch.getText() + "%' ORDER BY name";
				PreparedStatement pst;
				//if(cbGameNames.getSelectionModel().getSelectedIndex() != -1)
				pst = conn.prepareStatement(query);
				ResultSet rs = pst.executeQuery(query);
				while(rs.next()) {
					String gamename = rs.getString("name");
					lvSearchOutput.getItems().add(gamename);
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
		}
	}

	@FXML
	private void addGameToFavorites(Event event) {
		if(lvSearchOutput.getSelectionModel().getSelectedIndex() == -1)
			return;
		String gameName = lvSearchOutput.getSelectionModel().getSelectedItem();
		gContext = new GameContext(new OperationAddGame());
		gContext.executeStrategy(user, gameName);
		updateSearchOutput(event);
	}

	@FXML
	private void removeGameFromFavorites() {
		if(lvGames.getSelectionModel().getSelectedIndex() == -1)
			return;
		String gameName = lvGames.getSelectionModel().getSelectedItem();
		gContext = new GameContext(new OperationRemoveGame());
		gContext.executeStrategy(user, gameName);
		lvGames.setItems((ObservableList<String>) user.loadFavoriteGamesFromDB());
	}

	@FXML
	private void openMainProg(Event event) {
		try {
			String fxmlName = "mProg.fxml";
			int height = 110, width = 620;
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
			Parent root = loader.load();
			MainProgController mpCont = loader.getController();
			mpCont.setUser(user);
			WindowFactory.newWindowOpenner(event, width, height,getClass(), root);
			WindowFactory.app_stage.setTitle("Welcome " + user.getUserName() + "!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void keyPressed(KeyEvent event) {
		switch(event.getCode()) {
		case ENTER:
			if(apSearchGame.isVisible())
				updateSearchOutput(event);
			break;
		case ESCAPE:
			openMainProg(event);
			break;
		default:
			break;
		}
	}

}
