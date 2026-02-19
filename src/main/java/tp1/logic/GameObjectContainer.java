package tp1.logic;

import java.util.ArrayList;
import java.util.List;

import tp1.logic.gameobjects.GameItem;
import tp1.logic.gameobjects.GameObject;

public class GameObjectContainer {

	private List<GameObject> gameObjects;

	public GameObjectContainer() {
		gameObjects = new ArrayList<>();
	}

	public String positionToString(Position p) {
		StringBuilder sb = new StringBuilder();

		for (GameObject obj : gameObjects) {
			if (obj.isInPosition(p) && obj.isAlive()) {
				sb.append(obj.getIcon());
			}
		}

		return sb.toString();
	}

	public void add(GameObject object) {
		if (object != null)
			gameObjects.add(object);
	}

	public void update() {
		// Bucle for indexado para evitar excepciones al modificar la lista mientras la
		// recorremos
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject object = gameObjects.get(i);
			object.update();
			doInteraction(object);
		}

		removeAllDead();
	}

	public void remove(GameObject obj) {
		gameObjects.remove(obj);
	}

	private void removeAllDead() {
		int i = gameObjects.size() - 1;
		while (i >= 0) {
			GameObject obj = gameObjects.get(i);
			if (!obj.isAlive()) {
				gameObjects.remove(i);
			}
			i--;
		}
	}

	public boolean isSolid(Position pos) {
		for (GameObject obj : gameObjects) {
			if (obj.isInPosition(pos) && obj.isSolid()) {
				return true;
			}
		}
		return false;
	}

	public void doInteraction(GameItem other) {
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject obj = gameObjects.get(i);
			if (obj != other && obj.isAlive() && other.isAlive()) {
				obj.interactWith(other);
				other.interactWith(obj);
			}
		}
	}

	public void serializeAll(StringBuilder sb) {
		for (GameObject obj : gameObjects) {
			sb.append(obj.serialize()).append("\n");
		}
	}

}
