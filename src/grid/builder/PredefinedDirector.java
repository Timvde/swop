package grid.builder;

import grid.Coordinate;
import java.util.ArrayList;
import java.util.List;
import square.Wall;

/**
 * Director to construct a new grid from a predefined grid.
 * 
 */
public class PredefinedDirector extends Director {
	
	private boolean usePowerfailure;
	
	/**
	 * @param builder
	 */
	public PredefinedDirector(GridBuilder builder) {
		super(builder);
	}
	
	/**
	 * This function returns a predefined grid which we can use to test. This
	 * should not be used in game play. The returned grid is shown below. The
	 * legend for this grid is a followed:
	 * <ul>
	 * <li>numbers: starting position of the players</li>
	 * <li>x: walls</li>
	 * <li>o: light grenades</li>
	 * <li>t: teleporters (these teleport to the square right above)</li>
	 * <li>d: destination of the teleporters</li>
	 * <li>F: Power failure</li>
	 * <li>i: Identity disc</li>
	 * </ul>
	 * 
	 * <pre>
	 * _____________________________
	 * |  |  |  | F| F| F|  | i|  | 2|
	 * |  |  |  | F| F| F|  |  |  |  |
	 * |  |  |  | F| F| F|  | o|  |t1|
	 * |  |  |  |  |  |  |  |  |  |d2|
	 * |  |  |  |  |  |  |  |  |  |  |
	 * |  |  |  |  | x| x| x| x| x|  |
	 * |  |  |  |  |  |  |  | o|  |  |
	 * |t2|  | o|  |  |  |  |  | o|  |
	 * |d1|  |  |  |  | o| o| o| o|  |
	 * | 1|  | i|  |  |  |  |  |  |  |
	 * -------------------------------
	 * </pre>
	 * 
	 */
	@Override
	public void construct() {
		List<Wall> walls = new ArrayList<Wall>();
		walls.add(new Wall(new Coordinate(4, 5), new Coordinate(8, 5)));
		
		buildTestGrid(usePowerfailure);
	}
	
	
	/**
	 * This function should ONLY be used by getPredefinedGrid(), to get a
	 * deterministic grid we can use to test. This should not be used in
	 * gameplay.
	 */
	private void buildTestGrid(boolean usePowerfailure) {

		for (int i = 0; i < PREDIFINED_GRID_SIZE; i++)
			for (int j = 0; j < PREDIFINED_GRID_SIZE; j++)
				builder.addSquare(new Coordinate(i, j));
		
		
		for (int i = 4; i <= 8; i++)
			builder.addWall(new Coordinate(i, 5));
		
		builder.placeLightGrenade(new Coordinate(2, 7));
		builder.placeLightGrenade(new Coordinate(5, 8));
		builder.placeLightGrenade(new Coordinate(6, 8));
		builder.placeLightGrenade(new Coordinate(7, 8));
		builder.placeLightGrenade(new Coordinate(7, 6));
		builder.placeLightGrenade(new Coordinate(8, 8));
		builder.placeLightGrenade(new Coordinate(8, 7));
		builder.placeLightGrenade(new Coordinate(7, 2));
		
		builder.placeUnchargedIdentityDisc(new Coordinate(7, 0));
		builder.placeUnchargedIdentityDisc(new Coordinate(2, 9));
		
		
	}
	
	public static final int	PREDIFINED_GRID_SIZE	= 10;
}