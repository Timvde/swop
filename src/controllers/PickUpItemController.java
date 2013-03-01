package controllers;

import game.Game;
import item.IItem;
import actions.PickUpAction;

public class PickUpItemController {
	

	private Game game;
	
	public PickUpItemController(Game game) {
		this.game = game;
	} 
	
	public void pickUpItem(IItem item) {
		PickUpAction action = new PickUpAction();
		game.executeGridAction(action);
	}
	
}
