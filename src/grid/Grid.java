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
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void movePlayer(Player player, Direction direction) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<IItem> getItemList(Coordinate coordinate) {
		return grid.get(coordinate).getItemList();
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
