package scenariotests;

import game.GameMode;
import grid.Grid;
import player.PlayerDataBase;
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
	private GameMode				mode;
	
	protected void setUp(GameMode mode) {
		DummyGameRunner runner = new DummyGameRunner();
		runner.newDeterministicTestGame(mode);
		 
		this.endTurnCont = runner.getEndTurnCont();
		this.moveCont = runner.getMoveCont();
		this.pickUpCont = runner.getPickUpCont();
		this.useItemCont = runner.getUseItemCont();
		this.playerDB = runner.getPlayerDB();
		this.grid = runner.getGrid();
		this.mode = mode;
	}
	
	public GameMode getMode() {
		return mode;
	}
	
}
