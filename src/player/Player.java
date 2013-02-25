package player;

import grid.Coordinate;
import item.Item;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import notnullcheckweaver.NotNull;

public class Player implements IPlayer {
	
	private int						id;
	private static AtomicInteger	nextID		= new AtomicInteger();
	private Coordinate				targetPosition;
	private Inventory				inventory	= new Inventory();
	
	public Player(@NotNull Coordinate targetPosition) {
		this.id = nextID.incrementAndGet();
		this.targetPosition = targetPosition;
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public Coordinate getTargetPosition() {
		return null;
	}
	
	@Override
	public List<Item> getInventory() {
		return inventory.getItems();
	}
	
}
