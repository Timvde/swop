package actions;

import grid.Direction;



public class MoveAction extends GridAction {
	
	private Direction direction;
	
	public MoveAction(Direction direction) {
		this.direction = direction;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	public void execute() {
		getGrid().movePlayer(getPlayer().getID(), direction);
	}
}
