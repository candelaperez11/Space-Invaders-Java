package tp1.logic;

import java.util.ArrayList;
import java.util.List;

public class ActionList {

	private List<Action> actionsToDo; // Lista de acciones pendientes

	public ActionList() {
		this.actionsToDo = new ArrayList<>();
	}

	public boolean isValidAction(Action action, List<Action> actions) {
		return action == Action.STOP && !actions.contains(action)
				|| action != Action.STOP && !actions.contains(action.opposite()) && this.times(action, actions) < 4;
	}

	private int times(Action action, List<Action> actions) {
		int count = 0;
		for (Action a : actions) {
			if (a == action)
				count++;
		}
		return count;
	}

	public void add(Action action) {
		if (isValidAction(action, actionsToDo)) {
			actionsToDo.add(action);
		}
	}

	public boolean hasActions() {
		return !actionsToDo.isEmpty();
	}

	public Action nextAction() {
		if (actionsToDo.isEmpty()) {
			return null;
		}
		return actionsToDo.remove(0);
	}

	public void clear() {
		actionsToDo.clear();
	}
}
