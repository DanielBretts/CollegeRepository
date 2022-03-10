package interfaces;

import classes.User;

public interface GameStrategy {

	boolean doOperation(User user, String gameName);
	
}
