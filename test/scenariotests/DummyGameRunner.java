package scenariotests;

import player.PlayerDataBase;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.NewGameController;
import controllers.PickUpItemController;
import controllers.UseItemController;
import game.GameMode;
import game.GameRunner;
import grid.Grid;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import gui.DummyGUI;

@SuppressWarnings("javadoc")
public class DummyGameRunner extends GameRunner {
	
	private Grid	grid;

	
	public DummyGameRunner() {
		super();
		super.gui = new DummyGUI();
	}
	
	
	/**
	 * Start a new game that is read from a file.
	 * 
	 * @param mode
	 *        The mode for the new game
	 */
	public void newDeterministicTestGame(GameMode mode) {
		if (mode == null)
			throw new IllegalArgumentException("the mode cannot be null");
		
		TronGridBuilder builder = new TronGridBuilder(mode.getEffectFactory());
		DeterministicGridBuilderDirector director = new DeterministicGridBuilderDirector(builder,
				false);
		director.construct();
		grid = builder.getResult();
		
		super.createGame(mode, grid);
	}
	
	public PlayerDataBase getPlayerDB() {
		return playerDB;
	}
	
	public GUIDataController getGuiDataCont() {
		return guiDataCont;
	}
	
	public MoveController getMoveCont() {
		return moveCont;
	}
	
	public PickUpItemController getPickUpCont() {
		return pickUpCont;
	}
	
	public UseItemController getUseItemCont() {
		return useItemCont;
	}
	
	public EndTurnController getEndTurnCont() {
		return endTurnCont;
	}
	
	public NewGameController getNewGameCont() {
		return newGameCont;
	}
	
	public Grid getGrid() {
		return grid;
	}
}
