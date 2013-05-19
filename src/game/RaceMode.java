package game;

public class RaceMode implements GameMode {
	
	private static final int	NUMBER_OF_PLAYERS_IN_RACE_MODE	= 2;
	
	@Override
	public void checkWinner() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getNumberOfPlayers() {
		return NUMBER_OF_PLAYERS_IN_RACE_MODE;
	}
}
