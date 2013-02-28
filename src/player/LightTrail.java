package player;

import grid.Square;
import java.util.LinkedList;

/**
 * Every player leaves behind a lighttrail. A lighttrailpart remains active
 * during 2 actions of the player. The maximum length of the light trail is 3
 * squares.
 * 
 */
public class LightTrail implements ILightTrail {

	/**
	 * The maximum lenght of a lightrail (in squares). At this moment fixed.
	 */
	private static final int MAX_LENGTH = 3;

	private LinkedList<Square> lightTrailList;

	@Override
	public int getMaxLength() {
		return MAX_LENGTH;
	}
	
	@Override
	public int getLenght() {
		return this.lightTrailList.size();
	}
}
