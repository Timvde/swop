package grid.builder;

import grid.Coordinate;
import grid.Grid;

/**
 * Director to construct a new deterministic (predefined) {@link Grid} (for
 * testing purposes).
 */
public class DeterministicGridBuilderDirector extends GridBuilderDirector {
	
	/**
	 * The size of the predifined grid
	 */
	public static final int	PREDIFINED_GRID_SIZE	= 10;
	
	private boolean			usePowerfailure;
	
	/**
	 * Create a new DeterministicGridDirector which will use the specified
	 * builder to build the predefined {@link Grid}.
	 * 
	 * @param builder
	 *        The builder this director will use to build the grid.
	 * @param usePowerFailure
	 *        Whether or not to include powerfailure in the grid.
	 */
	public DeterministicGridBuilderDirector(GridBuilder builder, boolean usePowerFailure) {
		super(builder);
		this.usePowerfailure = usePowerFailure;
	}
	
	/**
	 * This function returns a predefined grid which we can use to test. This
	 * should not be used in game play. The returned grid is shown below. The
	 * legend for this grid is a followed:
	 * <ul>
	 * <li>numbers: starting position of the players</li>
	 * <li>x: walls</li>
	 * <li>l: light grenades</li>
	 * <li>t: teleporters (these teleport to the square right above)</li>
	 * <li>d: destination of the teleporters</li>
	 * <li>F: Power failure</li>
	 * <li>u: Uncharged Identity disc</li>
	 * </ul>
	 * 
	 * <pre>
	 * _____________________________
	 * |  |  |  | F| F| F|  | u|  | 2|
	 * |  |  |  | F| F| F|  |  |  |  |
	 * |  |  |  | F| F| F|  | l|  |t1|
	 * |  |  |  |  |  |  |  |  |  |d2|
	 * |  |  |  |  |  |  |  |  |  |  |
	 * |  |  |  |  | x| x| x| x| x|  |
	 * |  |  |  |  |  |  |  | l|  |  |
	 * |t2|  | l|  |  |  |  |  | l|  |
	 * |d1|  |  |  |  | l| l| l| l|  |
	 * | 1|  | u|  |  |  |  |  |  |  |
	 * -------------------------------
	 * </pre>
	 * 
	 */
	@Override
	public void construct() {
		builder.createNewEmptyGrid();
		
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
		
		builder.addPlayerStartingPosition(new Coordinate(0, 0));
		builder.addPlayerStartingPosition(new Coordinate(PREDIFINED_GRID_SIZE - 1,
				PREDIFINED_GRID_SIZE - 1));
		
		if (usePowerfailure) {
			//TODO do we need support for powerfailures?
		}
	}
}
