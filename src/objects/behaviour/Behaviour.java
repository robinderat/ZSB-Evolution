package objects.behaviour;

import objects.Cell;

public abstract class Behaviour {
	public abstract void execute(Cell c);
	public abstract boolean isPossible(Cell c);
}
