package effects;

import item.Flag;
import item.IItem;
import java.util.List;
import java.util.Random;
import player.TronPlayer;
import square.PropertyType;
import square.SquareContainer;
import square.TronObject;

/**
 * if the player is carrying a flag, the flag is dropped onto a randomly
 * selected square neighbouring the player's current square.
 */
public class DropFlagEffect extends AbstractEffect {
	
	/**
	 * This method will drop the flag of the specified tronobject (if it is a
	 * flagkeeper) onto one of the neigbouring squares which cannot be a
	 * wallpart.
	 * 
	 * @throws IllegalStateException
	 *         The flagkeeper's current square must have at least one neigbour.
	 */
	@Override
	public void execute(TronObject object) throws IllegalStateException {
		if (object instanceof TronPlayer) {
			TronPlayer flagkeeper = (TronPlayer) object;
			Flag flag = getFlagFromPlayer(flagkeeper); 
			if (flag != null) {
				List<SquareContainer> neighbours = flagkeeper.getCurrentPosition()
						.getAllNeighbours();
				if (neighbours.size() == 0)
					throw new IllegalStateException(
							" The flagkeeper's current square must have at least one neigbour.");
				
				// Try to choose a valid neighbour square that is not a
				// wallpart.
				SquareContainer neighbour = neighbours.get(new Random().nextInt(neighbours.size()));
				int i = 0;
				while (neighbour.hasProperty(PropertyType.WALL)) {
					neighbour = neighbours.get(new Random().nextInt(neighbours.size()));
					
					if (i > 8) {
						throw new IllegalStateException(
								"Cannot choose a valid neighbour to drop the flag on!");
					}
					
					i++;
				}
				neighbour.addItem(flag);
			}
		}
		super.execute(object);
	}
	
	/**
	 * Remove the flag from the player inventory if he got it. Else returns
	 * null.
	 */
	private Flag getFlagFromPlayer(TronPlayer player) {
		for (IItem item : player.getInventoryContent()) {
			if (item instanceof Flag) {
				player.getInventory().removeItem(item);
				return (Flag) item;
			}
		}
		return null;
	}
	
}
