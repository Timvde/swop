package grid;

import item.Item;

import java.util.ArrayList;
import java.util.List;

import player.IPlayer;

public class Square extends ASquare {

	private List<Item> itemList = new ArrayList<Item>();

	private IPlayer player;

	private boolean hasLightTrail;

	@Override
	public List<Item> getCarryableItems() {
		List<Item> result = new ArrayList<Item>();
		for (Item item : itemList) {
			if (item.isCarriable())
				result.add(item);
		}
		return result;
	}

	@Override
	public boolean hasLightTrail() {
		return this.hasLightTrail;
	}

	@Override
	public IPlayer getPlayer() {
		return this.player;
	}

	@Override
	public Item pickupItem(int ID) {
		for (Item itemOnSquare : this.itemList)
			if (ID == itemOnSquare.getId())
				return itemOnSquare;
		// if not yet returned --> not on square
		throw new IllegalArgumentException(
				"The square doesn't hold the requested item");
	}

	@Override
	public boolean hasItemWithID(int ID) {
		for (Item itemOnSquare : this.itemList)
			if (ID == itemOnSquare.getId())
				return true;
		return false;
	}

}
