package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;
import objects.Cell;


// in this behaviour the cell stays in the same tile that it currently is
public class HoldPositionBehaviour extends Behaviour {

	@Override
	public void execute(Cell c) {
		holdPosition(c);
	}
	// NOTE: if we use this behaviour then it probably needs to kill the cell if it has <2 energy

	@Override
	public boolean isPossible(Cell c) {
		return c.properties.currentEnergy > 1;
	}

	// cell stays in same spot
	public void holdPosition(Cell c) {
		c.worldRef.get().nextCells.add(c);
	}
}
