package grid;

import item.IItem;
import item.Item;
import java.util.ArrayList;
import java.util.List;
import player.IPlayer;

public class Square extends ASquare {
	
	private List<Item>	itemList;
	
	private IPlayer		player;
	
	private boolean		hasLightTrail;
	
	@Override
	public List<IItem> getItemList() {
		List<IItem> itemList = new ArrayList<IItem>();
		
		for (IItem item : itemList)
			itemList.add(item);
		
		return itemList;
	}
	
	/**
	 * Returns whether or not a lightrail is currently active on this square.
	 * 
	 * @return whether or not a lightrail is currently active on this square.
	 */
	public boolean hasLightTrail() {
		return this.hasLightTrail;
	}
	
}
