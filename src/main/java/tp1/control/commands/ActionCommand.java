package tp1.control.commands;

import java.util.ArrayList;
import java.util.List;

import tp1.exceptions.ActionParseException;
import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.logic.Action;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class ActionCommand extends AbstractCommand {

	private static final String NAME = Messages.COMMAND_ACTION_NAME;
	private static final String SHORTCUT = Messages.COMMAND_ACTION_SHORTCUT;
	private static final String DETAILS = Messages.COMMAND_ACTION_DETAILS;
	private static final String HELP = Messages.COMMAND_ACTION_HELP;

	private Action[] actions;

	public ActionCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}

	private ActionCommand(Action[] actions) {
		this();
		this.actions = actions;
	}

	@Override
	public ActionCommand parse(String[] commandWords) throws CommandParseException {
		if (!matchCommandName(commandWords[0]))
			return null;

		if (commandWords.length < 2)
			throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);

		List<Action> parsedActions = new ArrayList<>();

		for (int i = 1; i < commandWords.length; i++) {
			try {
				parsedActions.add(Action.stringToDir(commandWords[i]));
			} catch (ActionParseException e) {

			}
		}

		if (parsedActions.isEmpty()) {
			throw new CommandParseException(Messages.INCORRECT_ACTION_COMMAND);
		}

		return new ActionCommand(parsedActions.toArray(new Action[0]));
	}

	@Override
	public void execute(GameModel game, GameView view) throws CommandExecuteException {

		try {
			for (Action act : actions)
				game.addAction(act);

			game.update();
			view.showGame();

		} catch (Exception e) {
			throw new CommandExecuteException(Messages.ERROR_COMMAND_EXECUTE, e);
		}
	}

}
