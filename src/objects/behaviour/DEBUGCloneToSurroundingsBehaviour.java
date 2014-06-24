package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;
import objects.Cell;

// in this behaviour the cell clones itself onto all tiles around it
public class DEBUGCloneToSurroundingsBehaviour extends Behaviour {

	@Override
	public void execute(Cell c) {
		ArrayList<Tile> moves = c.getMoveSet();
		DEBUGmoveToAllInMoveSet(moves, c);
	}

	@Override
	public boolean isPossible(Cell c) {
		return c.properties.currentEnergy > 1;
	}
	
	// moves to surrounding tiles
	public void DEBUGmoveToAllInMoveSet(ArrayList<Tile> moveSet, Cell oldCell){
		for (Tile tile : moveSet) {
			if (oldCell.worldRef.get().getCellAtPositionNext(tile.x, tile.y) == null &&
				oldCell.worldRef.get().getCellAtPositionCurrent(tile.x, tile.y) == null) { 
				Cell cell = new Cell(oldCell.worldRef.get(), tile.x, tile.y, oldCell.type, null);
				
				oldCell.worldRef.get().nextCells.add(cell);
			}
		}
	}
}
