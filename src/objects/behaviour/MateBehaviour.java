package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;

import objects.Cell;

public class MateBehaviour extends Behaviour {

	@Override
	public boolean execute(Cell c) {
		
		ArrayList<Tile> vision = c.getPerceptionSet();
		ArrayList<Cell> partners = getPotentialPartners(vision);
		
		if (partners.size() == 0) return false;
		
		Cell partner = null;
		Tile bestTile = null;
		double shortestDistance = 1000;
		for(Cell cell : partners){
			 Tile tile = cell.getClosestNeighbour(c.x, c.y);
			 int Dx = tile.x - c.x;
			 int Dy = tile.y - c.y;
			 double distance = Math.sqrt(Dx *Dx  + Dy * Dy);
			 if (distance < shortestDistance) {
				 shortestDistance = distance;
				 bestTile = tile;
				 partner = cell;
			 }
		}
		
		c.moveTo(bestTile);
		if (c.x == bestTile.x && c.y == bestTile.y) {
			if (c.mate(partner)) {
				return true;
			}
		}
		
		return false;
	}

	private ArrayList<Cell> getPotentialPartners(ArrayList<Tile> visionRad) {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		
		for (Tile tile : visionRad){
			Cell cell = tile.worldRef.get().getCellAtPositionCurrent(tile.x, tile.y);
			if(cell != null && cell.canMate()){
				cells.add(cell);
			}
		}

		return cells;
	}

}
