package controllers;

import item.IItem;
import item.Item;
import java.util.List;
import game.Game;


public class GetInventoryListController {
	private Game game;
	
	public GetInventoryListController(Game g) {
		this.game = g;
	}
	
	public List<IItem> getCurrentPlayerInventory() {
		return this.game.getCurrentPlayer().getInventory();
	}
}
