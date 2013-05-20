package item.forcefieldgenerator;

import java.util.List;
import square.AbstractSquare;
import square.AbstractSquareDecorator;
import square.Property;
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
	
	private ForceFieldGenerator		generator1;
	private ForceFieldGenerator		generator2;
	private List<SquareContainer>	squares;
	private ForceFieldState			state;
	private int						counter;
	
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
	 */
	public ForceField(ForceFieldGenerator generator1, ForceFieldGenerator generator2,
			List<SquareContainer> squares) {
		this.generator1 = generator1;
		this.generator2 = generator2;
		this.squares = squares;
		this.state = ForceFieldState.ACTIVE;
		this.counter = 0;
		
		for (SquareContainer square : squares)
			square.addProperty(this);
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
		counter++;
		
		if (counter >= squares.size() * 2) {
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
