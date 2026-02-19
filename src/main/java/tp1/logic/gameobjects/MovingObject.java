package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;

public abstract class MovingObject extends GameObject {

	protected Action direction;
	protected boolean isFalling;

	public MovingObject(GameWorld game, Position pos, Action dir) {
		super(game, pos);
		this.direction = dir;
		this.isFalling = false;
	}

	protected MovingObject() {
		super();
	}

	protected void walkOrFall() {
		if (isInAir()) {
			fall();
		} else if (direction != Action.STOP) {
			step();
		}
	}

	protected boolean isInAir() {
		return !game.isSolid(pos.down());
	}

	protected void fall() {
		isFalling = true;
		Position below = pos.down();

		if (!game.isInBoard(below)) {
			die();
		} else {
			pos = below;
		}
	}

	protected void step() {
		Position nextPos = pos.move(direction);

		if (game.isInBoard(nextPos) && !game.isSolid(nextPos)) {
			pos = nextPos;
		} else {
			direction = direction.opposite(); // rebota
		}
	}

	public boolean isFalling() {
		return isFalling;
	}

	@Override
	public boolean isSolid() {
		return false;
	}

}
