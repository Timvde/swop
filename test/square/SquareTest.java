package square;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import item.Item;
import item.identitydisk.IdentityDisk;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import player.DummyPlayer;
import player.IPlayer;
import player.PlayerDataBase;

@SuppressWarnings("javadoc")
public class SquareTest {
	
	private static Square	square;
	private static IPlayer playerOnSquare;
	
	@Before
	public void setUp() {
		square = new Square(Collections.<Direction, ASquare> emptyMap());
		
		
		TronGridBuilder builder = new TronGridBuilder();
		new DeterministicGridBuilderDirector(builder, false).construct();
		PlayerDataBase db = new PlayerDataBase();
		db.createNewDB(builder.getResult().getAllStartingPositions());
		
		playerOnSquare = db.getCurrentPlayer();
		square.addPlayer(db.getCurrentPlayer());
	}
	
	@Test
	public void testAddItem() {
		Item item = new LightGrenade();
		square.addItem(item);
		assertTrue(square.getCarryableItems().contains(item));
		assertTrue(square.contains(item));
		square.remove(item);
		assertEquals(square.getCarryableItems().size(), 0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddItem_nullArgument() {
		assertFalse(square.canBeAdded((Item) null));
		square.addItem(null);
	}
	
	@Test
	public void testAddItem_effectExecuted() {
		// TODO test whether a square automatically adds an effect if an item is
		// added to the square
	}
	
	@Test
	public void testRemove() {		
		// place some stuff on the square
		LightGrenade lightGrenade = new LightGrenade();
		square.addItem(lightGrenade);
		
		// test if null removes anything (it shouldn't)
		square.remove(null);
		assertTrue(square.contains(playerOnSquare));
		assertTrue(square.contains(lightGrenade));
		
		// remove the player from the square
		square.remove(playerOnSquare);
		assertFalse(square.contains(playerOnSquare));
		assertTrue(square.contains(lightGrenade));
		assertFalse(square.hasPlayer());
		
		// remove the light grenade from the square
		square.remove(lightGrenade);
		assertFalse(square.contains(lightGrenade));
		assertFalse(square.contains(playerOnSquare));
		assertFalse(square.hasPlayer());
	}
	
	@Test
	public void testContains() {
		// create some stuff but do not place it on the square
		IPlayer player = new DummyPlayer();
		LightGrenade lightGrenade = new LightGrenade();
		
		// test if the square contains anything (i hope not)
		assertFalse(square.contains(null));
		assertFalse(square.contains(player));
		assertFalse(square.contains(lightGrenade));
		
		// add a player to the square
		square.removePlayer();
		square.addPlayer(player);
		assertTrue(square.contains(player));
		assertFalse(square.contains(lightGrenade));
		assertFalse(square.contains(null));
		
		// add an item to the square
		square.addItem(lightGrenade);
		assertTrue(square.contains(lightGrenade));
		assertTrue(square.contains(player));
		assertTrue(square.hasPlayer());
	}
	
	@Test
	public void testPlayer() {
		assertEquals(square.getPlayer(), playerOnSquare);
		square.remove(playerOnSquare);
		assertEquals(square.getPlayer(), null);
	}
	
	@Test
	public void testLightTrail() {
		assertFalse(square.hasLightTrail());
		square.placeLightTrail();
		assertTrue(square.hasLightTrail());
		square.removeLightTrail();
		assertFalse(square.hasLightTrail());
	}
	
	@Test
	public void testHasPowerFailure() {
		// check if we start clean
		assertFalse(square.hasPowerFailure());
		
		// setup (add a neighbour and create a powerfailure)
		Map<Direction, ASquare> neighbours = new HashMap<Direction, ASquare>();
		neighbours.put(Direction.NORTH, square);
		Square sq = new Square(neighbours);
		PrimaryPowerFailure powerFailure = new PrimaryPowerFailure(sq);
		
		// test if both squares suffer from power failures
		assertTrue(square.hasPowerFailure());
		assertTrue(sq.hasPowerFailure());
		assertEquals(powerFailure, sq.getPowerFailure());
		assertEquals(powerFailure, square.getPowerFailure());
		
		// make the power failure run out of lives
		powerFailure.decreaseTimeToLive();
		powerFailure.decreaseTimeToLive();
		powerFailure.decreaseTimeToLive();
		
		// check the squares again
		assertFalse(square.hasPowerFailure());
		assertFalse(sq.hasPowerFailure());
	}
	
	@Test
	public void testRemovePowerFailure() {
		PrimaryPowerFailure powerFailure = new PrimaryPowerFailure(square);
		
		// null arguments should not be remove
		square.removePowerFailure(null);
		assertEquals(powerFailure, square.getPowerFailure());
		
		// other power failures should also not remove the current power failure
		square.removePowerFailure(new PrimaryPowerFailure(new Square(Collections
				.<Direction, ASquare> emptyMap())));
		assertEquals(powerFailure, square.getPowerFailure());
		
		// default case ... not much to say
		square.removePowerFailure(powerFailure);
		assertEquals(null, square.getPowerFailure());
		
		// if there is no power failure, nothing should happen by this
		square.removePowerFailure(powerFailure);
		assertEquals(null, square.getPowerFailure());
		
		// a last hopeless try
		square.removePowerFailure(null);
		assertEquals(null, square.getPowerFailure());
	}
	
	@Test
	public void testGetCarryableItems() {
		// add some items to the square
		LightGrenade lightGrenade = new LightGrenade();
		Teleporter teleporter = new Teleporter(null, square);
		IdentityDisk identityDisk = new UnchargedIdentityDisk();
		
		square.addItem(lightGrenade);
		square.addItem(identityDisk);
		square.addItem(teleporter);
		
		// test if the returned array is correct
		assertEquals(2, square.getCarryableItems().size());
		assertTrue(square.getCarryableItems().contains(lightGrenade));
		assertTrue(square.getCarryableItems().contains(identityDisk));
	}
	
	@Test
	public void testGetCarryableItems_encapsulation() {
		// add some items to the square
		LightGrenade lightGrenade = new LightGrenade();
		Teleporter teleporter = new Teleporter(null, square);
		IdentityDisk identityDisk = new UnchargedIdentityDisk();
		
		square.addItem(lightGrenade);
		square.addItem(identityDisk);
		
		// try and add a non carryable item to the list
		square.getCarryableItems().add(teleporter);
		// test if it was internally added
		assertFalse(square.contains(teleporter));
		assertFalse(square.getCarryableItems().contains(teleporter));
	}
	
	@Test
	public void testPickUpItem() {
		// add some items to the square
		LightGrenade lightGrenade = new LightGrenade();
		Teleporter teleporter = new Teleporter(null, square);
		IdentityDisk identityDisk = new UnchargedIdentityDisk();
		
		square.addItem(lightGrenade);
		square.addItem(identityDisk);
		square.addItem(teleporter);
		
		// pick up the light grenade
		assertEquals(lightGrenade, square.pickupItem(lightGrenade.getId()));
		assertFalse(square.contains(lightGrenade));
		assertTrue(square.contains(teleporter));
		assertTrue(square.contains(identityDisk));
		
		// pick up the identity disk
		assertEquals(identityDisk, square.pickupItem(identityDisk.getId()));
		assertFalse(square.contains(lightGrenade));
		assertFalse(square.contains(identityDisk));
		assertTrue(square.contains(teleporter));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPickUpItem_itemDoesNotExist() {
		square.pickupItem(0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPickUpItem_itemIsNotCarryable() {
		Teleporter teleporter = new Teleporter(null, square);
		square.addItem(teleporter);
		square.pickupItem(teleporter.getId());
	}
	
	@Test
	public void testAddPlayer() {
		// setup (make a new player and make sure he is not placed on the
		// square)
		square.remove(playerOnSquare);
		
		assertFalse(square.hasPlayer());
		assertFalse(square.contains(playerOnSquare));
		
		// add a player to the square
		square.addPlayer(playerOnSquare);
		
		// test if the player is placed on the square
		assertTrue(square.contains(playerOnSquare));
		assertTrue(square.hasPlayer());
	}
	
	@Test
	public void testAddPlayer_executeEffect() {
		// make a player to test with
		DummyPlayer player = new DummyPlayer();
		Square newSquare = new Square(Collections.<Direction, ASquare> emptyMap());
		
		// create a square with power failure
		new PrimaryPowerFailure(newSquare);
		
		// start testing
		// when the player moves onto the square, he will suffer from this. He
		// will be (hopefully) affected by a power failure.
		
		newSquare.addPlayer(player);
		assertTrue(player.isDamagedByPowerFailure());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddPlayer_allreadyPlayerOnSquare() {
		assertFalse(square.canAddPlayer());
		square.addPlayer(new DummyPlayer());
	}
}
