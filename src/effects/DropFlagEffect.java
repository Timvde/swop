package effects;

import item.Flag;
import java.util.List;
import java.util.Random;
import square.FlagKeeper;
import square.SquareContainer;
import square.TronObject;

/**
 * if the player is carrying a flag, the flag is dropped onto a randomly
 * selected square neighbouring the player's current square.
 */
public class DropFlagEffect extends AbstractEffect {
	
	/**
	 * This method will drop the flag of the specified tronobject (if it is a
	 * flagkeeper) onto one of the neigbouring squares.
	 * 
	 * @throws IllegalStateException
	 *         The flagkeeper's current square must have at least one neigbour.
	 */
	@Override
	public void execute(TronObject object) throws IllegalStateException {
		FlagKeeper flagkeeper = object.asFlagKeeper();
		if (flagkeeper != null) {
			Flag flag = flagkeeper.giveFlag();
			if (flag != null) {
				List<SquareContainer> neighbours = flagkeeper.getCurrentPosition()
						.getAllNeighbours();
				if (neighbours.size() == 0)
					throw new IllegalStateException(
							" The flagkeeper's current square must have at least one neigbour.");
				
				neighbours.get(new Random().nextInt(neighbours.size())).addItem(flag);
			}
		}
		
		super.execute(object);
	}
	
}
