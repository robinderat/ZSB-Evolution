package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;
import objects.Cell;

// in this behaviour the cell scours its surroundings for 
public class FleeBehaviour extends MoveAnywhereBehaviour {

	@Override
	public boolean execute(Cell c) {
		if (!(c.properties.currentEnergy > 1)) return false;


		ArrayList<Tile> perception = c.getPerceptionSet();

		// first search for other cells in area
		ArrayList<Tile> dangerousTiles = searchForDanger(perception);
		// if found, move in other direction
		if (!dangerousTiles.isEmpty()) {
			// flee behaviour (ROBIN)
			//
			return true;
		} else { // if not found then no move possible in this behaviour
			return false;
		}
		
	}
	
	// looks through perception tiles searching for tiles that have a cell on them
	public ArrayList<Tile> searchForDanger(ArrayList<Tile> tiles){
		ArrayList<Tile> dangerousTiles = new ArrayList<Tile>();
		
		// ROBIN
		
		// returns a list containing all tiles that have a cell on them
		return dangerousTiles;
	}
	
}