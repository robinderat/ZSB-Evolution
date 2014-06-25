package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;

import objects.Cell;

public class MateBehaviour extends Behaviour {
	
	

	@Override
	public boolean execute(Cell c) {
		
		if (!(c.isAlive())) {
			return false;
		}
		
		ArrayList<Tile> vision = c.getPerceptionSet();
		ArrayList<Cell> partners = getPotentialPartners(vision, c);
		
		if (partners.size() == 0) return false;
		
		Cell partner = null;
		Tile bestTile = null;
		double shortestDistance = 1000;
		for(Cell part : partners){
			 Tile tile = part.getClosestFreeNeighbour(c.x, c.y);
			 
			 if (tile == null){
				 continue;
			 }
			 
			 int Dx = tile.x - c.x;
			 int Dy = tile.y - c.y;
			 double distance = Math.sqrt(Dx * Dx  + Dy * Dy);
			 if (distance < shortestDistance) {
				 shortestDistance = distance;
				 System.out.println("1x " + tile.x + " y " + tile.y);
				 bestTile = tile;
				 partner = part;
			 }
		}
		
		System.out.println("bestx " + bestTile.x + " besty " + bestTile.y);
		
		if (bestTile == null || partner == null) {	
			return false;
		}
		
		c.moveTo(bestTile);
		System.out.println("cellx: " + c.x + " celly " + c.y);
		if (c.x == bestTile.x && c.y == bestTile.y) {
			if (c.mate(partner)) {
				System.out.println("Mating");
				return true;
			}
		}
		
		return false;
	}

	private ArrayList<Cell> getPotentialPartners(ArrayList<Tile> visionRad, Cell c) {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		
		for (Tile tile : visionRad){
			Cell cell = tile.worldRef.get().getCellAtPositionCurrent(tile.x, tile.y);
			if(cell != null && c.canMate() && cell.canMate() && cell != c && c.type == cell.type && c.isAlive() && cell.isAlive()){
				cells.add(cell);
			}
		}

		System.out.println(cells);
		return cells;
	}

}
