package objects.behaviour;

import objects.Cell;

public abstract class Behaviour {
	public abstract boolean execute(Cell c);
	public abstract boolean isPossible(Cell c);
}
