package tp1.logic;

import tp1.exceptions.PositionParseException;
import tp1.view.Messages;

public class Position {

	private final int col;
	private final int row;

	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public String toString() {
		return "(" + row + "," + col + ") ";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return col == other.col && row == other.row;
	}

	// Para devolver la nueva posicion tras el movimiento del objeto
	public Position translate(int dRow, int dCol) {
		return new Position(this.row + dRow, this.col + dCol);
	}

	public Position up() {
		return translate(-1, 0);
	}

	public Position down() {
		return translate(1, 0);
	}

	public Position left() {
		return translate(0, -1);
	}

	public Position right() {
		return translate(0, 1);
	}

	public Position move(Action dir) {
		return translate(dir.getY(), dir.getX());
	}

	// Para parsear una posicion que se lee en formato de string: "(4,2)"
	public static Position stringToPos(String s) throws PositionParseException {

		String[] nums = null;

		try {
			if (!s.matches("\\(\\s*[^,]+\\s*,\\s*[^)]+\\s*\\)")) {
				throw new PositionParseException(Messages.INVALID_POSITION.formatted(s));
			}

			nums = s.replace("(", "").replace(")", "").split(",");

			if (nums.length != 2) {
				throw new PositionParseException(Messages.INVALID_POSITION.formatted(s));
			}

			int row = Integer.parseInt(nums[0]);
			int col = Integer.parseInt(nums[1]);

			return new Position(row, col);

		} catch (NumberFormatException e) {
			String msg = Messages.INVALID_POSITION.formatted(s) + "\n"
					+ Messages.FOR_INPUT_STRING.formatted(nums[0].trim());
			throw new PositionParseException(msg);
		}
	}

}
