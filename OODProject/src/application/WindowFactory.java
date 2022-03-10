package application;

import java.net.URISyntaxException;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class WindowFactory {

	static Stage app_stage;


	public static void openWindow(Event event,String fxmlName, int width, int height, Class<?> class1) {
		try {
			Parent home_page_parent = FXMLLoader.load(class1.getResource(fxmlName));
			Scene sceneReg = new Scene(home_page_parent,width,height);
			sceneReg.getStylesheets().add(class1.getResource("application.css").toExternalForm());
			app_stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			app_stage.close(); //optional
			app_stage.setScene(sceneReg);
			app_stage.show();      
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void openNewWindow(Event event,String fxmlName, int width, int height, Class<?> class1) {
		try {
			Parent home_page_parent = FXMLLoader.load(class1.getResource(fxmlName));
			Scene sceneReg = new Scene(home_page_parent,height,width);
			sceneReg.getStylesheets().add(class1.getResource("application.css").toExternalForm());
			Stage app_stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			app_stage.setScene(sceneReg);
			app_stage.show();      
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void newWindowOpenner(Event event, int width, int height, Class<?> class1, Parent p) {
		try {
			Scene sceneReg = new Scene(p,width,height);
			sceneReg.getStylesheets().add(class1.getResource("application.css").toExternalForm());
			app_stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			app_stage.close(); //optional
			app_stage.setScene(sceneReg);
			app_stage.show();      
			WindowFactory.playTransition(class1);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public void setTitle(String text) {
		app_stage.setTitle(text);
	}

	public static void playTransition(Class<?> class1) {
		try {
			AudioClip plonkSound = new AudioClip(class1.getResource("/Images/transitionSound.mp3").toURI().toString());
			plonkSound.play();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
	}

}
