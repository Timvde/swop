package controllers;

import game.Game;


public class NewGameController {
	
	private Game game;
	
	public NewGameController(Game g) {
		this.game = g;
	}
	
	public void newGame(int width, int height) {
		this.game.newGame(width, height);
	}
}