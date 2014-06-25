package objects.behaviour;

import framework.Tile;
import objects.Cell;

// in this behaviour the cell wants to go one of the world's borders
// note that this is a move behaviour, which means if it takes precedence over an eat behaviour, the cell will die
public class ApproachBorderBehaviour extends WanderBehaviour {

	@Override
	public boolean execute(Cell c) {
		if (!(c.properties.getCurrentEnergy() > 1)) return false;
		
		Tile myLocation = c.worldRef.get().getTile(c.x,c.y);
		Tile[][] tiles = c.worldRef.get().getTiles();
		
		
		
		// get current position
		if (1<2 ) {}
		// get closest border
		// go there
		
		
		return false;
	}
}