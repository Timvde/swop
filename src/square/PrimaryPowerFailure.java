package square;

import java.util.Random;

/**
 * This class represents a primary powerfailure. It keeps track of a secondary
 * and tertiary powerfailure, and is responsible for the rotation of the
 * secondary powerfailure.
 */
public class PrimaryPowerFailure extends PowerFailure {
	
	private SecondaryPowerFailure	secondaryPF;
	private TertiaryPowerFailure	tertiaryPF;
	
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
		Random rnd = new Random();
		if (rnd.nextInt(2) == 1)
			this.clockwiseRotation = true;
		else
			this.clockwiseRotation = false;
		
		this.rotationCounter = 2;
		this.timeToLive = 3;
		
		createSecondaryPowerFailure();
	}
	
	private void createSecondaryPowerFailure() {
		Direction randomDirection = Direction.getRandomDirection();
		ASquare secPFSquare = getSquare().getNeighbour(randomDirection);
		
		// Check if we selected a square that is part of the grid. If so,
		// create the secondary powerfailure, which results in the creation
		// of a tertiary powerfailure.
		if (secPFSquare != null) {
			this.secondaryPF = new SecondaryPowerFailure(secPFSquare);
			secPFSquare.addPowerFailure(this.secondaryPF);
			
			createTertiaryPowerFailure();
		}
	}
	
	private void createTertiaryPowerFailure() {
		if (this.secondaryPF == null) {
			throw new IllegalStateException(
					"The secondary powerfailure cannot be null when creating a tertiary powerfailure");
		}
		
		// Find the possible tertiary powerfailure directions as seen from the
		// secondary powerfailure
		Direction[] possibleTertiaryPFDirs = new Direction[3];
		
		Direction tertPFDirection1 = this.getSquare().getDirectionOfNeighbour(
				this.secondaryPF.getSquare());
		
		Direction[] tertPFDirections2and3 = Direction.getAdjacentDirections(tertPFDirection1);
		
		possibleTertiaryPFDirs[0] = tertPFDirection1;
		possibleTertiaryPFDirs[1] = tertPFDirections2and3[0];
		possibleTertiaryPFDirs[2] = tertPFDirections2and3[1];
		
		// Choose a random tertiary powerfailure position
		Random rnd = new Random();
		Direction tertiaryPFDirection = possibleTertiaryPFDirs[rnd.nextInt(3)];
		
		// Create the tertiary powerfailure, if it is located on the grid
		ASquare tertPFSquare = this.secondaryPF.getSquare().getNeighbour(tertiaryPFDirection);
		
		if (tertPFSquare != null) {
			this.tertiaryPF = new TertiaryPowerFailure(tertPFSquare);
			tertPFSquare.addPowerFailure(this.tertiaryPF);
		}
	}
	
	// TODO dit oproepen als 2 actions gebeurd zijn (rotationCounter nul is
	// geworden)
	private void rotateSecondaryPowerFailure() {
		// only do the rotation if there is a secondary powerfailure, and if it
		// is still alive.
		if (this.secondaryPF != null && this.secondaryPF.getTimeToLive() > 0) {
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
			
			// Move the secondary powerfailure, if the new sec PF square is part
			// of the grid.
			this.secondaryPF.getSquare().removePowerFailure(this.secondaryPF);
			this.secondaryPF.setSquare(null);
			
			if (nextSecPFSquare != null) {
				// TODO wat als sec niet op grid? die kan volgende plek dan niet berek.
				// TODO wat als 2 powerfailures op dezelfde plek?
				nextSecPFSquare.addPowerFailure(this.secondaryPF);
				this.secondaryPF.setSquare(nextSecPFSquare);
				
				// Create the new tertiary powerfailure
				createTertiaryPowerFailure();
			}
			
			rotationCounter = 2;
		}
	}
}
