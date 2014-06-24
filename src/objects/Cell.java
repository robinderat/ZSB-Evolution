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
	
	WeakReference<World> worldRef;
	
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
		behaviours.add(new JosephDebugBehaviour());
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
		
		// TEMPORARY TEST LINE : Always make first behaviour the chosen one :
		behaviour = behaviours.get(0);

		// go there
		behaviour.execute(this);
	}
	
	public void DEBUGmoveToAllInMoveSet(ArrayList<Tile> moveSet, Cell oldCell){
		for (Tile tile : moveSet) {
			if (worldRef.get().getCellAtPositionNext(tile.x, tile.y) == null &&
				worldRef.get().getCellAtPositionCurrent(tile.x, tile.y) == null) { 
				Cell cell = new Cell(worldRef.get(), tile.x, tile.y, oldCell.type, null);
				
				
//				locationRef.get().coreRef.get().removeCellsDelayed();
				worldRef.get().nextCells.add(cell);
//				locationRef.get().coreRef.get().addCellsDelayed();
			}
		}
//		locationRef.get().coreRef.get().removeCellsDelayed();
//		locationRef.get().coreRef.get().addCellsDelayed();
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
	
	public void moveTo(Tile destination) {
		x = destination.x;
		y = destination.y;
		worldRef.get().nextCells.add(this);

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
