package square;

import item.IItem;

/**
 * A special kind of {@link Square}. Where a player can stand on when the game
 * starts. The starting position of a player cannot contain an item.
 */
public class PlayerStartingPosition extends NormalSquare {
	
	@Override
	public boolean canBeAdded(IItem item) {
		// The starting position of a player cannot contain an item
		return false;
	}
	
	@Override
	public boolean isStartingPosition() {
		return true;
	}
	
	/*
	 * TODO Volgens mij moeten we de playerstartpositions naar een decorator
	 * pattern herschrijven:
	 * 
	 * - De startpos op het grid zijn at build time maar potentiële startpos -
	 * Later (als de game mode en het aantal players gekozen is), worden de
	 * eerste n posities echte playerstarts
	 * 
	 * Een decorator zou dus toelaten om alle potentiële startpos te wrappen en
	 * later eventueel een aantal te unwrappen
	 */
}
