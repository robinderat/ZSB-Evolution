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

	public Cell(World w, int lx, int ly, int t) {
		this(w, lx, ly, t, "");
	}
	
	/**
	 * cell constructor
	 */
	public Cell(World w, int locX, int locY, int t, String DNA) {
		
		if (DNA.length() == 0) {
			properties = new Properties(); // random DNA
		} else {
			properties = new Properties(DNA);
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
			behaviours.add(new MoveAnywhereBehaviour());
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
	
	public void moveTowards(Tile target) {
		
		ArrayList<Tile> tiles = getMoveSet();
		
		double distance = 500;
		int Dx;
		int Dy;
		Tile bestTile = null;
		for (Tile tile : tiles) {
			Dx = tile.x - target.x; 
			Dy = tile.y - target.y;
			double newDistance = Math.sqrt(Dx * Dx + Dy * Dy);
			
			if (newDistance < distance) {
				distance = newDistance;
				bestTile = tile;
			}
		}
		
		//moveTo(bestTile);
		
	}
	
	public void mate(Cell cell){
		String ownDNA = properties.getDNA();
		String otherDNA = cell.properties.getDNA();
		String newDNA = worldRef.get().cBreeder.merge(ownDNA, otherDNA)[0];
		
		Cell c = new Cell(worldRef.get(), 15, 15, cell.type, newDNA);
		worldRef.get().nextCells.add(c);
		System.out.println("A baby has been made");
	}
	
	/*
	public void flee(ArrayList<Tile> danger){
		ArrayList<Tile> options = new ArrayList<Tile>();
		ArrayList<Tile> tiles = getMoveSet();
		Tile bestTile = null;
		
		for (Tile t : danger) {
			Tile proposedTile = null;
			double distance = 0;
			for (Tile tile : tiles) {
					
				int Dx = tile.x - t.x; 
				int Dy = tile.y - t.y;
				double newDistance = Math.sqrt(Dx * Dx + Dy * Dy);
					
				if (newDistance > distance) {
					distance = newDistance;
					proposedTile = tile;
				}
			}
			options.add(proposedTile);
			
			double bestDistance = 0;
			for (Tile tile : options) {
				int Dx = tile.x - t.x; 
				int Dy = tile.y - t.y;
				double newDistance = Math.sqrt(Dx * Dx + Dy * Dy);
				
				if (newDistance > bestDistance) {
					distance = newDistance;
					bestTile = tile;
				}
			}
		
		
	//	moveTo(bestTile);
	}*/

	
	// cell moves to new destination
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
