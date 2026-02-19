package tp1.logic.gameobjects;

import tp1.exceptions.ActionParseException;
import tp1.exceptions.GameParseException;
import tp1.logic.Action;
import tp1.logic.ActionList;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Mario extends MovingObject {

	private boolean isBig; // Para saber si Mario es grande
	private final ActionList actions;

	// Constructor de mario por defecto
	public Mario(GameWorld game, Position pos) {
		super(game, pos, Action.RIGHT);
		this.isBig = true;
		this.actions = new ActionList();
	}

	// Constructor de mario creado con direccion y tamaño dados
	public Mario(GameWorld game, Position pos, Action dir, boolean isBig) {
		super(game, pos, dir);
		this.isBig = isBig;
		this.actions = new ActionList();
	}

	public Mario() {
		super();
		this.actions = new ActionList();
	}

	@Override
	public void update() {
		if (!isAlive())
			return;

		isFalling = false;

		// Variable que comprueba si se mueve con acciones
		boolean hasMoved = false;

		// Mientras haya acciones pendientes
		while (actions.hasActions()) {
			Action act = actions.nextAction();
			if (executeAction(act)) {
				hasMoved = true;
				// Tras cada movimiento de mario en la lista de acciones, se deben comprobar las
				// interacciones
				checkInteractions();
			}

		}
		actions.clear();

		// Si no se ha movido, se hace movimiento automatico
		if (!hasMoved) {
			walkOrFall();
		}
	}

	public void checkInteractions() {
		game.doInteractionsFrom(this);
	}

	public void addAction(Action act) {
		actions.add(act);
	}

	protected boolean executeAction(Action act) {
		Position nextPos;

		switch (act) {
		case LEFT:
			direction = Action.LEFT;
			nextPos = pos.left();
			if (canOccupy(nextPos)) {
				pos = nextPos;
				return true;
			} else {
				direction = Action.RIGHT;
				return false;
			}

		case RIGHT:
			direction = Action.RIGHT;
			nextPos = pos.right();
			if (canOccupy(nextPos)) {
				pos = nextPos;
				return true;
			} else {
				direction = Action.LEFT;
				return false;
			}

		case UP:
			nextPos = pos.up();
			if (canOccupy(nextPos)) {
				pos = nextPos;
				return true;
			}
			break;

		case DOWN:
			if (isInAir()) {
				while (isInAir()) {
					fall();
					if (!isAlive())
						break;
					checkInteractions();
				}
				return true;
			} else {
				direction = Action.STOP;
			}
			break;

		case STOP:
			direction = Action.STOP;
			break;
		}
		return false;
	}

	// Comprobar si Mario puede moverse, siendo big o normal
	protected boolean canOccupy(Position pos) {
		// Mario normal ocupa 1 celda
		if (!isBig)
			return game.isInBoard(pos) && !game.isSolid(pos);

		// Mario grande ocupa 2 celdas, pos y pos.up()
		Position upper = pos.up();
		return game.isInBoard(pos) && game.isInBoard(upper) && !game.isSolid(pos) && !game.isSolid(upper);
	}

	@Override
	public boolean interactWith(GameItem other) {

		boolean collide;

		if (other.isSolid()) {
			if (!isBig) {
				collide = other.isInPosition(pos.up());
			} else {
				collide = other.isInPosition(pos.up()) || other.isInPosition(pos.up().up());
			}
		}

		else {
			if (!isBig) {
				collide = other.isInPosition(pos);
			} else {
				collide = other.isInPosition(pos) || other.isInPosition(pos.up());
			}
		}

		// Si no estan en la misma posicion, no hay colision
		if (!collide)
			return false;

		// Avisar al goomba para que reaccione
		return other.receiveInteraction(this);

	}

	@Override
	public boolean receiveInteraction(Goomba g) {
		return true;
	}

	@Override
	public boolean receiveInteraction(Mushroom mu) {
		return true;
	}

	@Override
	public boolean receiveInteraction(Box b) {
		return true;
	}

	@Override
	public boolean receiveInteraction(ExitDoor d) {
		return true;
	}

	@Override
	public void die() {
		super.die();
		game.marioDies();
	}

	public boolean isBig() {
		return isBig;
	}

	// Mario crece por seta
	public void powerUp() {
		isBig = true;
	}

	// Mario recibe daño de un Goomba
	public void hitByEnemy() {
		if (!isBig) {
			die();
		} else {
			isBig = false;
		}
	}

	@Override
	public boolean isInPosition(Position p) {
		// Mario pequeño ocupa una sola casilla
		if (!isBig) {
			return pos.equals(p);
		}
		// Mario grande ocupa dos: la suya y la de arriba
		return pos.equals(p) || pos.up().equals(p);
	}

	@Override
	public String getIcon() {
		if (direction == Action.STOP)
			return Messages.MARIO_STOP;
		else {
			if (direction == Action.RIGHT)
				return Messages.MARIO_RIGHT;
			else
				return Messages.MARIO_LEFT;
		}
	}

	@Override
	public String getName() {
		return Messages.MARIO_NAME;
	}

	@Override
	public String getSymbol() {
		return Messages.MARIO_SYMBOL;
	}

	@Override
	public Mario parse(String[] words, GameWorld game) throws GameParseException {
		if (!(words.length >= 2 && (words[1].equalsIgnoreCase(getName()) || words[1].equalsIgnoreCase(getSymbol()))))
			return null;

		if (words.length > 4) {
			throw new GameParseException(Messages.TOO_MUCH_ARGS.formatted(String.join(" ", words)));
		}

		Position pos;
		Action dir = Action.RIGHT;
		boolean isBig = true;

		try {
			pos = Position.stringToPos(words[0]);

			if (words.length >= 3) {
				try {
					dir = Action.stringToDir(words[2]);
				} catch (ActionParseException e) {
					throw new GameParseException(Messages.UNKNOWN_MOVING_DIRECTION.formatted(String.join(" ", words))
							+ "\n" + Messages.ERROR.formatted(e.getMessage()));
				}
			}

			if (words.length == 4) {
				if (words[3].equalsIgnoreCase("BIG") || words[3].equalsIgnoreCase("B"))
					isBig = true;
				else if (words[3].equalsIgnoreCase("SMALL") || words[3].equalsIgnoreCase("S"))
					isBig = false;
				else
					throw new GameParseException(Messages.INVALID_MARIO_SIZE.formatted(String.join(" ", words)));
			}

		} catch (GameParseException e) {
			throw e;
		} catch (Exception e) {
			throw new GameParseException(e.getMessage());
		}

		return new Mario(game, pos, dir, isBig);
	}

	public void registerNew(GameWorld game) {
		game.newMario(this);
	}

	@Override
	public String serialize() {
		String size = isBig ? "Big" : "Small";
		return pos.toString() + getName() + " " + direction.toString() + " " + size;
	}

	@Override
	public Mario copy() {
		return new Mario(game, new Position(pos.getRow(), pos.getCol()), this.direction, this.isBig);
	}

}
