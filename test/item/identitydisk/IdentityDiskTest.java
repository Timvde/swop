package item.identitydisk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import item.UseArguments;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import square.Direction;
import square.NormalSquare;
import square.SquareContainer;
import square.WallPart;

@SuppressWarnings("javadoc")
public class IdentityDiskTest {
	
	private IdentityDisk	identityDisk;
	private SquareContainer	square;
	
	@Before
	public void setUp() throws Exception {
		identityDisk = new ChargedIdentityDisk();
		square = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare());
	}
	
	@Test
	public final void testUse() {
		
		// setup a long strait of squares
		SquareContainer container = new SquareContainer(
				Collections.<Direction, SquareContainer> emptyMap(), new NormalSquare());
		SquareContainer first = container;
		for (int i = 0; i < 10; i++) {
			Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
			neighbours.put(Direction.WEST, container);
			container = new SquareContainer(neighbours, new NormalSquare());
		}
		
		// USE THE ID TO THE EAST
		DummyIDArgumentsHandler idHandler = new DummyIDArgumentsHandler();
		idHandler.setChoice(2);
		UseArguments<?> arguments = identityDisk.getUseArguments();
		if (arguments != null)
			idHandler.handleArguments(arguments);
		identityDisk.use(first, arguments);
		
		assertEquals(identityDisk, container.getAllItems().get(0));
	}
	
	@Test
	public final void testUse_ItemCannotMove() {
		// USE THE ID TO THE EAST
		DummyIDArgumentsHandler idHandler = new DummyIDArgumentsHandler();
		idHandler.setChoice(2);
		UseArguments<?> arguments = identityDisk.getUseArguments();
		if (arguments != null)
			idHandler.handleArguments(arguments);
		identityDisk.use(square, arguments);
		
		assertEquals(identityDisk, square.getAllItems().get(0));
	}
	
	@Test(expected = IllegalStateException.class)
	public final void testUse_useItemNullSquare() {
		// USE THE ID TO THE EAST
		DummyIDArgumentsHandler idHandler = new DummyIDArgumentsHandler();
		idHandler.setChoice(2);
		UseArguments<?> arguments = identityDisk.getUseArguments();
		if (arguments != null)
			idHandler.handleArguments(arguments);
		identityDisk.use(null, arguments);
	}
	
	@Test
	public final void testUse_hitWall() {
		// setup a long strait of squares
		SquareContainer first = square;
		SquareContainer goal = null;
		for (int i = 0; i < 10; i++) {
			Map<Direction, SquareContainer> neighbours = new HashMap<Direction, SquareContainer>();
			neighbours.put(Direction.WEST, square);
			
			if (i == 7) {
				goal = square;
				square = new SquareContainer(neighbours, new WallPart());
				continue;
			}
			square = new SquareContainer(neighbours, new NormalSquare());
		}
		
		// USE THE ID TO THE EAST
		DummyIDArgumentsHandler idHandler = new DummyIDArgumentsHandler();
		idHandler.setChoice(2);
		UseArguments<?> arguments = identityDisk.getUseArguments();
		if (arguments != null)
			idHandler.handleArguments(arguments);
		identityDisk.use(first, arguments);
		
		assertEquals(identityDisk, goal.getAllItems().get(0));
		assertTrue(square.getAllItems().isEmpty());
	}
	
	@Test
	public final void testIsCarriable() {
		assertTrue(identityDisk.isCarriable());
	}
	
	@Test
	public final void testExecute() {
		// not much to do here
		// because we write such good code ...
		// *sarcasm*
	}
}
