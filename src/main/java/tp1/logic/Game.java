package tp1.logic;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import tp1.exceptions.GameLoadException;
import tp1.exceptions.GameModelException;
import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;
import tp1.logic.gameobjects.Box;
import tp1.logic.gameobjects.ExitDoor;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.GameObjectFactory;
import tp1.logic.gameobjects.Goomba;
import tp1.logic.gameobjects.Land;
import tp1.logic.gameobjects.Mario;
import tp1.logic.gameobjects.Mushroom;
import tp1.view.Messages;

public class Game implements GameModel, GameStatus, GameWorld {

	public static final int DIM_X = 30; // Numero de columnas
	public static final int DIM_Y = 15; // Numero de filas

	private int remainingTime;
	private int points;
	private int lives;

	private GameObjectContainer gameObjects;
	private int nLevel;
	private Mario mario;
	private boolean victory;
	private boolean gameOver;
	private boolean finished;

	private GameConfiguration fileLoader = null;

	public Game(int nLevel) {
		this.nLevel = nLevel;
		this.points = 0;
		this.lives = 3;
		this.victory = false;
		this.gameOver = false;
		this.finished = false;
		reset(); // Al iniciarse el juego, cargamos el nivel pasado por argumentos
	}

	// Metodos de GameModel

	@Override
	public boolean isFinished() {
		return finished || playerWins() || playerLoses();
	}

	@Override
	public void reset() {
		if (fileLoader == null) {
			this.remainingTime = 100;
			gameObjects = new GameObjectContainer();

			switch (nLevel) {
			case 0:
				initLevel0();
				break;
			case 1:
				initLevel1();
				break;
			case -1:
				initLevelMinus1();
				break;
			case 2:
				initLevel2();
				break;
			default:
				initLevel1();
				break;
			}
		} else {
			load(fileLoader);
		}
	}

	@Override
	public void reset(int level) {
		if (level == 0 || level == 1 || level == -1 || level == 2) {
			this.nLevel = level; // Solo se actualiza si existe el nivel
			this.fileLoader = null;
		}
		reset(); // Usamos la logica del reset sin parametros
	}

	@Override
	public void update() {
		if (remainingTime > 0) {
			remainingTime--;
		}
		gameObjects.update();
	}

	@Override
	public void exit() {
		finished = true;
	}

	@Override
	// Se añaden las acciones a una lista en la clase Mario
	public void addAction(Action act) {
		if (mario != null) {
			mario.addAction(act);
		}
	}

	@Override
	public void addObject(String[] objWords) throws OffBoardException, ObjectParseException {
		GameObject newObject = GameObjectFactory.parse(objWords, this);
		newObject.registerNew(this);
		addGameObject(newObject);
	}

	@Override
	public void save(String fileName) throws GameModelException {
		try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
			pw.println(this.toString());
		} catch (IOException e) {
			throw new GameModelException(Messages.ERROR_SAVING_FILE.formatted(fileName), e);
		}
	}

	@Override
	public void load(String filename) throws GameLoadException {
		GameConfiguration fileLoader = new FileGameConfiguration(filename, this);
		this.lives = fileLoader.numLives();
		this.points = fileLoader.points();
		load(fileLoader);
	}

	private void load(GameConfiguration loader) {
		this.remainingTime = loader.getRemainingTime();

		this.gameObjects = new GameObjectContainer();

		this.mario = loader.getMario();
		gameObjects.add(this.mario);

		for (GameObject obj : loader.getNPCObjects()) {
			gameObjects.add(obj);
		}

		this.fileLoader = loader;
	}

	// Metodos de GameWorld

	@Override
	public boolean isSolid(Position pos) {
		return gameObjects.isSolid(pos);
	}

	@Override
	public boolean isInBoard(Position pos) {
		return pos.getRow() >= 0 && pos.getRow() < DIM_Y && pos.getCol() >= 0 && pos.getCol() < DIM_X;
	}

	@Override
	public void addPoints(int pts) {
		points += pts;
	}

	@Override
	public void marioDies() {
		lives--;
		if (lives > 0) {
			reset(); // Reinicia el nivel actual
		} else {
			gameOver = true;
		}
	}

	@Override
	public void marioExited() {
		int add = remainingTime * 10;
		points += add;
		remainingTime = 0;
		victory = true;
	}

	@Override
	public void doInteractionsFrom(Mario mario) {
		gameObjects.doInteraction(mario);
	}

	@Override
	public void newMario(Mario m) {
		if (this.mario != null)
			gameObjects.remove(this.mario);
		this.mario = m;
	}

	@Override
	public void addGameObject(GameObject obj) {
		gameObjects.add(obj);
	}

	// Metodos de GameStatus

	@Override
	public String positionToString(int col, int row) {
		return gameObjects.positionToString(new Position(row, col));
	}

	@Override
	public boolean playerWins() {
		return victory;
	}

	@Override
	public boolean playerLoses() {
		return gameOver;
	}

	@Override
	public int remainingTime() {
		return remainingTime;
	}

	@Override
	public int points() {
		return points;
	}

	@Override
	public int numLives() {
		return lives;
	}

	// Otros metodos

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(remainingTime).append(" ").append(points).append(" ").append(lives).append("\n");
		gameObjects.serializeAll(sb);

		return sb.toString().trim();
	}

	private void initLevel0() {
		this.remainingTime = 100;

		// 1. Mapa
		gameObjects = new GameObjectContainer();
		for (int col = 0; col < 15; col++) {
			gameObjects.add(new Land(this, new Position(13, col)));
			gameObjects.add(new Land(this, new Position(14, col)));
		}

		gameObjects.add(new Land(this, new Position(Game.DIM_Y - 3, 9)));
		gameObjects.add(new Land(this, new Position(Game.DIM_Y - 3, 12)));
		for (int col = 17; col < Game.DIM_X; col++) {
			gameObjects.add(new Land(this, new Position(Game.DIM_Y - 2, col)));
			gameObjects.add(new Land(this, new Position(Game.DIM_Y - 1, col)));
		}

		gameObjects.add(new Land(this, new Position(9, 2)));
		gameObjects.add(new Land(this, new Position(9, 5)));
		gameObjects.add(new Land(this, new Position(9, 6)));
		gameObjects.add(new Land(this, new Position(9, 7)));
		gameObjects.add(new Land(this, new Position(5, 6)));

		// 2. Salto final
		int tamX = 8, tamY = 8;
		int posIniX = Game.DIM_X - 3 - tamX, posIniY = Game.DIM_Y - 3;

		for (int col = 0; col < tamX; col++) {
			for (int fila = 0; fila < col + 1; fila++) {
				gameObjects.add(new Land(this, new Position(posIniY - fila, posIniX + col)));
			}
		}

		gameObjects.add(new ExitDoor(this, new Position(Game.DIM_Y - 3, Game.DIM_X - 1)));

		// 3. Personajes
		this.mario = new Mario(this, new Position(Game.DIM_Y - 3, 0));
		gameObjects.add(this.mario);

		gameObjects.add(new Goomba(this, new Position(0, 19)));
	}

	private void initLevel1() {
		// Reutiliza todo lo de level0
		initLevel0();

		// Añade los Goombas extra del nivel 1
		gameObjects.add(new Goomba(this, new Position(4, 6)));
		gameObjects.add(new Goomba(this, new Position(10, 10)));
		gameObjects.add(new Goomba(this, new Position(12, 6)));
		gameObjects.add(new Goomba(this, new Position(12, 8)));
		gameObjects.add(new Goomba(this, new Position(12, 11)));
		gameObjects.add(new Goomba(this, new Position(12, 14)));
	}

	private void initLevelMinus1() {
		this.remainingTime = 100;
		this.lives = 3;
		this.points = 0;
		gameObjects = new GameObjectContainer();
	}

	private void initLevel2() {

		initLevel1();

		// Añade caja
		gameObjects.add(new Box(this, new Position(9, 4)));
		// Añade setas
		gameObjects.add(new Mushroom(this, new Position(12, 8)));
		gameObjects.add(new Mushroom(this, new Position(2, 20)));
	}

}
