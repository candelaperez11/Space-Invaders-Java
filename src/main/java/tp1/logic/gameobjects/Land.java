package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Land extends GameObject {

	public Land(GameWorld game, Position pos) {
		super(game, pos);
	}

	public Land() {
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
		return Messages.LAND;
	}

	@Override
	public String getName() {
		return Messages.LAND_NAME;
	}

	@Override
	public String getSymbol() {
		return Messages.LAND_SYMBOL;
	}

	@Override
	public GameObject createInstance(GameWorld game, Position pos) {
		return new Land(game, pos);
	}

	@Override
	public boolean interactWith(GameItem other) {
		if (other.isInPosition(this.pos))
			return other.receiveInteraction(this);
		return false;
	}

	@Override
	public String serialize() {
		return pos.toString() + getName();
	}

	@Override
	public GameObject copy() {
		return new Land(game, new Position(pos.getRow(), pos.getCol()));
	}

}
