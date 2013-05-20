package gui;

import square.Direction;
import controllers.EndTurnController;
import controllers.GUIDataController;
import controllers.MoveController;
import controllers.NewGameController;
import controllers.PickUpItemController;
import controllers.UseItemController;


public class DummyGUI extends GUI {

	public DummyGUI() {
		super(null, null, null, null, null, null);
	}

	@Override
	public Direction getBasicDirection() {
		return Direction.SOUTH;
	}
}
