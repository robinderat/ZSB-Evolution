package objects.behaviour;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import framework.Tile;
import objects.Cell;

// in this behaviour the cell scours its surroundings for 
public class HuntBehaviour extends MoveAnywhereBehaviour {

	private boolean targetIsClose;
	
	@Override
	public boolean execute(Cell c) {
		if (!(c.properties.currentEnergy > 1)) return false;

		c.isHunting = true;
		targetIsClose = false;
		ArrayList<Tile> perception = c.getPerceptionSet();

		// first search for other cells in area
		Tile target = scour(perception, c);
		// if found, check if you can eat/beat them
		if (target != null) {
			// if so, check if they are far or close
			if (!targetIsClose){ // if far, approach
				c.moveTo(target);
			} else { // if close, eat
				c.eat(target.worldRef.get().getCellAtPositionCurrent(target.x, target.y));
				c.moveTo(target);
			}
			
			//temp add to next world state. should be called by eat or moveto so remove when that is implemented
			c.worldRef.get().nextCells.add(c);
			
			// and return true!
			c.isHunting = false;
			return true;
		}
		// if not found, return (do nothing) ==> will automatically try another behaviour
		else {
			c.isHunting = false;
			return false;
		}
	}
	
	// scours surrounding for tiles that hold a cell
	public Tile scour(ArrayList<Tile> vision, Cell me){
		CopyOnWriteArrayList<Cell> targets = new CopyOnWriteArrayList<Cell>();
		// ^if you don't know what this is:
		// difference between CopyOnWrite and normal ArrayList is that CoW allows you to change the list
		// while iterating over it (at expense of more memory cost)
		
		for (Tile tile : vision) {
			Cell target = tile.worldRef.get().getCellAtPositionCurrent(tile.x, tile.y);
			
			if (target != null && target != me){
				// if very hungry, cells resort to cannibalism
				if (me.properties.currentEnergy < me.properties.getMaxEnergy()/4 && target.type == me.type){
					targets.add(target);
					continue;
				}
				// check if cell is of other type
				if (me.type != target.type) targets.add(target);
			}
		}
		// if found, see if it can eat/beat any of those cells
		return chooseOnlyBeatableTargets(targets, me);
	}
	
	// selects a target from a list of targets that it can eat/beat
	public Tile chooseOnlyBeatableTargets(CopyOnWriteArrayList<Cell> targets, Cell me){
		// cant do for cell in targets because of concurrent modification, so need a while true loop
		for (Cell potentialFood : targets) {
		
			// cell must decide whether it can beat target. it does this based on two relevant stats
			// (energy and strength). right now our idea is basing it solely on strength
			if (me.properties.getStrength() < potentialFood.properties.getStrength()) {
				targets.remove(potentialFood);
			}
			
		
		}
		// now that it has filtered its list to only targets it thinks it can beat, it is time to pick one
		return bestOfTargets(targets, me);
	}
	
	// filters list until only one target is left, based on energy and/or strength
	// NOTE: right now selects weakest, maybe we should do this instead until strongest is left?
	// SOURCE: how do they do that in nature?
	// OTHER NOTE: ideally, we would weigh distance to target and strength of target and pick best based on both
	// ^for now though, just base on strength
	// OVERRULED : actually, for now I am basing this on energy
	public Tile bestOfTargets(CopyOnWriteArrayList<Cell> targets, Cell me){
		if (targets.isEmpty()) return null;	
		
		// first check if one or more of the targets is a neighbor (an optimal target)

		ArrayList<Cell> optimalTargets = new ArrayList<Cell>();
		ArrayList<Tile> neighbors = me.getMoveSet();
		for (Cell target : targets){
			Tile them = target.worldRef.get().getTile(target.x,target.y);
			if (neighbors.contains(them)){
				optimalTargets.add(target);
				break;
			}
		}
		// if there are neighbors
		if (!optimalTargets.isEmpty()){
			targets = new CopyOnWriteArrayList<Cell>();
			for (Cell t : optimalTargets) targets.add(t);
			targetIsClose = true;
		}
		
		while (targets.size() > 1) {
			Cell one = targets.get(0);
			Cell two = targets.get(1);
			if (one.properties.currentEnergy >= two.properties.currentEnergy) targets.remove(one);
			else if (one.properties.currentEnergy < two.properties.currentEnergy) targets.remove(two);
			// additional if to take the cell closest to hunter if both targets have equal currentEnergy
			//else {
			//	if (distance(two)>distance(one))
			//}
		}
		
		// DEBUG write lines to show it hunting
		System.out.println("target found: " + targets.get(0).type + " " + targets.get(0).x + " " + targets.get(0).y);
		// END DEBUG line
		
		return targets.get(0).worldRef.get().getTile(targets.get(0).x, targets.get(0).y);
	}
}