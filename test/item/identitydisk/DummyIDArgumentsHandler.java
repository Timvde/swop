package item.identitydisk;

import item.UseArguments;
import gui.ArgumentsHandler;

/**
 * A dummy arguments handler for manipulating items during tests.
 * @author tom
 *
 */
public class DummyIDArgumentsHandler implements ArgumentsHandler {
	
	private int userChoice;
	
	@SuppressWarnings("javadoc")
	public DummyIDArgumentsHandler() {
		this.userChoice = 0; // default
	}

	@SuppressWarnings("javadoc")
	public void setChoice(int num) {
		this.userChoice = num;
	}
	
	@Override
	public void handleArguments(UseArguments<?> arguments) {
		arguments.setUserChoice(this.userChoice);
	}
	
}
