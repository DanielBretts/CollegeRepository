package application;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.SQLIntegrityConstraintViolationException;
//import java.sql.Statement;
//import java.util.Iterator;
//import java.util.Vector;
import java.time.LocalDate;
import java.util.List;

//import SQLConnection.SQLConnector;
import classes.ProgManager;
//import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Group;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
//import javafx.stage.Stage;

public class RegisterController {

	private ProgManager progManager = ProgManager.getProgManager();
	
	final int MAX_USERNAME_LEN = 15;
	final int MIN_USERNAME_LEN = 5;
	final int MAX_PASSWORD_LEN = 15;
	final int MIN_PASSWORD_LEN = 8;

	@FXML
	private AnchorPane aPRegisterPaneFirst;

	@FXML
	private AnchorPane aPRegisterPaneSecond;

	@FXML
	private Button bRegister;

	@FXML
	private Button bChangeScreen;

	@FXML
	private Button bBackToLogin;

	@FXML
	private Label lError;

	@FXML
	private TextField pFPassword;

	@FXML
	private TextField tFUsername;

	@FXML
	private ComboBox<Integer> cbBirthYears;

	@FXML
	private Button bNext;

	@FXML
	private Button bBack;

	@FXML
	private Button bAddGame;

	@FXML
	private Button bRemoveGame;

	@FXML
	private ListView<String> lvGamesList;

	@FXML
	private ListView<String> lvGamesChosen;	



	@FXML
	private void initialize() {
		int currentYear = LocalDate.now().getYear();
		for (int i = 13; i < 120; i++) 
			cbBirthYears.getItems().add(currentYear - i);
	}

	@FXML
	void addGameToFavorites() {
		int selectedIdx = lvGamesList.getSelectionModel().getSelectedIndex();
		if(selectedIdx < 0)
			return;
		lvGamesChosen.getItems().add(lvGamesList.getSelectionModel().getSelectedItem().toString());
		lvGamesList.getItems().remove(selectedIdx);
	}

	@FXML
	void removeGameFromFavorites() {
		int selectedIdx = lvGamesChosen.getSelectionModel().getSelectedIndex();
		if(selectedIdx < 0)
			return;
		lvGamesList.getItems().add(lvGamesChosen.getSelectionModel().getSelectedItem().toString());
		lvGamesChosen.getItems().remove(selectedIdx);
	}

	@FXML
	void backToFirstRegister() {	//prev screen
		lError.setText(null);
		aPRegisterPaneFirst.setVisible(true);
		aPRegisterPaneSecond.setVisible(false);
	}


	@FXML
	void openGameChooser() {   //next screen
		if(checkReg()) {
			lError.setText(null);
			aPRegisterPaneFirst.setVisible(false);
			aPRegisterPaneSecond.setVisible(true);
			if(lvGamesList.getItems().isEmpty() && lvGamesChosen.getItems().isEmpty())
				lvGamesList.setItems((ObservableList<String>) progManager.loadGamesNamesFromDB());		
		}
	}

	boolean checkReg() {
		String username = tFUsername.getText();
		String password = pFPassword.getText().toString();
		int index = cbBirthYears.getSelectionModel().getSelectedIndex();
		
		if(username.isBlank()) {
			lError.setText("Username cannot be empty!");
			return false;
		}else if(password.isBlank()) {
			lError.setText("Password cannot be empty!");
			return false;
		}else if(index == -1) {
			lError.setText("Birth year cannot be empty!");
			return false;
		}else if (username.length() < MIN_USERNAME_LEN) {
			lError.setText("Username is too short! Atleast 5 chars!");
			return false;
		}else if (username.length() > MAX_USERNAME_LEN) {
			lError.setText("Username is too long! Maximum of 5 chars!");
			return false;
		} else if (password.length() < MIN_PASSWORD_LEN) {
			lError.setText("Password is too short! Atleast 8 chars!");
			return false;
		} else if (password.length() > MAX_PASSWORD_LEN) {
			lError.setText("Password is too long! Maximum of 15 chars!");
			return false;
		} else if(progManager.checkUserLogin(username)) {
			lError.setText("User already exist!");
			return false;
		}
		return true;
	}

	@FXML
	void register(ActionEvent event) {
		String name = tFUsername.getText(), password = pFPassword.getText().toString();
		List<String> gameNames = lvGamesChosen.getItems();
		if(cbBirthYears.getSelectionModel().getSelectedIndex() == -1) {
			lError.setText("Please choose your birth year.");
			return;
		}
		int birthYear = cbBirthYears.getSelectionModel().getSelectedItem();
		registerUser(name, password, gameNames, birthYear);
		openLogIn(event);
	}

	private void registerUser(String username, String password, List<String> list, int birthYear) {
//		progManager.addUser(username,password,list, birthYear);
		progManager.addNewUserToDB(username,password,birthYear);
		progManager.addUserFavGamesToDB(username,list);
	}

	@FXML
	void openLogIn(ActionEvent event) {
		String fxmlName = "LogIn.fxml";
		int height = 500, width = 800;
		WindowFactory.openWindow(event,fxmlName, width, height,getClass());
	}		     
}
