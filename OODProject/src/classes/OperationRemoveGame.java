package classes;

import interfaces.GameStrategy;

public class OperationRemoveGame implements GameStrategy{

	@Override
	public boolean doOperation(User user, String gameName) {
		if(ProgManager.getProgManager().removeFavGameFromDB(user, gameName))
			return true;	
		return false;
	}




}
