package application;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import SQLConnection.SQLConnector;
import classes.FriendContext;
import classes.OperationAddFriend;
import classes.ProgManager;
import classes.User;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;


public class SearchController {

	private User user;

	private ProgManager progManager = ProgManager.getProgManager();

	private FriendContext context;
	
	@FXML
	private Label lStatusAdd;
	@FXML
	private ImageView bAddFriend;

	@FXML
	private Label bReturn;

	@FXML
	private ImageView bSearch;

	@FXML
	private ListView<String> lvSearchOutput;

	@FXML
	private ComboBox<String> searchBy;

	@FXML
	private TextField tfSearch;

	@FXML
	private ComboBox<String> cbGameNames;

	@FXML
	void initialize() {
		cbGameNames.getItems().clear();
		searchBy.getItems().addAll("Names","Games");
		addGamesToComboBox(cbGameNames);
		context = new FriendContext(new OperationAddFriend());
	}

	private void addGamesToComboBox(ComboBox<String> cbGameNames) {
		if(cbGameNames.getItems().isEmpty() && cbGameNames.getItems().isEmpty())
			cbGameNames.setItems((ObservableList<String>) progManager.loadGamesNamesFromDB());		
	}

	@FXML
	void updateSearchOutput(Event event) {
		lStatusAdd.setVisible(false);
		String selected = searchBy.getSelectionModel().getSelectedItem();
		if(selected != null) {
			lvSearchOutput.getItems().clear();
			try {
				Connection conn = SQLConnector.setUpConnectionToServer();
				System.out.println("Successfully connected to database!");
				String query = "SELECT username FROM users ORDER BY username";
				PreparedStatement pst;
				if(selected.equals("Names")) {
					tfSearch.setVisible(true);
					cbGameNames.setVisible(false);
					query = "SELECT username FROM users WHERE username like '%" + tfSearch.getText().toString() + "%' ORDER BY username";
				}else{
					tfSearch.setVisible(false);
					cbGameNames.setVisible(true);
					if(cbGameNames.getSelectionModel().getSelectedIndex() != -1)
						query = "SELECT username FROM user_games WHERE gamename = '" + 
								cbGameNames.getSelectionModel().getSelectedItem().toString() + "' order by username";
				}	
				pst = conn.prepareStatement(query);
				ResultSet rs = pst.executeQuery(query);
				while(rs.next()) {
					String username = rs.getString("username");
					if(!username.equals(user.getUserName()) && !user.getFriendsFromDB().contains(username))
						if(!lvSearchOutput.getItems().contains(username))
							lvSearchOutput.getItems().add(username);
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

	public void setUser(User user) {
		this.user = user;
	}

	@FXML
	public void openMainProg(Event event) {
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
	public void addFriend() {
		int index = lvSearchOutput.getSelectionModel().getSelectedIndex();
		if(index == -1)
			return;
		String friendSelected = lvSearchOutput.getSelectionModel().getSelectedItem().toString();
		User friend = progManager.loadUser(friendSelected);
		
		lStatusAdd.setVisible(true);
		lStatusAdd.setText(null);
		if(context.executeStrategy(user, friend)) {
			lStatusAdd.setTextFill(Color.LAWNGREEN);
			lStatusAdd.setText("Friend added!");
		} else {
			lStatusAdd.setText("Already friends");
			lStatusAdd.setTextFill(Color.INDIANRED);
		}
	}

	@FXML
	void keyPressed(KeyEvent event) {
		switch(event.getCode()) {
		case ENTER:
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
