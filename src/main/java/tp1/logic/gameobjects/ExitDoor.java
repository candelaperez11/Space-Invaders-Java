package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class ExitDoor extends GameObject {

	public ExitDoor(GameWorld game, Position pos) {
		super(game, pos);
	}

	public ExitDoor() {
		super();
	}

	@Override
	public void update() {
	}

	@Override
	public String getIcon() {
		return Messages.EXIT_DOOR;
	}

	@Override
	public String getName() {
		return Messages.EXITDOOR_NAME;
	}

	@Override
	public String getSymbol() {
		return Messages.EXITDOOR_SYMBOL;
	}

	@Override
	public GameObject createInstance(GameWorld game, Position pos) {
		return new ExitDoor(game, pos);
	}

	@Override
	public boolean interactWith(GameItem other) {
		if (other.isInPosition(this.pos))
			return other.receiveInteraction(this);
		return false;
	}

	@Override
	public boolean receiveInteraction(Mario m) {
		game.marioExited();
		return true;
	}

	@Override
	public String serialize() {
		return pos.toString() + getName();
	}

	@Override
	public GameObject copy() {
		return new ExitDoor(game, new Position(pos.getRow(), pos.getCol()));
	}

}
