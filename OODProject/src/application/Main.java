package application;
	

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane aPLogIn = (AnchorPane)FXMLLoader.load(getClass().getResource("LogIn.fxml"));
			Scene sceneLog = new Scene(aPLogIn,800,500);
			sceneLog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(sceneLog);
			primaryStage.show();
			primaryStage.setTitle("Discord Afeka");
			primaryStage.getIcons().add(new Image(getClass().getResource("/Images/LOGO ICON.png").toURI().toString()));
			WindowFactory.app_stage = primaryStage;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//User admin = new User("Admin", "admin1", null, 200);
//		ProgManager progManager = ProgManager.getProgManager();
		//progManager.addUser(admin);
		launch(args);
	}
}
