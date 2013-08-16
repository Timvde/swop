package item.forcefieldgenerator;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import square.Direction;
import square.NormalSquare;
import square.Property;
import square.SquareContainer;


@SuppressWarnings("javadoc")
public class ForceFieldGeneratorTest {
	

	private ForceFieldGenerator	FFGen1;
	private ForceFieldGenerator	FFGen2;
	
	@Before
	public void setUp() throws Exception {
		FFGen1 = new ForceFieldGenerator();
		FFGen2 = new ForceFieldGenerator();
	}
	
	@Test
	public final void testUse() {
		
		// setup a long strait of squares
		SquareContainer container = new SquareContainer(
				Collections.<Direction, SquareContainer> emptyMap(), new NormalSquare());
		
		SquareContainer first = container;
		
		for (int i = 0; i < 1; i++) {
			Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
			neighbours.put(Direction.WEST, container);
			container = new SquareContainer(neighbours, new NormalSquare());
		}
		
		FFGen1.use(first, null);
		FFGen2.use(container, null);
		
		assertTrue(hasForceField(first) && hasForceField(container));
		
		ForceField FF = getForceField(first);
		
		FF.update();
		FF.update();
		FF.update();
		FF.update();
		
		assertSame(FF.getState(), ForceFieldState.ACTIVE);
	}
	
	private boolean hasForceField(SquareContainer square) {
		for (Property property : square.getProperties())
			if (property instanceof ForceField)
				return true;
		return false;
	} 
	
	private ForceField getForceField(SquareContainer square) {
		for (Property property : square.getProperties())
			if (property instanceof ForceField)
				return (ForceField) property;
		
		return null;
	} 
}
