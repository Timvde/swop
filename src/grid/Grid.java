package grid;

import grid.Wall.WallPart;
import item.IItem;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import com.google.java.contract.*;

public class Grid implements IGrid {
	
	private HashMap<Coordinate, ASquare>	grid;
	private int								width;
	private int								height;
	
	private Grid(Builder builder) {
		this.width = builder.width;
		this.height = builder.height;
		grid = new HashMap<>();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				grid.put(new Coordinate(i, j), new Square());
	}
	
	private void placeWall(double maxPercentage) {
		int wallLength = new Random().nextInt(getMaximumLengthOfWall(maxPercentage));
		Coordinate start = Coordinate.random(width, height);
		Wall wall = new Wall(start, start.getRandomCoordinateWithDistance(wallLength));
		while(!canPlaceWall(wall)) 
			wall = new
	}
	
	private int getMaximumLengthOfWall(double maxPercentage) {
		int maxLength = 0;
		int walls = getNumberOfWallParts();
		while(maxPercentage > (walls + maxLength++) / size());
		return maxLength;
	}
	
	/**
	 * returns the number of squares in this grid. If the grid contains more
	 * than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
	 * 
	 * @return the number of squares
	 */
	public int size() {
		return grid.size();
	}
	
	private int getNumberOfWallParts() {
		int i = 0;
		for (ASquare square : grid.values())
			if (square.getClass() == WallPart.class)
				i++;
		return i;
	}
	
	@Override
	public boolean canMovePlayer(Coordinate coordinate, Direction direction) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void movePlayer(Coordinate coordinate, Direction direction) {
		if (!canMovePlayer(coordinate, direction))
			throw new IllegalArgumentException("Player can not be moved in that direction!");
		
	}
	
	@Override
	public List<IItem> getItemList(Coordinate coordinate) {
		return grid.get(coordinate).getItemList();
	}
	
	public class Builder {
		
		private int		minimalLengthOfWall;
		private double	maximalLengthOfWall;
		private double	maximumNumberOfWalls;
		private int		width;
		private int		height;
		
		public Builder() {
			this.minimalLengthOfWall = 2;
			this.maximalLengthOfWall = 0.50;
			this.maximumNumberOfWalls = 0.20;
			this.width = 10;
			this.height = 10;
		}
		
		/**
		 * Set the minimum length of a wall in the grid. The specified length
		 * should be greater then or equal to 2.
		 * 
		 * @param minimalLength
		 *        the minimum length of a wall
		 * @return this
		 */
		@Requires("minimalLengthOfWall >= 2")
		public Builder setMinimalLengthOfWall(int minimalLength) {
			this.minimalLengthOfWall = minimalLength;
			return this;
		}
		
		/**
		 * set the maximal length of a wall as a percentage of the grid's
		 * length/width.
		 * 
		 * @param maximalLength
		 *        the maximal length of a wall
		 * @return this
		 */
		public Builder setMaximalLengthOfWall(double maximalLength) {
			this.maximalLengthOfWall = maximalLength;
			return this;
		}
		
		/**
		 * set the maximum number of walls in the grid
		 * 
		 * @param maximum
		 *        the maximum number of walls
		 * @return this
		 */
		public Builder setMaximumNumberOfWalls(int maximum) {
			this.maximumNumberOfWalls = maximum;
			return this;
		}
		
		/**
		 * set the width of the grid. This must be a strictly positive integer
		 * 
		 * @param width
		 *        the width of the grid
		 * @return this
		 */
		@Requires("width > 0")
		public Builder setGridWidth(int width) {
			this.width = width;
			return this;
		}
		
		/**
		 * set the height of the grid. This must be a strictly positive integer
		 * 
		 * @param height
		 *        the height of the grid
		 * @return this
		 */
		@Requires("height > 0")
		public Builder setGridHeigth(int height) {
			this.height = height;
			return this;
		}
	}
}
