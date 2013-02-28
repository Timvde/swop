package grid;

import item.IItem;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import player.Player;


public class Grid implements IGrid {
	
	private HashMap<Coordinate, ASquare> grid;
	
	
	@Override
	public boolean canMovePlayer(Player player, Direction direction) {
		return false;
	}
	
	@Override
	public void movePlayer(Player player, Direction direction) {
		
	}
	
	@Override
	public List<IItem> getItemList(Coordinate coordinate) {
		return grid.get(coordinate).getCarryableItems();
	}
	
	@Override
	public ASquare getSquareAt(Coordinate coordinate) {
		return grid.get(coordinate);
	}
	
	@Override
	public Set<Coordinate> getAllGridCoordinates() {
		return this.grid.keySet();
	}
	
}
