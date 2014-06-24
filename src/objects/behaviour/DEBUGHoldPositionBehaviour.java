package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;
import objects.Cell;


// in this behaviour the cell stays in the same tile that it currently is
public class DEBUGHoldPositionBehaviour extends Behaviour {

	@Override
	public void execute(Cell c) {
		c.holdPosition();
	}
	// NOTE: if we use this behaviour then it probably needs to kill the cell if it has <2 energy

	@Override
	public boolean isPossible(Cell c) {
		return c.properties.currentEnergy > 1;
	}

}
