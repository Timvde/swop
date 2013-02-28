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
	
	Coordinate start;
	Coordinate stop;
	/*
	 * TODO: Add a method to check whether two walls are validly placed.
	 */
	
	private List<WallPart>	parts	= new ArrayList<WallPart>();
	
	public Wall(Coordinate start, Coordinate stop) throws IllegalArgumentException {
		if (start.getX() != stop.getX() && start.getY() != stop.getY())
			throw new IllegalArgumentException("Coordinates not aligned");
		
		if (start.getX() == stop.getX() && start.getY() == stop.getY())
			throw new IllegalArgumentException("A wall should consist of at least two squares");
		
		this.start = start;
		this.stop = stop;
		
		int numberOfParts = Math.abs((start.getX() == stop.getX() ? start.getY() - stop.getY()
				: start.getX() - stop.getX()));
		
		// "Less than or equal to" is intentional. Imagine having a wall on
		// (0,0) and (0,1). The previous "numberOfParts" calculation will return
		// 1 - 0 == 1. Therefore, we needed to either add 1 to the previous
		// calculation, or change this "<" to "<=".
		for (int i = 0; i <= numberOfParts; i++)
			parts.add(new WallPart(this));
		
	}
	
	public class WallPart extends ASquare {
		
		private Wall	wall;
		
		private WallPart(Wall wall) {
			this.wall = wall;
		}
		
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
			return null;
		}

		@Override
		public boolean hasItemWithID(int ID) {
			return false;
		}
		
	}
	
	public List<WallPart> getWallParts() {
		return parts;
	}
	
}
