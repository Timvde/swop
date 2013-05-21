package game;

import item.EffectFactory;


public class CTFMode implements GameMode {

	private int	numberOfPlayers;

	public CTFMode(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	@Override
	public void checkWinner() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	@Override
	public EffectFactory getEffectFactory() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
