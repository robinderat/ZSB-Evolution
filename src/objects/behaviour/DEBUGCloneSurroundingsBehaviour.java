package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;
import objects.Cell;

// in this behaviour the cell clones itself onto all tiles around it
public class DEBUGCloneSurroundingsBehaviour extends Behaviour {

	@Override
	public void execute(Cell c) {
		ArrayList<Tile> moves = c.getMoveSet();
		c.DEBUGmoveToAllInMoveSet(moves, c);
	}

	@Override
	public boolean isPossible(Cell c) {
		return c.properties.currentEnergy > 1;
	}

}
