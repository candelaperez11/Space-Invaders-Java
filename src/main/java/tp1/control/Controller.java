package tp1.control;

import tp1.control.commands.Command;
import tp1.control.commands.CommandGenerator;
import tp1.exceptions.CommandException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

/**
 * Accepts user input and coordinates the game execution logic
 */
public class Controller {

	private GameModel game;
	private GameView view;

	public Controller(GameModel game, GameView view) {
		this.game = game;
		this.view = view;
	}

	/**
	 * Runs the game logic, coordinate Model(game) and View(view)
	 * 
	 */
	public void run() {
		view.showWelcome();
		view.showGame();

		while (!game.isFinished()) {
			String[] userWords = view.getPrompt();

			try {
				Command command = CommandGenerator.parse(userWords);
				if (command != null)
					command.execute(game, view);
				else
					view.showError(Messages.UNKNOWN_COMMAND.formatted(userWords[0]));

			} catch (CommandException e) {
				view.showError(e.getMessage());
				Throwable cause = e.getCause();
				while (cause != null) {
					view.showError(cause.getMessage());
					cause = cause.getCause();
				}
			}
		}

		view.showEndMessage();
	}

}
