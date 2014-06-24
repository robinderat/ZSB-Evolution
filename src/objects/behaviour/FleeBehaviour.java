package objects.behaviour;

import java.util.Vector;

import framework.Tile;
import objects.Cell;

// in this behaviour the cell scours its surroundings for 
public class FleeBehaviour extends WanderBehaviour {

	@Override
	public boolean execute(Cell c) {
		
		if (!(c.properties.currentEnergy > 1)) return false;
			System.out.println("Flee" + c);
			Vector<Tile> perception = c.getPerceptionSet();
	
			// first search for other cells in area
			Vector<Tile> dangerousTiles = searchForDanger(perception, c);
			// if found, move in other direction
			if (dangerousTiles.size() != 0) {	
				Vector<Tile> options = new Vector<Tile>();
				Vector<Tile> tiles = c.getMoveSet();
				Tile bestTile = null;
				
				for (Tile t : dangerousTiles) {
					Tile proposedTile = null;
					double distance = 0;
					for (Tile tile : tiles) {
							
						int Dx = tile.x - t.x; 
						int Dy = tile.y - t.y;
						double newDistance = Math.sqrt(Dx * Dx + Dy * Dy);
							
						if (newDistance > distance) {
							distance = newDistance;
							proposedTile = tile;
						}
					}
					if(proposedTile != null){
						options.add(proposedTile);
					}	
				}
				double bestDistance = 0;
				double totalDistance = 0;
				for (Tile tile : options) {
					for(Tile t : dangerousTiles){
						System.out.println("tile" + tile);
						System.out.println("dangerous" + t);
						int Dx = tile.x - t.x; 
						int Dy = tile.y - t.y;
						double newDistance = Math.sqrt(Dx * Dx + Dy * Dy);
						totalDistance = totalDistance + newDistance;
					}
					if (totalDistance > bestDistance) {
						bestDistance = totalDistance;
						bestTile = tile;
					}
			}
			c.moveTo(bestTile);
			return true;
		} else { // if not found then no move possible in this behaviour
			return false;
		}
		
	}
	
	// looks through perception tiles searching for tiles that have a cell on them
	private Vector<Tile> searchForDanger(Vector<Tile> vision, Cell cell) {
		// create vector, reserve memory for size
		Vector<Tile> dangerousTiles = new Vector<Tile>(vision.size());
		for (Tile tile : vision) {
			Cell c = tile.worldRef.get().getCellAtPositionCurrent(tile.x, tile.y);
			
			if(c != null){
			}
			
			if(c != null && cell.type != c.type){
				dangerousTiles.add(tile);
			}
		}
		
		
		// returns a list containing all tiles that have a cell on them
		return dangerousTiles;
	}
	
}