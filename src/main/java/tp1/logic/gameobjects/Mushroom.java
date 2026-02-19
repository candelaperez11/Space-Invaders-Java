package tp1.logic.gameobjects;

import tp1.exceptions.ActionParseException;
import tp1.exceptions.GameParseException;
import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Mushroom extends MovingObject {

	public Mushroom(GameWorld game, Position pos) {
		super(game, pos, Action.RIGHT);
	}

	public Mushroom(GameWorld game, Position pos, Action dir) {
		super(game, pos, dir);
	}

	public Mushroom() {
		super();
	}

	@Override
	public void update() {
		if (!isAlive())
			return;

		// Cuando sale de una caja, debe estar parada en el primer update
		if (direction == Action.STOP) {
			direction = Action.RIGHT;
			return;
		}

		walkOrFall();
	}

	@Override
	public String getIcon() {
		return Messages.MUSHROOM;
	}

	@Override
	public String getName() {
		return Messages.MUSHROOM_NAME;
	}

	@Override
	public String getSymbol() {
		return Messages.MUSHROOM_SYMBOL;
	}

	@Override
	public GameObject parse(String[] words, GameWorld game) throws GameParseException {
		if (!(words.length >= 2 && (words[1].equalsIgnoreCase(getName()) || words[1].equalsIgnoreCase(getSymbol()))))
			return null;

		if (words.length > 3) {
			throw new GameParseException(Messages.TOO_MUCH_ARGS.formatted(String.join(" ", words)));
		}

		Position pos;
		Action dir = Action.RIGHT;

		try {
			pos = Position.stringToPos(words[0]);

			if (words.length == 3) {
				try {
					dir = Action.stringToDir(words[2]);
				} catch (ActionParseException e) {
					throw new GameParseException(Messages.UNKNOWN_MOVING_DIRECTION.formatted(String.join(" ", words))
							+ "\n" + Messages.ERROR.formatted(e.getMessage()));
				}

				if (dir == Action.UP || dir == Action.DOWN) {
					throw new GameParseException(Messages.UNKNOWN_MOVING_DIRECTION.formatted(String.join(" ", words))
							+ "\n" + Messages.ERROR.formatted(Messages.UNKNOWN_ACTION.formatted(words[2])));
				}
			}

		} catch (GameParseException e) {
			throw e;
		} catch (Exception e) {
			throw new GameParseException(e.getMessage());
		}

		return new Mushroom(game, pos, dir);
	}

	@Override
	public boolean interactWith(GameItem other) {
		if (other.isInPosition(this.pos))
			return other.receiveInteraction(this);
		return false;
	}

	@Override
	public boolean receiveInteraction(Mario m) {
		m.powerUp();
		die();
		return true;
	}

	@Override
	public String serialize() {
		return pos.toString() + getName() + " " + direction.toString();
	}

	@Override
	public GameObject copy() {
		return new Mushroom(game, new Position(pos.getRow(), pos.getCol()), this.direction);
	}

}
