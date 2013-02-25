package grid;

import item.IItem;
import java.util.ArrayList;
import java.util.List;

public class Wall extends ASquare {

	@Override
	public List<IItem> getItemList() {
		return new ArrayList<>();
	}
	
}
