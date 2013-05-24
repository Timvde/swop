package square;

import player.Player;

/**
 * A Startingposition property is set at build time of the grid and means the
 * square is a candidate to be the starting position of a {@link Player}.
 */
public class StartingPositionProperty implements Property {
	
	@Override
	public StartingPositionDecorator getDecorator(AbstractSquare square) {
		return new StartingPositionDecorator(square);
	}
	
}
