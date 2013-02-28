package grid;

import item.Item;

import java.util.ArrayList;
import java.util.List;

import player.IPlayer;

/**
 * This class exists to enforce the wall restrictions as class invariants.
 * - A wall needs to be straight (i.e. either horizontally or vertically aligned)
 * - A wall needs to consist of at least two squares.
 */
public class Wall {
	
	private List<WallPart>	parts	= new ArrayList<WallPart>();
	private static WallPart wallPart;
	
	public Wall(Coordinate start, Coordinate stop) throws IllegalArgumentException {
		if (start.getX() != stop.getX() && start.getY() != stop.getY())
			throw new IllegalArgumentException("Coordinates not aligned");
		
		if (start.getX() == stop.getX() && start.getY() == stop.getY())
			throw new IllegalArgumentException("A wall should consist of at least two squares");
		
		int numberOfParts = Math.abs((start.getX() == stop.getX() ? start.getY() - stop.getY()
				: start.getX() - stop.getX()));
		
		// "Less than or equal to" is intentional. Imagine having a wall on
		// (0,0) and (0,1). The previous "numberOfParts" calculation will return
		// 1 - 0 == 1. Therefore, we needed to either add 1 to the previous
		// calculation, or change this "<" to "<=".
		for (int i = 0; i <= numberOfParts; i++)
			parts.add(wallPart);
		
	}
	
	public WallPart getWallPart() {
		if (wallPart == null)
			wallPart = new WallPart();
		return wallPart;
	}

	public class WallPart extends ASquare {
		
		private WallPart() {}
		
		@Override
		public List<Item> getCarryableItems() {
			return new ArrayList<Item>();
		}
		
		@Override
		public IPlayer getPlayer() {
			return null;
		}

		@Override
		public boolean hasLightTrail() {
			return false;
		}

		@Override
		public Item pickupItem(int ID) throws IllegalArgumentException {
			throw new IllegalArgumentException("Walls do not contain items");
		}

		@Override
		public boolean hasItemWithID(int ID) {
			return false;
		}
		
	}
	
}
