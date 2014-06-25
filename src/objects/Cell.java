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

	// properties of cell. 
	public Properties properties;
	
	public Image img;
	
	// list of behaviours available to cell
	public ArrayList<Behaviour> behaviours;
	
	// location
	public int x;			
	public int y;
	
	public int type; // type of cell
	
	public boolean isHunting;
	
	// reference back to the world
	public WeakReference<World> worldRef;
	
	/**
	 * constructor which generates random DNA
	 */
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
		
		isHunting = false;
		
		worldRef = new WeakReference<World>(w);
		
		behaviours = new ArrayList<Behaviour>();

		// special behavior for type 2 for DEBUG testing purposes
		if (type == 2) behaviours.add(new HoldPositionBehaviour());
		else {
		// dump all behaviours for normal type (not finished: since some types get only some behaviours)
		//
		// !! note: we want to give certain (initial) types certain (initial) behaviours
		// !! new note: OR (better?) randomize certain behaviours to all cells
		//
		// the below order is the optimal order. NOTE: right now all cells (save type 2) have this order!!
			//behaviours.add(new DEBUGCloneToSurroundingsBehaviour());
			behaviours.add(new HuntBehaviour());
			behaviours.add(new FleeBehaviour());
			behaviours.add(new ApproachCenterBehaviour());
			behaviours.add(new ApproachBorderBehaviour());
			behaviours.add(new MoveAnywhereBehaviour());
			behaviours.add(new HoldPositionBehaviour());


		}
	}

	/*
	 * updates cell in iteration
	 */
	public void update() {

		// DEBUG : Turning off expiration- uncomment the following line to turn back on		
		//if (properties.currentEnergy <= 0) return;
		
		// loops through each behaviour in a cell's own order, and breaks as soon as it finds one that it can do
		for(Behaviour behaviour : behaviours){
			if(behaviour.execute(this)){
				break;
			}
		}
 
	}

	// cell eats another cell and fills current energy up to max if possible
	public void eat(Cell target) {
		int energyValue = 20; // this is hardcoded, should really be taken from target cell or something
		if (properties.currentEnergy < properties.getMaxEnergy() - energyValue) {
			properties.currentEnergy += energyValue;
		} else {
			properties.currentEnergy = properties.getMaxEnergy();
		}
		worldRef.get().currentCells.remove(target);
	}
	
	// 
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
		
		moveTo(bestTile);
	}
	
	public void mate(Cell cell){
		String ownDNA = properties.getDNA();
		String otherDNA = cell.properties.getDNA();
		String newDNA = worldRef.get().cBreeder.merge(ownDNA, otherDNA)[0];
		
		Cell c = new Cell(worldRef.get(), 15, 15, cell.type, newDNA);
		worldRef.get().nextCells.add(c);
		System.out.println("A baby has been made");
	}
	
	// cell moves to new destination
	public void moveTo(Tile destination) {
		
		if (getMoveSet().contains(destination)) {
			// decrease energy
			int dist = worldRef.get().pointDistanceInWorldUnit(x, y, destination.x, destination.y);
			properties.currentEnergy -= dist;
		
			// update position
			x = destination.x;
			y = destination.y;
			
			// we dont kill him here. even if energy reaches 0, we let him live one more iteration
			worldRef.get().nextCells.add(this);

		} else {
			// will call moveTo again. 
			moveTowards(destination);	
		}		
	}
	
	public ArrayList<Tile> getTilesInRadius(int rad) {
		ArrayList<Tile> result = new ArrayList<Tile>();

		for (int k = 0; k < 4; k++) {
			for (int i = 0; i < rad + 1; i++) {
				int _x;
				if (k < 2) {
					_x = x + worldRef.get().xOffSet + i * worldRef.get().TILE_SIZE;	
				} else {
					_x = x + worldRef.get().xOffSet - i * worldRef.get().TILE_SIZE;	
				} 
				int Dy = rad + 1 - i;
				
				for (int j = 0; j < Dy; j++) {
					int _y;
					if (k % 2 == 0) {
						_y = y + worldRef.get().yOffSet + j * worldRef.get().TILE_SIZE;
					} else {
						_y = y + worldRef.get().yOffSet - j * worldRef.get().TILE_SIZE;
					}
					
					Tile tile = worldRef.get().getTile(_x, _y);
					
					if (isHunting && tile != null) result.add(tile);
					else if (tile != null && result.contains(tile) == false) result.add(tile);
				}
			}
		}
		
		//System.out.println(result);
		//System.out.println(result.size());
		
		return result;
	}
	
	public ArrayList<Tile> getMoveSet() {
		
		int moveRad = properties.currentEnergy < properties.getSpeed() ? properties.currentEnergy : properties.getSpeed();
		
		return getTilesInRadius(moveRad);
	}
	
	public ArrayList<Tile> getPerceptionSet() {
		return getTilesInRadius(properties.getVision());
	}

}
