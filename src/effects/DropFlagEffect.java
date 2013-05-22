package effects;

import item.Flag;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import square.Direction;
import square.FlagKeeper;
import square.SquareContainer;
import square.TronObject;

/**
 * if the player is carrying a flag, the flag is dropped onto a randomly
 * selected square neighbouring the player's current square.
 */
public class DropFlagEffect extends AbstractEffect {
	
	@Override
	public void execute(TronObject object) {
		FlagKeeper flagkeeper = object.asFlagKeeper();
		if (flagkeeper != null) {
			Flag flag = flagkeeper.giveFlag();
			if (flag != null) {
				List<SquareContainer> neighbours = this.getAllNeighboursOf(flagkeeper
						.getCurrentPosition());
				neighbours.get(new Random().nextInt(neighbours.size())).addItem(flag);
			}
		}
		
		super.execute(object);
	}
	
	private List<SquareContainer> getAllNeighboursOf(SquareContainer sq) {
		List<SquareContainer> result = new ArrayList<SquareContainer>();
		for (Direction dir : Direction.values()) {
			if (sq.getNeighbourIn(dir) != null)
				result.add(sq.getNeighbourIn(dir));
		}
		return result;
	}
	
}
