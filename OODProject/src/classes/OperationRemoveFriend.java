package classes;

import interfaces.FriendStrategy;

public class OperationRemoveFriend implements FriendStrategy{

	@Override
	public boolean doOperation(User user, User friend) {
		if(ProgManager.getProgManager().removeChatFromDB(user.getFriendCID(friend))) {	//removing a chat from the system means removing friendship
			user.getFriends().remove(friend);
			System.out.println("friendship removed :(");
			return true;
		}
		return false;	
	}


}
