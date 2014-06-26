package objects.behaviour;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import framework.Settings;
import framework.Tile;
import objects.Cell;

// in this behaviour the cell scours its surroundings for cells that are weaker than it
public class HuntBehaviour extends Behaviour {

	double veryHungryRate = Settings.getInstance().veryHungryThreshold;
	Cell prey;
	
	@Override
	public boolean execute(Cell c) {
		if (!(c.isAlive())) {
			return false;
		}
		if (!(c.isHungry())) return false;

		ArrayList<Tile> perception = c.getPerceptionSet();

		// first search for other cells in area
		Tile target = scour(perception, c);
		// if found, check if you can eat/beat them
		if (target != null) {
			if (c.moveTo(target)) {
				c.stab(prey);
			}
			return true;
		}
		// if not found, return (do nothing) ==> will automatically try another behaviour
		else return false;
	}
	
	// scours surrounding for tiles that hold a cell
	public Tile scour(ArrayList<Tile> vision, Cell me){
		CopyOnWriteArrayList<Cell> targets = new CopyOnWriteArrayList<Cell>();
		// ^if you don't know what this is:
		// difference between CopyOnWrite and normal ArrayList is that CoW allows you to change the list
		// while iterating over it (at expense of more memory cost)
		
		for (Tile tile : vision) {
			Cell target = tile.worldRef.get().getCellAtPositionCurrent(tile.x, tile.y);
			
			if (target != null && target != me && target.isAlive()){
				// if very hungry, cells resort to cannibalism
				boolean allowCannibalism = Settings.getInstance().allowCannibalism;
				if (allowCannibalism && (me.properties.getCurrentEnergy() < me.properties.getMaxEnergy() * veryHungryRate && target.type == me.type)){
					targets.add(target);
					continue;
				}
				// check if cell is of other type
				if (me.type != target.type) targets.add(target);
			}
		}
		// if found, see if it can eat/beat any of those cells
		return chooseTarget(targets, me);
	}
	
	// selects a target from a list of targets that it can eat/beat
	public Tile chooseTarget(CopyOnWriteArrayList<Cell> targets, Cell me){
		// cant do for cell in targets because of concurrent modification, so need a while true loop
		for (Cell potentialFood : targets) {
		
			if (!(isWeaker(potentialFood,me)) || !(me.properties.getCurrentEnergy() < me.properties.getMaxEnergy() * veryHungryRate)) {
				targets.remove(potentialFood);
			}
		
		}
		// now that it has filtered its list to only targets it wants to beat, it is time to pick one
		return bestOfTargets(targets, me);
	}
	
	// filters list until only one target is left, based on energy and/or strength
	public Tile bestOfTargets(CopyOnWriteArrayList<Cell> targets, Cell me){
		if (targets.isEmpty()) return null;	
		
		Tile optimalTile = null;
		
		// check if a target has an empty space next to it
		double shortestDistance = 1000;
		for (Cell target : targets){
			Tile tile = target.getClosestFreeNeighbour(me.x, me.y);
			if (tile == null) continue;
			 
			int Dx = tile.x - me.x;
			int Dy = tile.y - me.y;
			double distance = Math.sqrt(Dx * Dx  + Dy * Dy);
			if (distance < shortestDistance) {
				shortestDistance = distance;
				optimalTile = tile;
				prey = target;
			}

		}
		
		//
		if (optimalTile == null){
			return null;
		}
		
		// DEBUG write lines to show it hunting
		//System.out.println("target found: " + targets.get(0).type + " " + targets.get(0).x + " " + targets.get(0).y);
		// END DEBUG line
		
		return optimalTile;
	}
	
	// compares strength of other cell with own strength. nearby cells of same type improve cell's overal strength.
	private boolean isWeaker(Cell target, Cell me){
		int meStrength = me.properties.getStrength();
		// anti cannibalism check
		if (target.type != me.type) {
			int friendStrength = 0;
			ArrayList <Tile> vision = me.getPerceptionSet(); 
			ArrayList <Cell> friends = new ArrayList<Cell>();
			
			for(Tile t : vision) {
				Cell cell = t.worldRef.get().getCellAtPositionNext(t.x, t.y);
				if (cell != null && cell != me && cell.type == me.type) {
					friends.add(cell);
				}
			}
			
			for (Cell friend : friends) friendStrength += friend.properties.getStrength();
			meStrength += friendStrength;
		}
		return meStrength > target.properties.getStrength();
	}
}