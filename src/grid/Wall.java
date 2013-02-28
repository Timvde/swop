package grid;

import item.IItem;
import java.util.ArrayList;
import java.util.List;
import player.IPlayer;

/**
 * This class exists to enforce the wall restrictions as class invariants. - A
 * wall needs to be straight (i.e. either horizontally or vertically aligned) -
 * A wall needs to consist of at least two squares.
 */
public class Wall {
	
	Coordinate				start;
	Coordinate				end;
	
	private List<WallPart>	parts	= new ArrayList<WallPart>();
	private static WallPart wallPart;
	
	public Wall(Coordinate start, Coordinate end) throws IllegalArgumentException {
		if (start.getX() != end.getX() && start.getY() != end.getY())
			throw new IllegalArgumentException("Coordinates not aligned");
		
		if (start.getX() == end.getX() && start.getY() == end.getY())
			throw new IllegalArgumentException("A wall should consist of at least two squares");
		
		this.start = start;
		this.end = end;
		
		int numberOfParts = Math.abs((start.getX() == end.getX() ? start.getY() - end.getY()
				: start.getX() - end.getX()));
		
		// "Less than or equal to" is intentional. Imagine having a wall on
		// (0,0) and (0,1). The previous "numberOfParts" calculation will return
		// 1 - 0 == 1. Therefore, we needed to either add 1 to the previous
		// calculation, or change this "<" to "<=".
		for (int i = 0; i <= numberOfParts; i++)
			parts.add(wallPart);
		
	}
	
	private Coordinate getStart() {
		return start;
	}
	
	private Coordinate getEnd() {
		return end;
	}
	
	public List<WallPart> getWallParts() {
		return parts;
	}
	
	/**
	 * This method returns whether this wall touches another wall
	 * 
	 * @param wall
	 *        Another wall to compare with
	 * @return True when they touch, false when they don't
	 */
	public boolean touchesWall(Wall wall) {
		/*
		 * To do this easily, we "spoof" one off the walls to be longer and
		 * thicker, and check whether they intersect.
		 */
		
		// If this is true, they can't touch because they don't have any overlap
		// in the X direction
		if (((this.getStart().getX() - 1) > wall.getEnd().getX())
				|| this.getEnd().getX() + 1 < wall.getStart().getX())
			return false;
		
		// If this is true, they can't touch because they don't have any overlap
		// in the Y direction
		if (((this.getStart().getY() - 1) > wall.getEnd().getY())
				|| this.getEnd().getY() + 1 < wall.getStart().getY())
			return false;
		
		// Otherwise, they have overlap in both directions, and they'll touch
		return true;
	}
	
	public WallPart getWallPart() {
		if (wallPart == null)
			wallPart = new WallPart();
		return wallPart;
	}

	public class WallPart extends ASquare {
		
		private WallPart() {}
		
		@Override
		public List<IItem> getCarryableItems() {
			return new ArrayList<IItem>();
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
		public IItem pickupItem(int ID) throws IllegalArgumentException {
			throw new IllegalArgumentException("Walls do not contain items");
		}

		@Override
		public boolean hasItemWithID(int ID) {
			return false;
		}
	}
}
