package scenariotests;

import static org.junit.Assert.assertEquals;
import grid.Grid;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Before;
import player.PlayerDataBase;
import square.PlayerStartingPosition;
import controllers.EndTurnController;
import controllers.MoveController;
import controllers.PickUpItemController;

@SuppressWarnings("javadoc")
public class SetupTestGrid {
	
	protected EndTurnController		endTurnCont;
	protected MoveController		moveCont;
	protected PickUpItemController	pickUpCont;
	protected Grid					grid;
	protected PlayerDataBase		playerDB;
	
	@Before
	public void setUp() {
		TronGridBuilder builder = new TronGridBuilder();
		DeterministicGridBuilderDirector director = new DeterministicGridBuilderDirector(builder,
				false);
		director.construct();
		grid = builder.getResult();
		
		// make a set with the startingpostions in a deterministic order
		Set<PlayerStartingPosition> playerstartingpositions = new LinkedHashSet<PlayerStartingPosition>();
		playerstartingpositions.add((PlayerStartingPosition) grid
				.getSquareAt(DeterministicGridBuilderDirector.PLAYER1_START_POS));
		playerstartingpositions.add((PlayerStartingPosition) grid
				.getSquareAt(DeterministicGridBuilderDirector.PLAYER2_START_POS));
		
		playerDB = new PlayerDataBase();
		playerDB.createNewDB(playerstartingpositions);
		assertEquals(1, playerDB.getCurrentPlayer().getID());
		
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
		pickUpCont = new PickUpItemController(playerDB);
	}
	
}
