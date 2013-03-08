package controllers;

import player.IPlayer;
import player.PlayerDatabase;


public class GUIDataController {
	
	private PlayerDatabase playerDB;
	
	public GUIDataController(PlayerDatabase playerDB) {
		this.playerDB = playerDB;
	}
	
	public IPlayer getCurrentPlayer() {
		return this.playerDB.getCurrentPlayer();
	}
	
}
