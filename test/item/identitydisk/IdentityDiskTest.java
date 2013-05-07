package item.identitydisk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import square.AbstractSquare;
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
		
		identityDisk.setDirection(Direction.EAST);
		identityDisk.use(first);
		
		assertEquals(identityDisk, container.getAllItems().get(0));
	}
	
	@Test
	public final void testUse_ItemCannotMove() {
		identityDisk.setDirection(Direction.EAST);
		identityDisk.use(square);
		
		assertEquals(identityDisk, square.getAllItems().get(0));
	}
	
	@Test(expected = IllegalStateException.class)
	public final void testUse_noDirectionSet() {
		identityDisk.use(new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				new NormalSquare()));
	}
	
	@Test(expected = IllegalStateException.class)
	public final void testUse_useItemTwice() {
		identityDisk.setDirection(Direction.EAST);
		identityDisk.use(square);
		identityDisk.use(square);
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
		
		identityDisk.setDirection(Direction.EAST);
		identityDisk.use(first);
		
		assertEquals(identityDisk, goal.getAllItems().get(0));
		assertTrue(square.getAllItems().isEmpty());
	}
	
	@Test
	public final void testIsCarriable() {
		assertTrue(identityDisk.isCarriable());
	}
	
	@Test
	public final void testTeleportTo() {
		// TODO: fix this
	}
	
	@Test(expected = IllegalArgumentException.class)
	public final void testTeleportTo_nullArgument() {
		// TODO: fix this
	}
	
	@Test
	public final void testExecute() {
		// not much to do here
		// because we write such good code ...
		// *sarcasm*
	}
}
