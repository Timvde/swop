package grid;

import item.IItem;
import java.util.ArrayList;
import java.util.List;
import player.IPlayer;

/**
 * This class exists to enforce the wall restrictions as class invariants. - A
 * wall needs to be straight (i.e. either horizontally or vertically aligned) -
 * A wall needs to consist of at least two squares. A wall cannot intersect with
 * another wall.
 */
public class Wall {
	
	private Coordinate		start;
	private Coordinate		end;
	
	private static WallPart	wallPart;
	
	/**
	 * Construct a Wall with the given start and end coordinates
	 * 
	 * @param start
	 *        Start coordinate
	 * @param end
	 *        End coordinate
	 * @throws IllegalArgumentException
	 *         The specified Coordinates are not aligned
	 * @throws IllegalArgumentException
	 *         A wall should should consist of at least two squares
	 */
	public Wall(Coordinate start, Coordinate end) throws IllegalArgumentException {
		if (start.getX() != end.getX() && start.getY() != end.getY())
			throw new IllegalArgumentException("Coordinates not aligned");
		
		if (start.getX() == end.getX() && start.getY() == end.getY())
			throw new IllegalArgumentException("A wall should consist of at least two squares");
		
		/*
		 * The one with the smallest x or y value should be first. This is
		 * consistent, as at least one of them has to be the same.
		 */
		if (start.getX() > end.getX() || start.getY() > end.getY()) {
			this.start = end;
			this.end = start;
		}
		else {
			this.start = start;
			this.end = end;
		}
	}
	
	/*
	 * The following methods are package private, since they can be accessed by
	 * Grid, to create a predefined test grid.
	 */
	Coordinate getStart() {
		return start;
	}
	
	Coordinate getEnd() {
		return end;
	}
	
	/**
	 * This method returns whether this wall touches another specified wall
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
	
	/**
	 * Returns a WallPart. As all WallParts are actually the same, this will be
	 * a reference to the same object every time.
	 * 
	 * @return A WallPart
	 */
	public WallPart getWallPart() {
		if (wallPart == null)
			wallPart = new WallPart();
		return wallPart;
	}
	
	/**
	 * A class that represents a part of a wall.
	 */
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
		
		@Override
		public void setPlayer(IPlayer p) {
			throw new IllegalStateException("Can not set a player on a wallpart!");
		}
		
		@Override
		public void removePlayer() {
			throw new IllegalStateException(
					"Can not remove a player on a wallpart! No players are supposed to be on it.");
		}
	}
}
