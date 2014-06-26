package objects.behaviour;

import objects.Cell;

// in this behaviour the cell stays in the same tile that it currently is
public class StayBehaviour extends Behaviour {

	@Override
	public boolean execute(Cell c) {
		c.holdPosition();
		return true; // always possible (dying happens automatically)
	}
}
