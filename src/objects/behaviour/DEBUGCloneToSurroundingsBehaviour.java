package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;
import objects.Cell;

// in this behaviour the cell clones itself onto all tiles around it
public class DEBUGCloneToSurroundingsBehaviour extends Behaviour {

	@Override
	public boolean execute(Cell c) {
		if (!(c.properties.currentEnergy > 1)) return false;
		
		ArrayList<Tile> moves = c.getMoveSet();
		return (DEBUGmoveToAllInMoveSet(moves, c));
	}
	
	// moves to surrounding tiles
	public boolean DEBUGmoveToAllInMoveSet(ArrayList<Tile> moveSet, Cell oldCell){
		boolean succes = false;
		for (Tile tile : moveSet) {
			if (oldCell.worldRef.get().getCellAtPositionNext(tile.x, tile.y) == null &&
				oldCell.worldRef.get().getCellAtPositionCurrent(tile.x, tile.y) == null) { 
				Cell cell = new Cell(oldCell.worldRef.get(), tile.x, tile.y, oldCell.type, null);
				
				oldCell.worldRef.get().nextCells.add(cell);
				succes = true;
			}
		}
		return succes;
	}
}
