package actions;

import item.IItem;
import grid.Square;


public class PickUpAction extends GridAction {
	
	private IItem item;
	
	public IItem getItem() {
		return this.item;
	}

	@Override
	public void execute() {
		IItem item = getGrid().getItemList(getGrid().getPlayerCoordinate(getPlayer().getID())).get(0);
		((Square) getGrid().getGrid().get(getGrid().getPlayerCoordinate(getPlayer().getID()))).removeItem(item);
		getPlayer().getInventory().add(item);
	}
}
