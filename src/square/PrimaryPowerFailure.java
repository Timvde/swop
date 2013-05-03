package square;

import java.util.Random;
import player.TurnEvent;

/**
 * This class represents a primary powerfailure. It keeps track of a secondary
 * and tertiary powerfailure, and is responsible for the rotation of the
 * secondary powerfailure.
 */
public class PrimaryPowerFailure extends PowerFailure {
	
	private SecondaryPowerFailure	secondaryPF;
	private TertiaryPowerFailure	tertiaryPF;
	
	private static final int		ROTATION_COUNTER_MAX	= 2;
	private static final int		TIME_TO_LIVE			= 3;
	
	/** A counter that keeps track of when to rotate the secondary PF. */
	private int						rotationCounter;
	
	/** This boolean determines if the secondary PF rotates clockwise or not. */
	private boolean					clockwiseRotation;
	
	/**
	 * Create a primary powerfailure for a given square. This will also result
	 * in secondary and tertiary powerfailures.
	 * 
	 * @param square
	 *        The square that is impacted by this power failure.
	 */
	public PrimaryPowerFailure(ASquare square) {
		super(square);
		
		// Determine the secondary powerfailure rotation.
		this.clockwiseRotation = new Random().nextBoolean();
		
		this.rotationCounter = ROTATION_COUNTER_MAX;
		this.timeToLive = TIME_TO_LIVE;
		
		createSecondaryPowerFailure();
	}
	
	private void createSecondaryPowerFailure() {
		Direction randomDirection = Direction.getRandomDirection();
		ASquare secPFSquare = getSquare().getNeighbour(randomDirection);
		
		// Check if we selected a square that is part of the grid. If so,
		// check if there is not yet a powerfailure. If so, create the secondary
		// powerfailure, which results in the creation of a tertiary
		// powerfailure.
		if (secPFSquare != null && !secPFSquare.isWall() && !secPFSquare.hasPowerFailure()) {
			this.secondaryPF = new SecondaryPowerFailure(secPFSquare);
			secPFSquare.addPowerFailure(this.secondaryPF);
			
			createTertiaryPowerFailure();
		}
	}
	
	private void createTertiaryPowerFailure() {
		if (this.secondaryPF == null || this.secondaryPF.getSquare() == null) {
			throw new IllegalStateException(
					"The secondary powerfailure must exist when creating a tertiary powerfailure");
		}
		
		// Find the possible tertiary powerfailure directions as seen from the
		// secondary powerfailure
		Direction[] possibleTertiaryPFDirs = new Direction[3];
		
		Direction tertPFDirection1 = this.getSquare().getDirectionOfNeighbour(
				this.secondaryPF.getSquare());
		
		Direction[] tertPFDirections2and3 = tertPFDirection1.getAdjacentDirections();
		
		possibleTertiaryPFDirs[0] = tertPFDirection1;
		possibleTertiaryPFDirs[1] = tertPFDirections2and3[0];
		possibleTertiaryPFDirs[2] = tertPFDirections2and3[1];
		
		// Choose a random tertiary powerfailure position
		Random rnd = new Random();
		Direction tertiaryPFDirection = possibleTertiaryPFDirs[rnd.nextInt(3)];
		
		// Create the tertiary powerfailure, if it is located on the grid and
		// the
		// chosen square does not yet have a powerfailure.
		ASquare tertPFSquare = this.secondaryPF.getSquare().getNeighbour(tertiaryPFDirection);
		
		if (tertPFSquare != null && !tertPFSquare.isWall() && !tertPFSquare.hasPowerFailure()) {
			this.tertiaryPF = new TertiaryPowerFailure(tertPFSquare);
			tertPFSquare.addPowerFailure(this.tertiaryPF);
		}
	}
	
	private void rotateSecondaryPowerFailure() {
		// only do the rotation if there is a secondary powerfailure, and if it
		// is still located on a square.
		if (this.secondaryPF != null && this.secondaryPF.getTimeToLive() > 0
				&& this.secondaryPF.getSquare() != null) {
			// calculate the next direction in which the second powerfailure
			// lies:
			Direction currSecPFDir = this.getSquare().getDirectionOfNeighbour(
					this.secondaryPF.getSquare());
			Direction nextSecPFDir;
			
			if (this.clockwiseRotation)
				nextSecPFDir = currSecPFDir.getNextClockwiseDirection();
			else
				nextSecPFDir = currSecPFDir.getNextCounterClockwiseDirection();
			
			ASquare nextSecPFSquare = this.getSquare().getNeighbour(nextSecPFDir);
			
			// Remove the powerfailure from the old square
			this.secondaryPF.getSquare().removePowerFailure(this.secondaryPF);
			this.secondaryPF.setSquare(null);
			
			// Move the secondary powerfailure, if the new sec PF square is part
			// of the grid and it does not already contain a powerfailure.
			if (nextSecPFSquare != null && !nextSecPFSquare.isWall()
					&& !nextSecPFSquare.hasPowerFailure()) {
				nextSecPFSquare.addPowerFailure(this.secondaryPF);
				this.secondaryPF.setSquare(nextSecPFSquare);
				
				// Create the new tertiary powerfailure
				createTertiaryPowerFailure();
			}
		}
	}
	
	@Override
	void updateStatus(TurnEvent event) {
		// A turn was ended, so decrease the time to live for this
		// powerfailure
		if (event == TurnEvent.END_TURN) {
			decreaseTimeToLive();
			
			// Check if this primary powerfailure ended. If so, remove the
			// secondary powerfailure, if there is one.
			if (this.getSquare() == null) {
				if (this.secondaryPF != null && this.secondaryPF.getSquare() != null) {
					this.secondaryPF.getSquare().removePowerFailure(this.secondaryPF);
					this.secondaryPF.setSquare(null);
					this.secondaryPF = null;
				}
			}
		}
		
		// An action was ended. Update the rotation counter and rotate
		// the secondary powerfailure if necessary.
		if (event == TurnEvent.END_ACTION) {
			this.rotationCounter--;
			if (this.rotationCounter == 0) {
				rotateSecondaryPowerFailure();
				
				// Reset the rotation counter
				rotationCounter = ROTATION_COUNTER_MAX;
			}
		}
		
	}
}
