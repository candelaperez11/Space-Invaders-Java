package tp1.control.commands;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.exceptions.GameLoadException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class LoadCommand extends AbstractCommand {

	private static final String NAME = Messages.COMMAND_LOAD_NAME;
	private static final String SHORTCUT = Messages.COMMAND_LOAD_SHORTCUT;
	private static final String DETAILS = Messages.COMMAND_LOAD_DETAILS;
	private static final String HELP = Messages.COMMAND_LOAD_HELP;

	private String fileName;

	public LoadCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}

	public LoadCommand(String fileName) {
		this();
		this.fileName = fileName;
	}

	@Override
	public Command parse(String[] words) throws CommandParseException {
		if (matchCommandName(words[0])) {

			if (words.length != 2)
				throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);

			return new LoadCommand(words[1]);
		}
		return null;
	}

	@Override
	public void execute(GameModel game, GameView view) throws CommandExecuteException {

		try {
			game.load(fileName);
			view.showGame();

		} catch (GameLoadException e) {
			view.showError(Messages.ERROR_LOADING_FILE.formatted(fileName));
			view.showError(e.getMessage());

			if (e.getCause() != null) {
				view.showError(e.getCause().getMessage());
			}
		}
	}
}
