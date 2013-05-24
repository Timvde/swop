package gui;

import item.UseArguments;

/**
 * This interface indicates a class implementing it is able to handle
 * UseArguments to communicate with the player.
 */
public interface ArgumentsHandler {
	
	/**
	 * This method communicates with the user and adds info delivered by the
	 * user to the arguments.
	 * 
	 * @param arguments
	 *        The use arguments
	 */
	public void handleArguments(UseArguments<?> arguments);
}
