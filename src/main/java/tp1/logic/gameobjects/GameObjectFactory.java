package tp1.logic.gameobjects;

import java.util.Arrays;
import java.util.List;

import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;
import tp1.logic.GameWorld;
import tp1.view.Messages;

public class GameObjectFactory {

	private static final List<GameObject> AVAILABLE_OBJECTS = Arrays.asList(new Land(), new ExitDoor(), new Goomba(),
			new Mario(), new Mushroom(), new Box());

	public static GameObject parse(String[] words, GameWorld game) throws ObjectParseException, OffBoardException {

		for (GameObject obj : AVAILABLE_OBJECTS) {
			GameObject parsed = null;

			try {
				parsed = obj.parse(words, game);

			} catch (Exception e) {
				throw new ObjectParseException(e.getMessage());
			}

			if (parsed != null) {
				if (!game.isInBoard(parsed.getPosition())) {
					throw new OffBoardException(Messages.OFF_BOARD_ERROR.formatted(String.join(" ", words)));
				}

				return parsed;
			}
		}
		throw new ObjectParseException(Messages.UNKNOWN_GAME_OBJECT.formatted(String.join(" ", words)));
	}

}
