package objects.behaviour;

import java.util.ArrayList;

import framework.RandomGenerator;
import framework.Tile;
import objects.Cell;

// in this behaviour the cell just goes to any location around it
public class WanderBehaviour extends Behaviour {

	@Override
	public boolean execute(Cell c) {
		if (!(c.isAlive())) {
			return false;
		}

		if (canMoveAnywhere(c)) return true;
		else return false;
	}
	
	// moves cell anywhere (if there is no cell)
	public boolean canMoveAnywhere(Cell c){
		ArrayList<Tile> moves = c.getMoveSet();
		
		if (moves.isEmpty()) return false;

		RandomGenerator gen = RandomGenerator.getInstance();
		
		int destIndex = gen.getRandom().nextInt(moves.size());
		
		Tile destination = moves.get(destIndex);
		if (c.worldRef.get() != null && c.worldRef.get().getCellAtPositionCurrent(destination.x, destination.y) == null){
			c.moveTo(destination);

			return true;
		}
		
		// if it gets here, it hasn't returned, which means it hasn't found a move
		// this means that it won't do anything here and automatically try another behaviour
		return false;
	}

}