package tp1.control.commands;

import java.util.Arrays;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.exceptions.GameModelException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class AddObjectCommand extends AbstractCommand {

	private static final String NAME = Messages.COMMAND_ADD_OBJECT_NAME;
	private static final String SHORTCUT = Messages.COMMAND_ADD_OBJECT_SHORTCUT;
	private static final String DETAILS = Messages.COMMAND_ADD_OBJECT_DETAILS;
	private static final String HELP = Messages.COMMAND_ADD_OBJECT_HELP;

	private String[] objectDescription;

	public AddObjectCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}

	public AddObjectCommand(String[] objectDescription) {
		super(NAME, SHORTCUT, DETAILS, HELP);
		this.objectDescription = objectDescription;
	}

	@Override
	public AddObjectCommand parse(String[] commandWords) throws CommandParseException {
		if (!matchCommandName(commandWords[0]))
			return null;

		if (commandWords.length < 3)
			throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);

		// Devuelve una copia del array desde el indice 1 hasta el final
		String[] objWords = Arrays.copyOfRange(commandWords, 1, commandWords.length);
		return new AddObjectCommand(objWords);
	}

	@Override
	public void execute(GameModel game, GameView view) throws CommandExecuteException {

		try {
			game.addObject(objectDescription);
			view.showGame();

		} catch (GameModelException e) {
			throw new CommandExecuteException(Messages.ERROR_COMMAND_EXECUTE, e);
		}
	}

}
