package item.identitydisk;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class IdentityDiskTest {
	
	private IdentityDisk	identityDisk;
	
	@Before
	public void setUp() throws Exception {
		identityDisk = new IdentityDisk();
	}
	
	@Test
	public final void testIdentityDisk() {
		// Not much to do here it seems. Let's put some other stuff here then. 
		//
		// An int, a char and a string walk into a bar and order some drinks. A
		// short while later, the int and char start hitting on the waitress who
		// gets very uncomfortable and walks away. The string walks up to the
		// waitress and says "You'll have to forgive them, they're primitive
		// types."
	}
	
	@Test
	public final void testUse() {
		fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testIsCarriable() {
		assertTrue(identityDisk.isCarriable());
	}
	
	@Test
	public final void testTeleportTo() {
		fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testDamageByPowerFailure() {
		fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testExecute() {
		fail("Not yet implemented"); // TODO
	}
	
}
