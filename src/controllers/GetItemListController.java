package controllers;

import item.IItem;
import java.util.List;
import game.Game;
import grid.ASquare;
import grid.Coordinate;
import grid.Grid;

public class GetItemListController {
	
	private Game	game;
	
	public GetItemListController(Game game) {
		this.game = game;
	}
	
	public List<IItem> getSquareItemList(Grid grid) {
		Coordinate playerCoord = grid.getPlayerCoordinate(this.game.getCurrentPlayer()
				.getID());
		ASquare playerSquare = grid.getSquareAt(playerCoord);
		
		return playerSquare.getCarryableItems();
	}
}
