package tp1.exceptions;

public class GameLoadException extends GameModelException {

	public GameLoadException(String message) {
		super(message);
	}

	public GameLoadException(String message, Throwable cause) {
		super(message, cause);
	}
}