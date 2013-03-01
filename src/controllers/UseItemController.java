package controllers;

import game.Game;
import actions.MoveAction;
import item.Item;
import actions.UseItemAction;

public class UseItemController {
	
	private Game game; 
	
	public UseItemController(Game game) {
		this.game = game;
	}
	
	public void useItem(Item item) {
		UseItemAction action = new UseItemAction(item);
		game.executeAction(action);
	}
}
