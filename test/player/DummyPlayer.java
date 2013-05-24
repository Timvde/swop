package player;

import java.util.Collections;
import powerfailure.PowerFailure;
import square.Direction;
import square.NormalSquare;
import square.SquareContainer;

/**
 * A dummy implementation of {@link TronPlayer}. This implementation of player
 * focuses on the testing of the {@link PowerFailure} class. The method
 * {@link TronPlayer#damageByPowerFailure()} is overwritten for testing purposes.
 * 
 */
public class DummyPlayer extends TronPlayer {
	
	private boolean	damageByPowerFailure;
	private int		numberOfActionsSkipped;
	
	/**
	 * Create a new dummy player. The player will be set on a self created
	 * square without any neighbours.
	 */
	public DummyPlayer() {
		super(new PlayerDataBase(), new SquareContainer(
				Collections.<Direction, SquareContainer> emptyMap(), new NormalSquare()));
		this.damageByPowerFailure = false;
		this.numberOfActionsSkipped = 0;
	}
	
	@Override
	public void damageByPowerFailure() {
		this.damageByPowerFailure = true;
	}
	
	/**
	 * Returns whether this player is damaged by power failure
	 * 
	 * @return True if the player is damaged by power failure
	 */
	public boolean isDamagedByPowerFailure() {
		return damageByPowerFailure;
	}
	
	@Override
	public void skipNumberOfActions(int numberOfActionsToSkip) {
		numberOfActionsSkipped = numberOfActionsToSkip;
	}
	
	/**
	 * Returns the amount of actions the player has skipped
	 * 
	 * @return The amount of actions the player has skipped
	 */
	public int getNumberOfActionsSkipped() {
		return numberOfActionsSkipped;
	}
	
}
