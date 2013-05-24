package scenariotests;

import static org.junit.Assert.assertEquals;
import game.GameMode;
import grid.Grid;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import java.util.ArrayList;
import java.util.List;
import player.PlayerDataBase;
import square.SquareContainer;
import controllers.EndTurnController;
import controllers.MoveController;
import controllers.PickUpItemController;
import controllers.UseItemController;

@SuppressWarnings("javadoc")
public abstract class SetUpTestGrid {
	
	protected EndTurnController		endTurnCont;
	protected MoveController		moveCont;
	protected PickUpItemController	pickUpCont;
	protected UseItemController		useItemCont;
	protected Grid					grid;
	protected PlayerDataBase		playerDB;
	
	protected void setUp(GameMode mode) {
		TronGridBuilder builder = new TronGridBuilder(mode.getEffectFactory());
		DeterministicGridBuilderDirector director = new DeterministicGridBuilderDirector(builder,
				false);
		director.construct();
		grid = builder.getResult();
		
		// make a list with the startingpostions in a deterministic order
		List<SquareContainer> playerstartingpositions = new ArrayList<SquareContainer>();
		playerstartingpositions.add((SquareContainer) grid
				.getSquareAt(DeterministicGridBuilderDirector.PLAYER1_START_POS));
		playerstartingpositions.add((SquareContainer) grid
				.getSquareAt(DeterministicGridBuilderDirector.PLAYER2_START_POS));
		
		playerDB = new PlayerDataBase();
		playerDB.createNewDB(playerstartingpositions);
		assertEquals(1, playerDB.getCurrentPlayer().getID());
		
		moveCont = new MoveController(playerDB);
		endTurnCont = new EndTurnController(playerDB);
		pickUpCont = new PickUpItemController(playerDB);
		useItemCont = new UseItemController(playerDB);
	}
	
}