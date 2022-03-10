package classes;

import interfaces.GameStrategy;

public class GameContext {
	
private GameStrategy gs;
	
	public GameContext(GameStrategy gs) {
		this.gs = gs;
	}
	
	public boolean executeStrategy(User user, String gameName) {
		return gs.doOperation(user, gameName);
	}
}
