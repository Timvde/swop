package player;

import grid.Coordinate;
import grid.Direction;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import item.IItem;

public class Player implements IPlayer {
	

	private int						id; 

	private static AtomicInteger	nextID		= new AtomicInteger();

	private Coordinate				targetPosition;

	private Inventory				inventory	= new Inventory();
	
	
	public Player(Coordinate targetPosition) {
		this.id = nextID.incrementAndGet();
		this.targetPosition = targetPosition;
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	/**
	 * TODO
	 * @return
	 */
	private boolean isPreconditionMoveSatisfied() {
		return false;
	}
	
	@Override
	public Coordinate getTargetPosition() {
		return this.targetPosition;
	}
	
	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public void IncreaseActionCounter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move(Direction d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endTurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void useItem(IItem i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickUpItem(IItem i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decreaseNumberOfActions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skipNumberOfActions(int n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumberOfActionsLeft() {
		// TODO Auto-generated method stub
		return 0;
	}
}
