package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;
import objects.Cell;

public class MoveBehaviour extends Behaviour {

	@Override
	public void execute(Cell c) {

		ArrayList<Tile> moves = c.getMoveSet();
		
		// always move right (DEBUG)
		Tile destination = moves.get(2); // 2 is right of current pos
		
		
		c.moveTo(destination);
	}
	
	// returns true if the move generated by this behaviour is possible
	@Override
	public boolean isPossible(Cell c) {

		return c.properties.currentEnergy > 1;
	}
}