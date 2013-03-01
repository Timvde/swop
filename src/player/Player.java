package player;

import grid.Coordinate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import notnullcheckweaver.NotNull;
import item.IItem;

public class Player implements IPlayer {
	
	@NotNull
	private int						id;
	@NotNull
	private static AtomicInteger	nextID		= new AtomicInteger();
	@NotNull
	private Coordinate				targetPosition;
	@NotNull
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
		return this.targetPosition;
	}
	
	@Override
	public List<IItem> getInventory() {
		return inventory.getItems();
	}
}
