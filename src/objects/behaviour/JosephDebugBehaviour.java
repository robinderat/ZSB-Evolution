package objects.behaviour;

import java.util.ArrayList;

import framework.Tile;
import objects.Cell;

public class JosephDebugBehaviour extends Behaviour {

	@Override
	public void execute(Cell c) {
		// TODO Auto-generated method stub
		ArrayList<Tile> moves = c.getMoveSet();
		c.DEBUGmoveToAllInMoveSet(moves);
	}

	@Override
	public boolean isPossible(Cell c) {
		// TODO Auto-generated method stub
		return true;
	}

}
