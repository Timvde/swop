package game;

import java.util.Observable;

public interface GameMode {
	
	void checkWinner();
	
	int getNumberOfPlayers();
}
