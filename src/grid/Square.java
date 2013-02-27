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
		return new ArrayList<IItem>(this.itemList);
	}
	
	@Override
	public boolean hasLightTrail() {
		return this.hasLightTrail;
	}
	
	@Override
	public IPlayer getPlayer() {
		return this.player;
	}
	
}
