package square;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import effects.RaceEffectFactory;
import grid.builder.DeterministicGridBuilderDirector;
import grid.builder.TronGridBuilder;
import item.DummyEffectFactory;
import item.Item;
import item.identitydisk.IdentityDisk;
import item.identitydisk.UnchargedIdentityDisk;
import item.lightgrenade.LightGrenade;
import item.teleporter.Teleporter;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import player.DummyPlayer;
import player.Player;
import player.PlayerDataBase;

@SuppressWarnings("javadoc")
public class SquareTest {
	
	private static NormalSquare		square;
	private static SquareContainer	squareContainer;
	private static Player			playerOnSquare;
	
	@Before
	public void setUp() {
		square = new NormalSquare();
		squareContainer = new SquareContainer(Collections.<Direction, SquareContainer> emptyMap(),
				square);
		
		TronGridBuilder builder = new TronGridBuilder(new DummyEffectFactory());
		new DeterministicGridBuilderDirector(builder, false).construct();
		PlayerDataBase db = new PlayerDataBase();
		db.createNewDB(builder.getResult().getAllStartingPositions());
		
		playerOnSquare = db.getCurrentPlayer();
		squareContainer.addPlayer(db.getCurrentPlayer());
	}
	
	@Test
	public void testAddItem() {
		Item item = new LightGrenade(new DummyEffectFactory());
		squareContainer.addItem(item);
		assertTrue(square.getCarryableItems().contains(item));
		assertTrue(square.contains(item));
		squareContainer.remove(item);
		assertEquals(square.getCarryableItems().size(), 0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddItem_nullArgument() {
		assertFalse(square.canBeAdded((Item) null));
		squareContainer.addItem(null);
	}
	
	@Test
	public void testAddItem_effectExecuted() {
		// TODO test whether a square automatically adds an effect if an item is
		// added to the square
	}
	
	@Test
	public void testRemove() {
		// place some stuff on the square
		LightGrenade lightGrenade = new LightGrenade(new DummyEffectFactory());
		squareContainer.addItem(lightGrenade);
		
		// test if null removes anything (it shouldn't)
		squareContainer.remove(null);
		assertTrue(square.contains(playerOnSquare));
		assertTrue(square.contains(lightGrenade));
		
		// remove the player from the square
		squareContainer.remove(playerOnSquare);
		assertFalse(square.contains(playerOnSquare));
		assertTrue(square.contains(lightGrenade));
		assertFalse(square.hasPlayer());
		
		// remove the light grenade from the square
		squareContainer.remove(lightGrenade);
		assertFalse(square.contains(lightGrenade));
		assertFalse(square.contains(playerOnSquare));
		assertFalse(square.hasPlayer());
	}
	
	@Test
	public void testContains() {
		// create some stuff but do not place it on the square
		Player player = new DummyPlayer();
		LightGrenade lightGrenade = new LightGrenade(new DummyEffectFactory());
		
		// test if the square contains anything (i hope not)
		assertFalse(square.contains(null));
		assertFalse(square.contains(player));
		assertFalse(square.contains(lightGrenade));
		
		// add a player to the square
		squareContainer.remove(playerOnSquare);
		squareContainer.addPlayer(player);
		assertTrue(square.contains(player));
		assertFalse(square.contains(lightGrenade));
		assertFalse(square.contains(null));
		
		// add an item to the square
		squareContainer.addItem(lightGrenade);
		assertTrue(square.contains(lightGrenade));
		assertTrue(square.contains(player));
		assertTrue(square.hasPlayer());
	}
	
	@Test
	public void testPlayer() {
		assertEquals(square.getPlayer(), playerOnSquare);
		squareContainer.remove(playerOnSquare);
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
	public void testGetCarryableItems() {
		// add some items to the square
		LightGrenade lightGrenade = new LightGrenade(new DummyEffectFactory());
		Teleporter teleporter = new Teleporter(null, squareContainer, new RaceEffectFactory());
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
		LightGrenade lightGrenade = new LightGrenade(new DummyEffectFactory());
		Teleporter teleporter = new Teleporter(null, squareContainer, new RaceEffectFactory());
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
		LightGrenade lightGrenade = new LightGrenade(new DummyEffectFactory());
		Teleporter teleporter = new Teleporter(null, squareContainer, new RaceEffectFactory());
		IdentityDisk identityDisk = new UnchargedIdentityDisk();
		
		squareContainer.addItem(lightGrenade);
		squareContainer.addItem(identityDisk);
		squareContainer.addItem(teleporter);
		
		// pick up the light grenade
		assertEquals(lightGrenade, squareContainer.pickupItem(lightGrenade.getId()));
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
		squareContainer.pickupItem(0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPickUpItem_itemIsNotCarryable() {
		Teleporter teleporter = new Teleporter(null, squareContainer, new RaceEffectFactory());
		squareContainer.addItem(teleporter);
		squareContainer.pickupItem(teleporter.getId());
	}
	
	@Test
	public void testAddPlayer() {
		/*
		 * setup (make a new player and make sure he is not placed on the
		 * square)
		 */
		squareContainer.remove(playerOnSquare);
		
		assertFalse(square.hasPlayer());
		assertFalse(square.contains(playerOnSquare));
		
		// add a player to the square
		squareContainer.addPlayer(playerOnSquare);
		
		// test if the player is placed on the square
		assertTrue(square.contains(playerOnSquare));
		assertTrue(square.hasPlayer());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddPlayer_allreadyPlayerOnSquare() {
		assertFalse(square.canAddPlayer());
		square.addPlayer(new DummyPlayer());
	}
}
