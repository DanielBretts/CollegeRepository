package application;

import java.io.IOException;
//import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import classes.ProgManager;
import classes.User;
import exceptions.BadInputException;
import exceptions.BadInputException.BadPasswordInputException;
import exceptions.BadInputException.BadUsernameInputException;
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
//import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class LogInController implements Initializable {

	private ProgManager progManager = ProgManager.getProgManager();
	
	@FXML
	private Label lLogin;

	@FXML
	private Label lError;

	@FXML
	private Label lRegister;

	@FXML
	private PasswordField pFPassword;

	@FXML
	private TextField tFUsername;

	@FXML
	private MainProgController mainProgCont;


	@FXML
	void logIn(Event event) {
		try {			
			String username = tFUsername.getText();

			if(username.isBlank())
				throw new BadInputException().new BadUsernameInputException("Username cannot be empty!");

			String password = pFPassword.getText().toString();

			if(password.isBlank())
				throw new BadInputException().new BadPasswordInputException("password cannot be empty!");
			
			User user = progManager.loadUser(username);
			
			if(user == null)
				throw new BadInputException().new BadUsernameInputException("User does not exist!");
			
			user.setFriends(progManager.loadFriends(user));
			
			if(!user.getPassword().equals(password))
				throw new BadInputException().new BadPasswordInputException("Bad password!");

			openMainProg(event, user);

		} catch (BadUsernameInputException buie) {
			lError.setText(buie.getMessage());
		} catch (BadPasswordInputException bpie) {
			lError.setText(bpie.getMessage());
		}catch (Exception e) {
			lError.setText("General exception: \n" + e.getMessage());
		}
	}


	void openMainProg(Event event,User user) {
		try {
			String fxmlName = "mProg.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
			Parent root = loader.load();
			MainProgController mpCont = loader.getController();
			mpCont.setUser(user);
			int height = 108, width = 592;
			WindowFactory.newWindowOpenner(event, width, height,getClass(), root);
			WindowFactory.app_stage.setTitle("Welcome " + user.getUserName() + "!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@FXML
	public void buttonPressed(KeyEvent e) { 
		if(e.getCode() == KeyCode.ENTER) {
			logIn(e);
		}else if(e.getCode() == KeyCode.ESCAPE) {
			Stage sb = (Stage)tFUsername.getScene().getWindow();
			sb.close();
		}
	}


	@FXML
	void openRegister(MouseEvent event) {
		String fxmlName = "Register.fxml";
		int height = 500, width = 939;
		WindowFactory.openWindow(event,fxmlName, width, height,getClass());
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		WindowFactory.playTransition(getClass());
	}

}
