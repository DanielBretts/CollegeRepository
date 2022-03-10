package classes;

import interfaces.FriendStrategy;

public class FriendContext {

	private FriendStrategy fs;
	
	public FriendContext(FriendStrategy fs) {
		this.fs = fs;
	}
	
	public boolean executeStrategy(User user, User friend) {
		return fs.doOperation(user, friend);
	}
}
