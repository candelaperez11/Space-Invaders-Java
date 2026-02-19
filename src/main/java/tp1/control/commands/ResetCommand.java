package tp1.control.commands;

import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class ResetCommand extends AbstractCommand {

	private static final String NAME = Messages.COMMAND_RESET_NAME;
	private static final String SHORTCUT = Messages.COMMAND_RESET_SHORTCUT;
	private static final String DETAILS = Messages.COMMAND_RESET_DETAILS;
	private static final String HELP = Messages.COMMAND_RESET_HELP;

	private Integer level;

	public ResetCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
		this.level = null;
	}

	private ResetCommand(Integer level) {
		this();
		this.level = level;
	}

	@Override
	public ResetCommand parse(String[] commandWords) throws CommandParseException {
		if (!matchCommandName(commandWords[0]))
			return null;

		if (commandWords.length > 2) {
			throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
		}

		if (commandWords.length == 1) {
			return new ResetCommand(null);
		}

		try {
			int level = Integer.parseInt(commandWords[1]);
			return new ResetCommand(level);

		} catch (NumberFormatException nfe) {
			throw new CommandParseException(Messages.LEVEL_NOT_A_NUMBER_ERROR.formatted(commandWords[1]), nfe);
		}
	}

	@Override
	public void execute(GameModel game, GameView view) {
		if (level == null)
			game.reset();
		else {
			if (level == 0 || level == 1 || level == -1 || level == 2) {
				game.reset(level);
			} else {
				view.showError(Messages.INVALID_LEVEL_NUMBER);
				return;
			}

		}

		view.showGame();
	}
}
