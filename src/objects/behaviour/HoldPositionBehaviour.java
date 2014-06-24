package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;
import objects.Cell;


// in this behaviour the cell stays in the same tile that it currently is
public class HoldPositionBehaviour extends MoveAnywhereBehaviour {

	@Override
	public boolean execute(Cell c) {
		holdPosition(c);
		return true; // always possible (unless you die) ?? ==> then still this must return true
					 // because this is the SOLE behaviour where you can even do it if you have too low energy
					 // just need to make sure it dies in that case
	}
	
	// cell stays in same spot ((( NOTE ! this still needs the kill cell if <2 energy )))
	public void holdPosition(Cell c) {
		c.worldRef.get().nextCells.add(c);
	}
}
