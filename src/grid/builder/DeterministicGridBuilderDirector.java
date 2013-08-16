package grid.builder;

import grid.Coordinate;
import grid.Grid;

/**
 * Director to construct a new deterministic (predefined) {@link Grid} (for
 * testing purposes).
 */
public class DeterministicGridBuilderDirector extends GridBuilderDirector {
	
	/** the number of players on the test grid */
	public static final int			NUMBER_OF_PLAYERS_ON_TEST_GRID	= 2;
	/** The size of the predifined grid */
	public static final int			PREDIFINED_GRID_SIZE			= 10;
	/** The coordinate of the startingpostion of the first player */
	public static final Coordinate	PLAYER1_START_POS				= new Coordinate(
																			PREDIFINED_GRID_SIZE - 1,
																			0);
	/** The coordinate of the startingposition of the second player */
	public static final Coordinate	PLAYER2_START_POS				= new Coordinate(
																			0,
																			PREDIFINED_GRID_SIZE - 1);
	@SuppressWarnings("unused")
	private boolean					usePowerfailure;
	
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
	 * <li>t: teleporters (these teleport to each other)</li>
	 * <li>d: destination of the teleporters</li>
	 * <li>F: Power failure</li>
	 * <li>u: Uncharged Identity disc</li>
	 * </ul>
	 * 
	 * <i>(The origin (0,0) is in the top left corner and the top right corner
	 * is (9,0).)</i>
	 * 
	 * <pre>
	 * _____________________________
	 * |  |  |  |  |  |  |  | u|  | 1|
	 * |  |  |  |  |  |  |  |  |  |  |
	 * |  |  |  |  |  | 1|  | l|  |t1|
	 * |  |  |  |  |  |  |  |  |  |  |
	 * |  |  |  |  |  |  |  |  |  |  |
	 * |  |  |  |  | x| x| x| x| x|  |
	 * |  |  |  |  |  |  |  | l|  |  |
	 * |t2|  | l|  |  |  |  |  | l|  |
	 * |  |  |  |  |  | l| l| l| l|  |
	 * | 2|2 | u|  |  |  |  |  |  |  |
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
		
		builder.addPlayerStartingPosition(PLAYER1_START_POS, 1);
		builder.addPlayerStartingPosition(PLAYER2_START_POS, 2);
		
		builder.placeFlag(PLAYER1_START_POS, 1);
		builder.placeFlag(PLAYER2_START_POS, 2);
		
		Coordinate t1 = new Coordinate(PREDIFINED_GRID_SIZE - 1, 2);
		Coordinate t2 = new Coordinate(0, PREDIFINED_GRID_SIZE - 3);
		builder.placeTeleporter(t1, t2);
		builder.placeTeleporter(t2, t1);
	}
	
	/**
	 * Returns a random coordinate on the test grid.
	 * 
	 * @return a random coordinate on the test grid
	 */
	public Coordinate getRandomCoordinateOnTestGrid() {
		return Coordinate.random(PREDIFINED_GRID_SIZE, PREDIFINED_GRID_SIZE);
	}
}
