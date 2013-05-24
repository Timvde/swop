package gui;

import item.UseArguments;
import java.util.Observable;
import java.util.Observer;
import square.Direction;

@SuppressWarnings("javadoc")
public class DummyGUI extends GUI implements Observer, ArgumentsHandler {
	
	
	public DummyGUI() {
		super(null, null, null, null, null, null);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// do nothing
	}
	
	@Override
	public void handleArguments(UseArguments<?> arguments) {
		arguments.setUserChoice(Direction.SOUTH);
	}
}
