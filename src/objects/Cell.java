package objects;

import java.awt.Image;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import objects.behaviour.*;
import framework.Tile;
import framework.World;

public class Cell {

	//public static final int MaxTypes = 9;

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
	 * copy constructor
	 */
	public Cell(Cell other) {
		this(other.worldRef.get(), other.x, other.y, other.type, other.properties.getDNA());
	}
	
	/**
	 * cell constructor
	 */
	public Cell(World w, int locX, int locY, int t, String DNA) {
		//System.out.println(DNA);
		if (DNA.length() == 0) {
			properties = new Properties(); // random DNA
		} else {
			properties = new Properties(DNA);
		}
		
		x = locX;
		y = locY;
		
		type = t;
		if (type > w.maxTypes || type < 1) {
			type = 1;
		}
		
		img = new ImageIcon("src/art/Cell" + Integer.toString(type) + ".png").getImage();
		
		isHunting = false;
		
		worldRef = new WeakReference<World>(w);
		
		behaviours = new ArrayList<Behaviour>();

		
		behaviours.add(new HuntBehaviour());
		//behaviours.add(new MateBehaviour());
		behaviours.add(new FleeBehaviour());
		//behaviours.add(new ApproachCenterBehaviour());
		//behaviours.add(new ApproachBorderBehaviour());
		behaviours.add(new WanderBehaviour());
		behaviours.add(new StayBehaviour());
	}

	public boolean isAlive() {
		// i think this can be removed. lets check it later
		if (properties.getCurrentEnergy() < 0) {
			properties.setCurrentEnergy(0);
		}
		return properties.getIsAlive();
	}
	
	/*
	 * updates cell in iteration
	 */
	public void update() {

		if (!isAlive()) {
			return;
		}
		
		// loops through each behaviour in a cell's own order, and breaks as soon as it finds one that it can do
		for(Behaviour behaviour : behaviours){
			if(behaviour.execute(this)){
				break;
			}
		}
 
	}

	// cell eats another cell and fills current energy up to max if possible
	public void eat(Cell target) {
		
		int attackValue = properties.getStrength();
		if (attackValue <= 0) {
			attackValue = 1;
		}
		target.properties.setCurrentEnergy(target.properties.getCurrentEnergy() - attackValue);
		if (target.isAlive() == false) {
			System.out.println("cell eats target");
			
			int energyValue = (int) Math.ceil(target.properties.getStrength() * 0.5f); // this is hardcoded, should really be taken from target cell or something
			if (energyValue <= 0) {
				energyValue = 1;
			}
			
			properties.setCurrentEnergy(properties.getCurrentEnergy() + energyValue);
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
		
		if (bestTile != null) {
			moveTo(bestTile);
		}
	}
	
	public boolean mate(Cell cell){
		
		String ownDNA = properties.getDNA();
		String otherDNA = cell.properties.getDNA();
		String newDNA = worldRef.get().cBreeder.merge(ownDNA, otherDNA)[0];
		
		ArrayList<Tile> tiles = getFreeNeighbours();
		if(tiles.size() != 0){
			properties.setCurrentEnergy(cell.properties.getCurrentEnergy() - cell.properties.getMaxEnergy() / 7 * 10);
			properties.setCurrentEnergy(properties.getCurrentEnergy() - properties.getMaxEnergy() / 7 * 10);
			
			Cell c = new Cell(worldRef.get(), tiles.get(0).x, tiles.get(0).y, cell.type, newDNA);
			worldRef.get().nextCells.add(c);
			//System.out.println("A baby has been made");
			return true;
		}
		//System.out.println("No space for another baby");
		return false;
	}
	
	

	// cell moves to new destination
	public void moveTo(Tile destination) {
		
		if (getMoveSet().contains(destination)) {
			// decrease energy
			int dist = worldRef.get().pointDistanceInWorldUnit(x, y, destination.x, destination.y);
			if (dist <= 0) {
				dist = 1;
			}
			properties.setCurrentEnergy(properties.getCurrentEnergy() - dist);
		
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
	
	private ArrayList<Tile> getFreeNeighbours() {
		
		ArrayList<Tile> neighbours = new ArrayList<Tile>();
		World world = worldRef.get();
		Tile t = null;
		
		 t = world.getTile(x - 1 * world.TILE_SIZE , y); 
		if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) neighbours.add(t);
		
		 t = world.getTile(x + 1 * world.TILE_SIZE , y); 
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) neighbours.add(t);
		 
		 t = world.getTile(x, y - 1 * world.TILE_SIZE);
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) neighbours.add(t);
		 
		 t = world.getTile(x, y + 1 * world.TILE_SIZE);
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) neighbours.add(t);
		
		
		return neighbours;
	}
	
	public Tile getClosestFreeNeighbour(int x, int y){
		
		
		ArrayList<Tile> neighbours = new ArrayList<Tile>();
		World world = worldRef.get();
		Tile t = null;
		
		 t = world.getTile(x - 1 * world.TILE_SIZE , y); 
		if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null ) neighbours.add(t);
		
		 t = world.getTile(x + 1 * world.TILE_SIZE , y); 
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) neighbours.add(t);
		 
		 t = world.getTile(x, y - 1 * world.TILE_SIZE);
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) neighbours.add(t);
		 
		 t = world.getTile(x, y + 1 * world.TILE_SIZE);
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) neighbours.add(t);
		 
		 Tile closest = null;
		 double bestDistance = 500;
		 for (Tile tile : neighbours) {
			 int Dx = tile.x - x;
			 int Dy = tile.y - y;
			 double distance = Math.sqrt(Dx *Dx  + Dy * Dy);
			 
			 if (distance < bestDistance) {
				 bestDistance = distance;
				 closest = tile;
			 }
		 }
		
		return closest;
	}
	
	public boolean canMate(){
		return true;
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
		
		int moveRad = properties.getCurrentEnergy() < properties.getSpeed() ? properties.getCurrentEnergy() : properties.getSpeed();
		
		return getTilesInRadius(moveRad);
	}
	
	public ArrayList<Tile> getPerceptionSet() {
		return getTilesInRadius(properties.getVision());
	}

}
