package tp1.logic.gameobjects;

import tp1.exceptions.GameParseException;
import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Box extends GameObject {

	private boolean isEmpty;

	public Box(GameWorld game, Position pos) {
		super(game, pos);
		this.isEmpty = false;
	}

	public Box(GameWorld game, Position pos, boolean isEmpty) {
		super(game, pos);
		this.isEmpty = isEmpty;
	}

	public Box() {
		super();
	}

	@Override
	public boolean isSolid() {
		return true;
	}

	@Override
	public void update() {
	}

	@Override
	public String getIcon() {
		if (!isEmpty)
			return Messages.BOX;
		else
			return Messages.EMPTY_BOX;
	}

	@Override
	public String getName() {
		return Messages.BOX_NAME;
	}

	@Override
	public String getSymbol() {
		return Messages.BOX_SYMBOL;
	}

	@Override
	public GameObject parse(String[] words, GameWorld game) throws GameParseException {
		if (!(words.length >= 2 && (words[1].equalsIgnoreCase(getName()) || words[1].equalsIgnoreCase(getSymbol()))))
			return null;

		if (words.length > 3) {
			throw new GameParseException(Messages.TOO_MUCH_ARGS.formatted(String.join(" ", words)));
		}

		Position pos;
		boolean isEmpty = false;

		try {
			pos = Position.stringToPos(words[0]);

			if (words.length == 3) {

				if (words[2].equalsIgnoreCase("EMPTY") || words[2].equalsIgnoreCase("E")) {
					isEmpty = true;
				} else if (words[2].equalsIgnoreCase("FULL") || words[2].equalsIgnoreCase("F")) {
					isEmpty = false;
				} else {
					throw new GameParseException(Messages.INVALID_BOX_STATUS.formatted(String.join(" ", words)));
				}
			}

		} catch (GameParseException e) {
			throw e;
		} catch (Exception e) {
			throw new GameParseException(e.getMessage());
		}

		return new Box(game, pos, isEmpty);
	}

	@Override
	public boolean interactWith(GameItem other) {
		if (other.isInPosition(pos.down()))
			return other.receiveInteraction(this);
		return false;
	}

	@Override
	public boolean receiveInteraction(Mario m) {
		if (!isEmpty) {
			isEmpty = true;
			game.addPoints(50);
			game.addGameObject(new Mushroom(game, pos.up(), Action.STOP));
		}
		return true;
	}

	@Override
	public String serialize() {
		return pos.toString() + getName() + " " + (isEmpty ? "Empty" : "Full");
	}

	@Override
	public GameObject copy() {
		return new Box(game, new Position(pos.getRow(), pos.getCol()), this.isEmpty);
	}

}
