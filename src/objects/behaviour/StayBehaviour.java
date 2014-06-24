package objects.behaviour;

import objects.Cell;

// in this behaviour the cell stays in the same tile that it currently is
public class StayBehaviour extends WanderBehaviour {

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
		c.properties.currentEnergy--;
		
		// if still alive :-)
		if (c.properties.currentEnergy > 0) {
			c.worldRef.get().nextCells.add(c);
		}
	}
}
