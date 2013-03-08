package game;

import player.Player;

public interface IGame {
	
	/**
	 * TODO
	 * @param width
	 * @param height
	 */
	public void newGame(int width, int height);
	
	/**
	 * TODO
	 * 
	 * @param p
	 */
	public void endGame(Player p);
}
