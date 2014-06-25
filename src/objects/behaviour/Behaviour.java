package objects.behaviour;

import objects.Cell;

public abstract class Behaviour {
	public abstract boolean execute(Cell c);
	
	@Override public String toString(){
		return this.getClass().toString().substring(24);
	}
}
