package objects;

import java.awt.Image;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import objects.behaviour.Behaviour;
import objects.behaviour.MoveRightBehaviour;
import framework.Tile;
import framework.World;

public class Cell {

	public WeakReference<Tile> locationRef;
	public Properties properties;
	public Image img;
	public ArrayList<Behaviour> behaviours;
	public boolean performingMultiTurnBehaviour; // in case we do multi turn complex behaviours
	
	public int x;
	public int y;
	
	WeakReference<World> worldRef;
	
	public Cell(World w, int locX, int locY) {
		properties = new Properties();
		img = new ImageIcon("src/art/Cell1.png").getImage();
		
		x = locX;
		y = locY;
		
		worldRef = new WeakReference<World>(w);
		
		behaviours = new ArrayList<Behaviour>();
		behaviours.add(new MoveRightBehaviour());
	}

	// moves the cell: check what it should do and then go toward that position
	public void update() {

		// possibly check if following multi-turn behaviour
		// if (performingMultiTurnBehaviour) then continue that
		
		
		
		// else:
		
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

		
		// get destination from behaviour
		// Tile dest = behaviour.getDest();
		
		
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
		worldRef.get().cellsToBeRemoved.add(cell);
		worldRef.get().removeCellsDelayed();
		
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
		/*
		Tile endTile = worldRef.get().getWorldEndPoint();
		Tile startTile = worldRef.get().getWorldStartPoint();
		
		if (x >= endTile.x) {
			x = startTile.x;
		}
		else if (y >= endTile.y) {
			y = startTile.y;
		}
		else if (x <= startTile.x) {
			x = endTile.x;
		}
		else if (y <= startTile.y) {
			y = endTile.y;
		}
		*/
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
					result.add(tile);
				}
			}
		}
		
		return result;
	}
	
	public ArrayList<Tile> getMoveSet() {
		return getTilesInRadius(properties.getSpeed());
	}
	
	public ArrayList<Tile> getPerceptionSet() {
		return getTilesInRadius(properties.getVision());
	}
}
