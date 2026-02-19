package tp1.logic;

import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.Mario;

public interface GameWorld {

	public boolean isSolid(Position pos);

	public boolean isInBoard(Position pos);

	public void addPoints(int pts);

	public void marioDies();

	public void marioExited();

	public void doInteractionsFrom(Mario mario);

	public void newMario(Mario m);

	public void addGameObject(GameObject obj);

}
