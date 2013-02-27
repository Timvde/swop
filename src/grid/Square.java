package grid;

import item.Item;
import java.util.ArrayList;
import java.util.List;
import player.IPlayer;

public class Square extends ASquare {
	
	private List<Placeable>	placeableList;
	
	private IPlayer			player;
	
	@Override
	public List<Item> getItemList() {
		List<Item> itemList = new ArrayList<>();
		for (Placeable placeable : placeableList)
			if (placeable instanceof Item)
				itemList.add((Item) placeable);
		return itemList;
	}
	
}
