package classes;

import javafx.scene.paint.Color;

public class Message {
	private int cid;
	private String time;
	private String username;
	private String message;
	private Color color;


	public Message(int cid, String time, String username, String message) {
		this.cid = cid;
		this.time = time;
		this.username = username;
		this.message = message;
	}
	
	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public Color getColor() {
		return color;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return username + " - " + time + "\n" + message;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Message) {
			obj = (Message)obj;
			Message objM = (Message)obj;
			return this.username.equals(objM.username) && this.time.equals(objM.time) && this.message.equals(objM.message);
		}
		return false;
	}

	

}
