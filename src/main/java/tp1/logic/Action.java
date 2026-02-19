package tp1.logic;

import tp1.exceptions.ActionParseException;
import tp1.view.Messages;

/**
 * Represents the allowed actions in the game
 *
 */
public enum Action {
	LEFT(-1, 0), RIGHT(1, 0), DOWN(0, 1), UP(0, -1), STOP(0, 0);

	private int x;
	private int y;

	private Action(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Action opposite() {
		switch (this) {
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		default:
			return STOP;
		}
	}

	public static Action stringToDir(String s) throws ActionParseException {
		switch (s.toUpperCase()) {
		case "RIGHT":
		case "R":
			return RIGHT;

		case "LEFT":
		case "L":
			return LEFT;

		case "UP":
		case "U":
			return UP;

		case "DOWN":
		case "D":
			return DOWN;

		case "STOP":
		case "S":
			return STOP;

		default:
			throw new ActionParseException(Messages.UNKNOWN_ACTION.formatted(s));
		}
	}

}
