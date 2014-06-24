package objects;

import java.awt.Image;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import objects.behaviour.*;
import framework.Tile;
import framework.World;

public class Cell {

	public static final int MaxTypes = 6;
	
	public Properties properties;
	public Image img;
	public ArrayList<Behaviour> behaviours;
	//public boolean performingMultiTurnBehaviour; // in case we do multi turn complex behaviours
	
	public int x;
	public int y;
	public int type;
	
	public WeakReference<World> worldRef;
	
	/**
	 * cell constructor
	 */
	public Cell(World w, int locX, int locY, int t, Properties p) {
		properties = p;
		if (properties == null) {
			properties = new Properties(); // random DNA
		}
		
		x = locX;
		y = locY;
		
		type = t;
		if (type > MaxTypes || type < 1) {
			type = 1;
		}
		
		img = new ImageIcon("src/art/Cell" + Integer.toString(type) + ".png").getImage();
		
		worldRef = new WeakReference<World>(w);
		
		behaviours = new ArrayList<Behaviour>();

		// special behavior for type 2 for DEBUG testing purposes
		if (type == 2) behaviours.add(new HoldPositionBehaviour());
		else {
		// dump all behaviours for normal type (not finished: since some types get only some behaviours)
			behaviours.add(new DEBUGCloneToSurroundingsBehaviour());
			behaviours.add(new HoldPositionBehaviour());
			behaviours.add(new HuntBehaviour());
			behaviours.add(new MoveBehaviour());
		}
	}

	// moves the cell: check what it should do and then go toward that position
	public void update() {

		// possibly check if following multi-turn behaviour
		// if (performingMultiTurnBehaviour) then continue that
		
		Behaviour behaviour;
		// pick behaviour from behaviours. picks first possible one from list
		for (int i = 0; i < behaviours.size(); i++) {
			// if (behaviours.get(i).isPossible()) {
				// behaviour = behaviours.get(i);
				// break;
			//}
		}
		
		// DEBUG testing hunt purposes
		if (type == 2) behaviour = behaviours.get(0);
		// TEMPORARY TEST LINE : Always make first behaviour the chosen one :
		else behaviour = behaviours.get(2); // 2 is hunt behaviour

		// go there
		behaviour.execute(this);
	}


	public void eat(Cell cell) {
		int energyValue = 20;
		if (properties.currentEnergy < properties.getMaxEnergy() - energyValue) {
			properties.currentEnergy += energyValue;
		} else {
			properties.currentEnergy = properties.getMaxEnergy();
		}		
	}
	
	public void attack(Cell target) {
		target.properties.currentEnergy -= properties.getStrength();
		if(target.properties.currentEnergy < 0) {
			eat(target);
		}
	}


	public ArrayList<Tile> getTilesInRadius(int rad) {
		ArrayList<Tile> result = new ArrayList<Tile>();

		for (int k = 0; k < 4; k++) {
			for (int i = 0; i < rad + 1; i++) {
				int _x;
				if (k < 2) {
					_x = x + worldRef.get().xOffSet + i * worldRef.get().tileSize;	
				} else {
					_x = x + worldRef.get().xOffSet - i * worldRef.get().tileSize;	
				} 
				int Dy = rad + 1 - i;
				
				for (int j = 0; j < Dy; j++) {
					int _y;
					if (k % 2 == 0) {
						_y = y + worldRef.get().yOffSet + j * worldRef.get().tileSize;
					} else {
						_y = y + worldRef.get().yOffSet - j * worldRef.get().tileSize;
					}
					
					Tile tile = worldRef.get().getTile(_x, _y);
					if (result.contains(tile) == false) {
						result.add(tile);
					}
				}
			}
		}
		
		System.out.println(result);
		System.out.println(result.size());
		
		return result;
	}
	
	public ArrayList<Tile> getMoveSet() {
		return getTilesInRadius(properties.getSpeed());
	}
	
	public ArrayList<Tile> getPerceptionSet() {
		return getTilesInRadius(properties.getVision());
	}

}
