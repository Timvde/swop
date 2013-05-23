package gui;

import square.Direction;


public class DummyGUI extends GUI {

	public DummyGUI() {
		super(null, null, null, null, null, null);
	}

	@Override
	public Direction getBasicDirection() {
		return Direction.SOUTH;
	}
}
