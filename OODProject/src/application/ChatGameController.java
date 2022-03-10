package application;

import java.io.IOException;
import java.util.List;

import classes.Chat;
import classes.Message;
import classes.ProgManager;
import classes.User;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ChatGameController extends ChatController{

	private ProgManager progManager = ProgManager.getProgManager();

	private Chat chat = new Chat();

	private Color userColor;
	//private List<Color> usersColors;

	private Message lastMsg;

	private MessagePopup msgPU;

	@FXML
	private AnchorPane apChat;

	@FXML
	private Button bSend;

	@FXML
	private Label lQuit;

	@FXML
	private ScrollPane spUsers;

	@FXML
	private ScrollPane spMessages;

	@FXML
	private VBox messagesArea;

	@FXML
	private TextField tfUserMessage;


	public Color getUserColor() {
		return userColor;
	}

	public void setUserColor(Color userColor) {
		this.userColor = userColor;
	}


	@FXML
	void sendMessage(Event event) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				String msg = tfUserMessage.getText().toString();
				tfUserMessage.setText(null);
				getChat().sendMessage(msg);
			}
		});
	}

	@FXML
	void openChatMenu(Event event) {
		try {
			stopThread();
			String fxmlName = "ChatMenu.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
			Parent p = loader.load();
			ChatMenuController cont = loader.getController();
			User user = getChat().getUser();
			cont.setUser(user);
			int height = 400, width = 400;
			WindowFactory.newWindowOpenner(event, width, height, getClass(), p);
			WindowFactory.app_stage.setTitle("Welcome " + user.getUserName() + "!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public void loadChat() {
		List<Message> chatMessages = chat.loadChat();

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < chatMessages.size(); i++) {
					Message s = chatMessages.get(i);
					String sender = s.getUsername();
					System.out.println(sender);
					User u = progManager.loadUser(sender);
					sendMessageToChat(s, u);
				}	
			}

		});
	}


	public void sendMessageToChat(Message s, User user) {
		Color color = userColor;
		List<User> users = chat.getUserListInChat();
		System.out.println(users.get(0).getPassword());
		String username = user.getUserName();
		if(!username.equals(chat.getUser().getUserName())) {
			for (User u : users) {
				System.out.println(u.getUserName());
				if(username.equals(u.getUserName())) {
					System.out.println("kaki");
					color = u.getChatColor();
				}
			}
		}
		final Color COLOR = color;

		this.lastMsg = s;
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				VBox v = new VBox();
				Label sender = new Label(user.getUserName());
				sender.setTextFill(COLOR);
				sender.setMinWidth(messagesArea.getWidth());
				sender.setStyle("-fx-font-weight: bold");
				Label lMessage = new Label(s.getMessage());
				lMessage.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, null, null)));
				lMessage.setMinWidth(messagesArea.getWidth());
				Label time = new Label(s.getTime());
				time.setMinWidth(messagesArea.getWidth());
				v.getChildren().addAll(sender,lMessage,time);
				messagesArea.getChildren().add(v);
				spMessages.setVvalue(spMessages.getVmax());

			}
		});
	}

	public Message getLastMsg() {
		return this.lastMsg;
	}

	@FXML
	void keyPressed(KeyEvent event) {
		switch(event.getCode()) {
		case ENTER:
			sendMessage(event);
			break;
		case ESCAPE:
			openChatMenu(event);
			break;
		default:
			break;
		}
	}

	public void setThread() {
		ChatGameController c = this;
		this.msgPU = new MessagePopup(chat, c);;

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Thread thread = new Thread(msgPU);
				thread.start();
			}
		});

	}


	public void stopThread() {
		this.msgPU.stop();
	}



}


