package objects.behaviour;

import java.util.Vector;

import framework.Tile;
import objects.Cell;

// in this behaviour the cell scours its surroundings for 
public class HuntBehaviour extends WanderBehaviour {

	@Override
	public boolean execute(Cell c) {
		if (!(c.properties.currentEnergy > 1)) return false;

		Vector<Tile> perception = c.getPerceptionSet();

		// first search for other cells in area
		Tile target = scour(perception);
		// if found, check if you can eat/beat them
		if (target != null) {
			// if so, check if they are far or close
			// if far, approach
			// if close, eat
			
			// and return true!
			return true;
		}
		// if not found, return (do nothing) ==> will automatically try another behaviour
		else return false;
		
		
		/* OLD
		// if not found, then just move (move behaviour)
		moveAnywhere(c);
		*/
		/*
		ArrayList<Tile> moves = c.getMoveSet();
		
		// always move right (DEBUG)
		Tile destination = moves.get(2); // 2 is right of current pos
		
		
		moveTo(destination,c);
		*/
	}
	
	// scours surrounding searching for tiles that holds a cell
	public Tile scour(Vector<Tile> tiles){
		Vector<Tile> targets = new Vector<Tile>();
		
		// if found, see if it can eat/beat any of those cells
		return chooseTarget(targets);
	}
	
	// selects a target from a list of targets that it can eat/beat
	public Tile chooseTarget(Vector<Tile> targets){
		Tile target = null;
		return target;
	}
}