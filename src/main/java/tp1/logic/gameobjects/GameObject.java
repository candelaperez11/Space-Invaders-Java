package tp1.logic.gameobjects;

import tp1.exceptions.GameParseException;
import tp1.exceptions.PositionParseException;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public abstract class GameObject implements GameItem {

	protected GameWorld game;
	protected Position pos;
	private boolean isAlive;

	public GameObject(GameWorld game, Position pos) {
		this.game = game;
		this.pos = pos;
		this.isAlive = true;
	}

	protected GameObject() {
		this.isAlive = true;
	}

	public Position getPosition() {
		return pos;
	}

	@Override
	public boolean isInPosition(Position p) {
		return pos.equals(p);
	}

	@Override
	public boolean isAlive() {
		return isAlive;
	}

	public void die() {
		isAlive = false;
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	public abstract void update();

	public abstract String getIcon();

	public void registerNew(GameWorld game) {
	}

	// Para que los objetos definan su serializacion
	public abstract String serialize();

	public abstract GameObject copy();

	@Override
	public boolean receiveInteraction(Land obj) {
		return false;
	}

	@Override
	public boolean receiveInteraction(ExitDoor obj) {
		return false;
	}

	@Override
	public boolean receiveInteraction(Mario obj) {
		return false;
	}

	@Override
	public boolean receiveInteraction(Goomba obj) {
		return false;
	}

	@Override
	public boolean receiveInteraction(Mushroom obj) {
		return false;
	}

	@Override
	public boolean receiveInteraction(Box obj) {
		return false;
	}

	public GameObject parse(String[] words, GameWorld game) throws GameParseException {
		if (!(words.length >= 2 && (words[1].equalsIgnoreCase(getName()) || words[1].equalsIgnoreCase(getSymbol()))))
			return null;

		if (words.length > 2) {
			throw new GameParseException(Messages.TOO_MUCH_ARGS.formatted(String.join(" ", words)));
		}

		try {
			Position pos = Position.stringToPos(words[0]);
			return createInstance(game, pos);

		} catch (PositionParseException e) {
			String msg = Messages.INVALID_OBJECT_POSITION.formatted(String.join(" ", words)) + "\n"
					+ Messages.ERROR.formatted(e.getMessage().split("\n")[0]);

			String[] parts = e.getMessage().split("\n");
			if (parts.length > 1) {
				msg += "\n" + Messages.ERROR.formatted(parts[1]);
			}

			throw new GameParseException(msg);
		}
	}

	public GameObject createInstance(GameWorld game, Position pos) {
		return null;
	}

	public String getName() {
		return "";
	}

	public String getSymbol() {
		return "";
	}

}
