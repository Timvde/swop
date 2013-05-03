package properties.forcefield;

import java.util.List;
import square.AbstractSquare;
import item.forcefieldgenerator.ForceFieldGenerator;

/**
 * A Force Field is placed on a square and has the following effect:
 * <ul>
 * <li>A player captured in an active force field can't move</li>
 * <li>Identity disks travelling through a force field will be destroyed</li>
 * <li>Each two actions, it flips state</li>
 * </ul>
 */
public class ForceField {
	
	ForceFieldGenerator		generator1;
	ForceFieldGenerator		generator2;
	List<AbstractSquare>	squares;
	ForceFieldState			state;
	
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
			List<AbstractSquare> squares) {
		this.generator1 = generator1;
		this.generator2 = generator2;
		this.squares = squares;
		this.state = ForceFieldState.ACTIVE;
	}
	
	/**
	 * Returns the {@link ForceFieldState} of this Force Field
	 * 
	 * @return The state of this force field
	 */
	public ForceFieldState getState() {
		return state;
	}
}
