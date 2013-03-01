package actions;

import grid.Square;
import item.IItem;


public class UseItemAction extends GridAction {
	
	private IItem item;
	
	public UseItemAction(IItem item) {
		this.item = item;
	}
	
	public IItem getItem() {
		return this.item;
	}

	@Override
	public void execute() {
		getPlayer().getInventory().remove(item);
		((Square) getGrid().getGrid().get(getGrid().getPlayerCoordinate(getPlayer().getID()))).addItem(item);
	}
}
