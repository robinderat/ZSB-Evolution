package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;
import objects.Cell;

// in this behaviour the cell wants to go one of the world's center
// note that this is a move behaviour, which means if it takes precedence over an eat behaviour, the cell will die
public class ApproachCenterBehaviour extends MoveAnywhereBehaviour {

	@Override
	public boolean execute(Cell c) {
		if (!(c.properties.currentEnergy > 1)) return false;
		
		return false;
	}
}