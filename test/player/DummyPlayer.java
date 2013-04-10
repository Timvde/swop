package player;

import java.util.Collections;
import player.Player;
import square.ASquare;
import square.Direction;
import square.PowerFailure;
import square.Square;

/**
 * A dummy implementation of {@link Player}. This implementation of player
 * focuses on the testing of the {@link PowerFailure} class. The method
 * {@link Player#damageByPowerFailure()} is overwritten for testing purposes.
 * 
 */
public class DummyPlayer extends Player {
	
	private boolean	damageByPowerFailure;
	private int		numberOfActionsSkipped;
	
	/**
	 * Create a new dummy player. The player will be set on a self created
	 * square without any neighbours.
	 */
	public DummyPlayer() {
		super(new Square(Collections.<Direction, ASquare> emptyMap()), new PlayerDataBase());
		this.damageByPowerFailure = false;
		this.numberOfActionsSkipped = 0;
	}
	
	@Override
	public void damageByPowerFailure() {
		this.damageByPowerFailure = true;
	}
	
	/**
	 * returns whether this player is damaged by power failure
	 * 
	 * @return true if the player is damaged by power failure
	 */
	public boolean isDamagedByPowerFailure() {
		return damageByPowerFailure;
	}
	
	@Override
	public void skipNumberOfActions(int numberOfActionsToSkip) {
		numberOfActionsSkipped = numberOfActionsToSkip;
	}
	
	/**
	 * returns the amount of actions the player has skipped
	 * 
	 * @return the amount of actions the player has skipped
	 */
	public int getNumberOfActionsSkipped() {
		return numberOfActionsSkipped;
	}
	
}
