package item.identitydisk;


/**
 * Charged Identity disk can be launched by players on the grid. The disk will
 * then travel until it hits a player, wall or reaches the end of the board. An
 * identity disk cannot travel trough walls or outside of the grid. When either
 * of those cases crosses the path of this disk, the disk will stop before the
 * wall or the end of the grid. When an identity disk hits a player that player
 * loses its turn, and the disk will stop at the position of the player that has
 * been hit by the disk.
 * 
 */
public class ChargedIdentityDisk extends IdentityDisk {
	
	@Override
	public char toChar() {
		return 'c';
	}	
	
}
