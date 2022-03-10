package classes;

import java.util.List;

import interfaces.FriendStrategy;

public class OperationAddFriend implements FriendStrategy{

	@Override
	public boolean doOperation(User user, User friend) {
		String friendName = friend.getUserName();
		List<String> friendList = user.getFriendsFromDB();
		for(String friendStr : friendList)	
			if(friendStr.equals(friendName)) {
				System.out.println("Friend already in friend list!");
				return false;
			}
		user.addNewFriendToDB(friend);
		user.getFriends().add(friend);
		System.out.println(friendName + " ADDED!");
		return true;
	}

	
	
}
