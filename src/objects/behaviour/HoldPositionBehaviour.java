package objects.behaviour;

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
		// also doing nothing costs energy. Ask Koen!
		// === NOTE this needs to scale to how big the cell is. big animals need require more energy to hold pos
		c.properties.currentEnergy--;
		
		// if still alive add to next state of world
		// DEBUG - temporarily removing auto expiration, uncomment the if line and the bracket to turn back on
		//if (c.properties.currentEnergy > 0) {
			c.worldRef.get().nextCells.add(c);
		//}
	}
}
