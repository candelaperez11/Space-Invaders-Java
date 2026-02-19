package tp1.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tp1.exceptions.GameLoadException;
import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.GameObjectFactory;
import tp1.logic.gameobjects.Mario;
import tp1.view.Messages;

public class FileGameConfiguration implements GameConfiguration {

	private int remainingTime;
	private int points;
	private int lives;

	private Mario mario;
	private List<GameObject> npcObjects;

	public FileGameConfiguration(String fileName, GameWorld game) throws GameLoadException {
		npcObjects = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String line = br.readLine();
			if (line == null)
				throw new GameLoadException(Messages.ERROR_EMPTY_FILE.formatted(fileName));

			String[] parts = line.trim().split("\\s+");
			if (parts.length != 3)
				throw new GameLoadException(Messages.INCORRECT_GAME_STATUS.formatted(line));

			try {
				remainingTime = Integer.parseInt(parts[0]);
				points = Integer.parseInt(parts[1]);
				lives = Integer.parseInt(parts[2]);
			} catch (NumberFormatException e) {
				throw new GameLoadException(Messages.ERROR_INVALID_HEADER_NUMBER.formatted(line), e);
			}

			while ((line = br.readLine()) != null && !line.isEmpty()) {
				line = line.trim();
				String[] words = line.split("\\s+");

				try {
					Mario m = new Mario().parse(words, game);

					if (m != null) {
						mario = m;
						continue;
					}

					GameObject obj = GameObjectFactory.parse(words, game);
					npcObjects.add(obj);

				} catch (OffBoardException | ObjectParseException e) {
					throw e;
				}
			}

			if (mario == null)
				throw new GameLoadException(Messages.ERROR_NO_MARIO_FOUND);

		} catch (IOException ioe) {
			throw new GameLoadException(Messages.FILE_NOT_FOUND.formatted(fileName), ioe);

		} catch (GameLoadException e) {
			throw e;

		} catch (Exception e) {
			throw new GameLoadException(Messages.INVALID_FILE_CONFIGURATION.formatted(fileName), e);
		}
	}

	@Override
	public int getRemainingTime() {
		return remainingTime;
	}

	@Override
	public int points() {
		return points;
	}

	@Override
	public int numLives() {
		return lives;
	}

	@Override
	public Mario getMario() {
		return mario.copy();
	}

	@Override
	public List<GameObject> getNPCObjects() {
		List<GameObject> list = new ArrayList<>();
		for (GameObject obj : npcObjects) {
			list.add(obj.copy());
		}
		return list;
	}

}