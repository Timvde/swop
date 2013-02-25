package grid;

import item.IItem;
import item.Placeable;
import java.util.ArrayList;
import java.util.List;
import player.IPlayer;

public class Square extends ASquare {
	
	private List<Placeable>	placeableList;
	
	private IPlayer			player;
	
	private boolean			hasLightTrail;
	
	@Override
	public List<IItem> getItemList() {
		List<IItem> itemList = new ArrayList<IItem>();
		
		for (Placeable placeable : placeableList)
			if (placeable instanceof IItem)
				itemList.add((IItem) placeable);
		
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
