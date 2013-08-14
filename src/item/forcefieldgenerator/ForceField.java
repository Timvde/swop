package item.forcefieldgenerator;

import java.util.List;
import java.util.Random;
import square.AbstractSquare;
import square.AbstractSquareDecorator;
import square.Property;
import square.PropertyType;
import square.SquareContainer;

/**
 * A Force Field is placed on a square and has the following effect:
 * <ul>
 * <li>A player captured in an active force field can't move</li>
 * <li>Identity disks travelling through a force field will be destroyed</li>
 * <li>Each two actions, it flips state</li>
 * </ul>
 */
public class ForceField implements Property {
	
	private static final int		NUMBER_OF_ACTIONS_TO_SWITCH	= 2;
	private ForceFieldGenerator		generator1;
	private ForceFieldGenerator		generator2;
	private List<SquareContainer>	squares;
	private ForceFieldState			state;
	private int						counter;
	private int						ID;
	
	/**
	 * Create a Force Field.
	 * 
	 * @param generator1
	 *        The first {@link ForceFieldGenerator} associated with this force
	 *        field
	 * @param generator2
	 *        The second {@link ForceFieldGenerator} associated with this force
	 *        field
	 * @param squares
	 *        All the squares this force field affects
	 * @param delayedStart
	 *        A boolean to indicate that this force field needs a delayed start.
	 *        This is used when a player puts one down and needs time to run.
	 */
	public ForceField(ForceFieldGenerator generator1, ForceFieldGenerator generator2,
			List<SquareContainer> squares, boolean delayedStart) {
		System.out.println("creating new FF");
		this.generator1 = generator1;
		this.generator2 = generator2;
		this.squares = squares;
		this.state = ForceFieldState.INACTIVE;
		this.ID = new Random().nextInt(1000);
		
		if (delayedStart)
			this.counter = -2;
		else
			this.counter = 0;
		
		for (SquareContainer square : squares) {
			square.addProperty(this);
		}
	}
	
	/**
	 * Returns the {@link ForceFieldState} of this Force Field
	 * 
	 * @return The state of this force field
	 */
	public ForceFieldState getState() {
		return state;
	}
	
	@Override
	public AbstractSquareDecorator getDecorator(AbstractSquare square) {
		return new ForceFieldDecorator(square, this);
	}
	
	/**
	 * Update this forcefield that an action has happened.
	 */
	public void update() {
		System.out.println(this.ID + " got updated!");
		counter++;
		
		if (counter >= (NUMBER_OF_ACTIONS_TO_SWITCH) * this.squares.size()) {
			switchForceField();
			counter = 0;
		}
		
		// check if both generators are still there
		int numberOfGenerators = 0;
		for (AbstractSquare square : squares) {
			if (square.contains(generator1) || square.contains(generator2))
				numberOfGenerators++;
		}
		
		if (numberOfGenerators < 2)
			for (SquareContainer square : squares)
				square.removeProperty(this);
	}
	
	private void switchForceField() {
		this.state = state.switchState();
	}
	
}
