package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;

import objects.Cell;

public class HuntBehaviourRobin extends Behaviour {

	@Override
	public boolean execute(Cell c) {
		System.out.println("Hunting");
		
		if(!c.isHungry()) return false;
		
		ArrayList <Tile> vision = c.getPerceptionSet();
		ArrayList <Cell> prey = new ArrayList<Cell>();
		
		for (Tile t : vision) {
			Cell cell = t.worldRef.get().getCellAtPositionCurrent(t.x, t.y);
			if (cell != null && cell.type != c.type) {
				prey.add(cell);
			}
		}
		ArrayList<Cell> possibleTargets = new ArrayList<Cell>();
		for (Cell possible : prey ) {
			if(isWeaker(possible, c)){
				possibleTargets.add(possible);
			}
		}
		Cell finalTarget = null;
		Tile bestTile = null;
		double shortestDistance = 1000;
		for (Cell target : possibleTargets) {
			Tile tile = target.getClosestFreeNeighbour(c.x, c.y);
			if (tile == null){
				continue;
			}
			 
			int Dx = tile.x - c.x;
			int Dy = tile.y - c.y;
			double distance = Math.sqrt(Dx * Dx  + Dy * Dy);
			if (distance < shortestDistance) {
				shortestDistance = distance;
				bestTile = tile;
				finalTarget = target;
			}
			
		}
 		
		if(bestTile == null || finalTarget == null) return false;
		
		c.moveTo(bestTile);
		
		if(c.x == bestTile.x && c.y == bestTile.y){
			c.eat(finalTarget);
		}
		
		return false;
	}
	
	
	
	private boolean isWeaker(Cell target, Cell me){
		
		int friendStrength = 0;
		ArrayList <Tile> vision = me.getPerceptionSet(); 
		ArrayList <Cell> friends = new ArrayList<Cell>();
		
		for(Tile t : vision) {
			Cell cell = t.worldRef.get().getCellAtPositionNext(t.x, t.y);
			if (cell != null && cell != me && cell.type == me.type) {
				friends.add(cell);
			}
		}
		
		for (Cell friend : friends) {
			friendStrength += friend.properties.getStrength();
		}
		
		int targetStrength = target.properties.getStrength();
		int meStrength = me.properties.getStrength() + friendStrength;
		
		return meStrength > targetStrength;
	}

}
