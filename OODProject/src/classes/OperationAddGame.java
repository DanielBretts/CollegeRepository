package classes;

import interfaces.GameStrategy;

public class OperationAddGame implements GameStrategy{

	@Override
	public boolean doOperation(User user,String gameName) {
		if(user.addFavoriteGameToDB(gameName))
			return true;
		return false;
	}

}
