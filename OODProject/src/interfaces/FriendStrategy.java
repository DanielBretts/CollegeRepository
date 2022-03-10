package interfaces;

import classes.User;

public interface FriendStrategy {
	
	public boolean doOperation(User user, User friend);
	
}
