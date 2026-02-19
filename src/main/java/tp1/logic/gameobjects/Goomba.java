package tp1.logic.gameobjects;

import tp1.exceptions.ActionParseException;
import tp1.exceptions.GameParseException;
import tp1.exceptions.PositionParseException;
import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Goomba extends MovingObject {

	public Goomba(GameWorld game, Position pos) {
		super(game, pos, Action.LEFT);
	}

	public Goomba(GameWorld game, Position pos, Action dir) {
		super(game, pos, dir);
	}

	public Goomba() {
		super();
	}

	@Override
	public void update() {
		if (!isAlive())
			return;
		walkOrFall();
	}

	@Override
	public String getIcon() {
		return Messages.GOOMBA;
	}

	@Override
	public String getName() {
		return Messages.GOOMBA_NAME;
	}

	@Override
	public String getSymbol() {
		return Messages.GOOMBA_SYMBOL;
	}

	@Override
	public GameObject parse(String[] words, GameWorld game) throws GameParseException {
		if (!(words.length >= 2 && (words[1].equalsIgnoreCase(getName()) || words[1].equalsIgnoreCase(getSymbol()))))
			return null;

		if (words.length > 3) {
			throw new GameParseException(Messages.TOO_MUCH_ARGS.formatted(String.join(" ", words)));
		}

		Position pos;
		Action dir = Action.LEFT;

		try {
			pos = Position.stringToPos(words[0]);

			if (words.length == 3) {

				try {
					dir = Action.stringToDir(words[2]);
				} catch (ActionParseException e) {
					throw new GameParseException(Messages.UNKNOWN_MOVING_DIRECTION.formatted(String.join(" ", words))
							+ "\n" + Messages.ERROR.formatted(e.getMessage()));
				}

				if (dir != Action.RIGHT && dir != Action.LEFT) {
					throw new GameParseException(Messages.INVALID_MOVING_DIRECTION.formatted(String.join(" ", words)));
				}
			}

		} catch (PositionParseException e) {
			String msg = Messages.INVALID_OBJECT_POSITION.formatted(String.join(" ", words)) + "\n"
					+ Messages.ERROR.formatted(e.getMessage().split("\n")[0]);

			String[] parts = e.getMessage().split("\n");
			if (parts.length > 1) {
				msg += "\n" + Messages.ERROR.formatted(parts[1]);
			}

			throw new GameParseException(msg);
		} catch (GameParseException e) {
			throw e;
		}

		return new Goomba(game, pos, dir);
	}

	@Override
	public boolean interactWith(GameItem other) {
		if (other.isInPosition(this.pos))
			return other.receiveInteraction(this);
		return false;
	}

	@Override
	public boolean receiveInteraction(Mario m) {
		if (!m.isFalling()) {
			m.hitByEnemy();
		}

		die();
		game.addPoints(100);
		return true;
	}

	@Override
	public String serialize() {
		return pos.toString() + getName() + " " + direction.toString();
	}

	@Override
	public GameObject copy() {
		return new Goomba(game, new Position(pos.getRow(), pos.getCol()), this.direction);
	}

}
