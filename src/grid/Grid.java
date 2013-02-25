package grid;

import item.Item;
import java.util.HashMap;
import java.util.List;
import com.google.java.contract.*;

public class Grid implements IGrid {
	
	private HashMap<Coordinate, ASquare>	grid;
	
	private Grid(Builder builder) {
		
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
	public List<Item> getItemList(Coordinate coordinate) {
		return grid.get(coordinate).getItemList();
	}
	
	public class Builder {
		
		private int		minimalLengthOfWall;
		private double	maximalLengthOfWall;
		private int		minimumNumberOfWalls;
		private double	maximumNumberOfWalls;
		private int		gridXSize;
		private int		gridYSize;
		
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
		 * set the minimum number of walls in the grid. This number must be
		 * positive!
		 * 
		 * @param minimum
		 *        the minimum number of walls
		 * @return this
		 */
		@Requires("minimum >= 0")
		public Builder setMinimumNumberOfWalls(int minimum) {
			this.minimumNumberOfWalls = minimum;
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
			this.gridXSize = width;
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
			this.gridYSize = height;
			return this;
		}
	}
}
