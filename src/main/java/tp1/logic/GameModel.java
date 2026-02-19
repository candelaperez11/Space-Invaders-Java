package tp1.logic;

import tp1.exceptions.GameLoadException;
import tp1.exceptions.GameModelException;
import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;

public interface GameModel {

	public boolean isFinished();

	public void update();

	public void reset();

	public void reset(int level);

	public void exit();

	public void addAction(Action act);

	public void addObject(String[] objWords) throws OffBoardException, ObjectParseException;

	public void save(String fileName) throws GameModelException;

	public void load(String fileName) throws GameLoadException;

}
