package tp1.logic;

public interface GameStatus {

	public int points();

	public int numLives();

	public int remainingTime();

	public String positionToString(int col, int row);

	public boolean playerWins();

	public boolean playerLoses();
}
