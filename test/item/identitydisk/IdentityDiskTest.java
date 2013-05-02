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
import square.WallPart;

@SuppressWarnings("javadoc")
public class IdentityDiskTest {
	
	private IdentityDisk	identityDisk;
	
	@Before
	public void setUp() throws Exception {
		identityDisk = new ChargedIdentityDisk();
	}
	
	@Test
	public final void testUse() {
		
		// setup a long strait of squares
		NormalSquare square = new NormalSquare(Collections.<Direction, AbstractSquare> emptyMap());
		NormalSquare first = square;
		for (int i = 0; i < 10; i++) {
			Map<Direction, AbstractSquare> neighbours = new HashMap<Direction, AbstractSquare>();
			neighbours.put(Direction.WEST, square);
			square = new NormalSquare(neighbours);
		}
		
		identityDisk.setDirection(Direction.EAST);
		identityDisk.use(first);
		
		assertEquals(identityDisk, square.getAllItems().get(0));
	}
	
	@Test
	public final void testUse_ItemCannotMove() {
		NormalSquare square = new NormalSquare(Collections.<Direction, AbstractSquare> emptyMap());
		identityDisk.setDirection(Direction.EAST);
		identityDisk.use(square);
		
		assertEquals(identityDisk, square.getAllItems().get(0));
	}
	
	@Test(expected = IllegalStateException.class)
	public final void testUse_noDirectionSet() {
		identityDisk.use(new NormalSquare(Collections.<Direction, AbstractSquare> emptyMap()));
	}
	
	@Test(expected = IllegalStateException.class)
	public final void testUse_useItemTwice() {
		NormalSquare square = new NormalSquare(Collections.<Direction, AbstractSquare> emptyMap());
		identityDisk.setDirection(Direction.EAST);
		identityDisk.use(square);
		identityDisk.use(square);
	}
	
	@Test
	public final void testUse_hitWall() {
		// setup a long strait of squares
		AbstractSquare square = new NormalSquare(Collections.<Direction, ASquare> emptyMap());
		AbstractSquare first = square;
		AbstractSquare goal = null;
		for (int i = 0; i < 10; i++) {
			Map<Direction, AbstractSquare> neighbours = new HashMap<Direction, ASquare>();
			neighbours.put(Direction.WEST, square);
			
			if (i == 7) {
				goal = square;
				square = new WallPart(neighbours);
				break;
			}
			square = new NormalSquare(neighbours);
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
		NormalSquare square = new NormalSquare(Collections.<Direction, AbstractSquare> emptyMap());
		identityDisk.teleportTo(square);
		assertEquals(identityDisk, square.getAllItems().get(0));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public final void testTeleportTo_nullArgument() {
		identityDisk.teleportTo(null);
	}
	
	@Test
	public final void testExecute() {
		// not much to do here 
		// because we write such good code ... 
		// *sarcasm*  
	}	
}