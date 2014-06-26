package objects;

import java.awt.Image;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import objects.behaviour.*;
import framework.Settings;
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
		if (type > World.maxTypes || type < 1) {
			type = 1;
		}
		
		img = new ImageIcon("src/art/Cell" + Integer.toString(type) + ".png").getImage();
		
		isHunting = false;
		worldRef = new WeakReference<World>(w);
		
		// order may still need to be randomized based on type or DNA, although that will mean rapid extinction
		behaviours = new ArrayList<Behaviour>();

		//behaviours.add(new DEBUGCloneToSurroundingsBehaviour());
		behaviours.add(new MateBehaviour());
		behaviours.add(new HuntBehaviour());

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
		
		if (!this.isAlive()) {
			return;
		}
		
		/*for(Behaviour behaviour : behaviours){
			
			System.out.println(behaviour.toString());
		}*/
		
		// loops through each behaviour in a cell's own order, and breaks as soon as it finds one that it can do
		for(Behaviour behaviour : behaviours){
			if(behaviour.execute(this)){
				this.properties.lastBehaviour = behaviour.toString();
				break;
			}
		}
		if (this.properties.lastBehaviour.equals("")) this.properties.lastBehaviour = "ERROR";
 
	}
	
	public boolean isHungry(){
		int hungerThreshold = properties.getMaxEnergy() / 10 * 8;
		return properties.getCurrentEnergy() < hungerThreshold;
	}

	// cell eats another cell and fills current energy up to max if possible
	public void eat(Cell target) {
		
		int attackValue = properties.getStrength();
		if (attackValue <= 0) {
			attackValue = 1;
		}
		target.properties.setCurrentEnergy(target.properties.getCurrentEnergy() - attackValue);
		if (target.isAlive() == false) {
			//System.out.println("Cell eaten.");
			
			float eatModifier = Settings.getInstance().eatingEnergyGain;
			
			int energyValue = (int) Math.ceil(target.properties.getStrength() * eatModifier); // this is hardcoded, should really be taken from target cell or something
			if (energyValue <= 0) {
				energyValue = 1;
			}
			properties.setCurrentEnergy(properties.getCurrentEnergy() + energyValue);
		}
	}
	
	// creates a new cell using dna of two mating cells
	public boolean mate(Cell part){
		
		String ownDNA = properties.getDNA();
		String otherDNA = part.properties.getDNA();
		String newDNA = worldRef.get().cBreeder.breed(ownDNA, otherDNA)[0];
		
		ArrayList<Tile> tiles = getFreeNeighbours();
		if(tiles.size() > 0 && tiles.get(0).worldRef.get().getCellAtPositionNext(tiles.get(0).x,tiles.get(0).y)==null){
			
			double energyCost = Settings.getInstance().matingEnergyCost;

			int energyLostCellPart = (int)Math.ceil(part.properties.getMaxEnergy() * energyCost);
			if (energyLostCellPart <= 0) {
				energyLostCellPart = 1;
			}
			
			int energyLostCell2 = (int)Math.ceil(properties.getMaxEnergy() * energyCost);
			if (energyLostCell2 <= 0) {
				energyLostCell2 = 1;
			}  
			
			//System.out.println("partner energy: " + part.properties.getCurrentEnergy());
			//System.out.println("energyLostCell Partner: " + energyLostCellPart);
			//System.out.println("own energy: " + properties.getCurrentEnergy());
			//System.out.println("own energy lost: " + energyLostCell2);
			
			part.properties.setCurrentEnergy(part.properties.getCurrentEnergy() - energyLostCellPart);
			properties.setCurrentEnergy(properties.getCurrentEnergy() - energyLostCell2);
			
			Cell c = new Cell(worldRef.get(), tiles.get(0).x, tiles.get(0).y, type, newDNA);

			worldRef.get().nextCells.add(c);
			//System.out.println("New cell born.");
			
			worldRef.get().lastStepCellsBorn++;
		
			return true;
		}
		//System.out.println("No space for newborn cell.");
		return false;
	}

	/*
	// changes target so that it is closer to where the cell can go
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
	}*/
	
	// cell moves to new destination
	public boolean moveTo(Tile destination) {
		//System.out.println("destx " + destination.x + " desty " + destination.y);
		boolean madeItInOneMove = true;
		
		// loop towards until a move can be made
		while (!this.getMoveSet().contains(destination)){
			madeItInOneMove = false;
			
			ArrayList<Tile> tiles = getMoveSet();
			
			double distance = 500;
			int Dx;
			int Dy;
			Tile bestTile = null;
			for (Tile tile : tiles) {
				Dx = tile.x - destination.x; 
				Dy = tile.y - destination.y;
				double newDistance = Math.sqrt(Dx * Dx + Dy * Dy);
				
				if (newDistance < distance) {
					distance = newDistance;
					bestTile = tile;
				}
			}
			if (bestTile != null) destination = bestTile;
			else {
				// in this special case, the cell cannot move towards its destination
				holdPosition();
				return false;
			}
		}
		// decrease energy
		int dist = worldRef.get().pointDistanceInWorldUnit(x, y, destination.x, destination.y);
		
		dist += (int)Math.ceil(dist * Settings.getInstance().moveStrengthModifier);
		if (dist <= 0) {
			dist = 1;
		}
		properties.setCurrentEnergy(properties.getCurrentEnergy() - dist);
	
		// update position
		x = destination.x;
		y = destination.y;
		
		// we dont kill him here. even if energy reaches 0, we let him live one more iteration
		worldRef.get().nextCells.add(this);

		return madeItInOneMove;
	}
	
	// the cell stays in the same spot. it will lose energy though because it is getting hungry
	public void holdPosition() {
		int potDec = (int)Math.ceil(Settings.getInstance().moveStrengthModifier * this.properties.getStrength());
		if (potDec <= 0) {
			potDec = 1;
		}
		this.properties.setCurrentEnergy(this.properties.getCurrentEnergy() - potDec);
		
		// if still alive add to next state of world
		this.worldRef.get().nextCells.add(this);
	}
	
	
	private ArrayList<Tile> getFreeNeighbours() {
		
		ArrayList<Tile> neighbours = new ArrayList<Tile>();
		World world = worldRef.get();
		Tile t = null;
		
		//System.out.println(this.x + " " + this.y + " me");
		
		 t = world.getTile(x - 1 * world.TILE_SIZE , y); 
		if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) neighbours.add(t);
		
		 t = world.getTile(x + 1 * world.TILE_SIZE , y); 
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) neighbours.add(t);
		 
		 t = world.getTile(x, y - 1 * world.TILE_SIZE);
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) neighbours.add(t);
		 
		 t = world.getTile(x, y + 1 * world.TILE_SIZE);
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) neighbours.add(t);
		 
		// for (Tile n : neighbours) System.out.println(n.x + " " + n.y);
		//System.out.println(neighbours);
		
		return neighbours;
	}
	
	public Tile getClosestFreeNeighbour(int askx, int asky){
		
		ArrayList<Tile> neighbours = new ArrayList<Tile>();
		World world = worldRef.get();
		Tile t = null;
		
		 t = world.getTile(x - 1 * world.TILE_SIZE , y); 
	     if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null && world.getCellAtPositionNext(t.x,t.y)==null) neighbours.add(t);
		
		 t = world.getTile(x + 1 * world.TILE_SIZE , y); 
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null && world.getCellAtPositionNext(t.x,t.y)==null) neighbours.add(t);
		 
		 t = world.getTile(x, y - 1 * world.TILE_SIZE);
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null && world.getCellAtPositionNext(t.x,t.y)==null) neighbours.add(t);
		 
		 t = world.getTile(x, y + 1 * world.TILE_SIZE);
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null && world.getCellAtPositionNext(t.x,t.y)==null) neighbours.add(t);
		 
		 Tile closest = null;
		 double bestDistance = 500;
		 for (Tile tile : neighbours) {
			 int Dx = tile.x - askx;
			 int Dy = tile.y - asky;
			 double distance = Math.sqrt(Dx *Dx  + Dy * Dy);
			 
			 if (distance < bestDistance) {
				 bestDistance = distance;
				 closest = tile;
			 }
		 }
		return closest;
	}

	// returns all viable tiles in a given radius around the cell
	public ArrayList<Tile> getTilesInRadius(int rad, boolean isViewing) {

		ArrayList<Tile> result = new ArrayList<Tile>();

		for (int k = 0; k < 4; k++) {
			for (int i = 0; i < rad + 1; i++) {
				int _x;
				if (k < 2) {
					_x = x + i * worldRef.get().TILE_SIZE;	
				} else {
					_x = x - i * worldRef.get().TILE_SIZE;	
				} 
				int Dy = rad + 1 - i;
				
				for (int j = 0; j < Dy; j++) {
					int _y;
					if (k % 2 == 0) {
						_y = y +  j * worldRef.get().TILE_SIZE;
					} else {
						_y = y  - j * worldRef.get().TILE_SIZE;
					}
					//System.out.println("X: " + _x + " Y: " + _y);
					Tile tile = worldRef.get().getTile(_x, _y);
					
					if (isHunting && tile != null && tile.worldRef.get().getCellAtPositionNext(tile.x, tile.y)==null) result.add(tile);
					else {
						if (tile != null && !result.contains(tile)){
							if (isViewing) result.add(tile);
							else if (tile.worldRef.get().getCellAtPositionCurrent(tile.x, tile.y) == null && tile.worldRef.get().getCellAtPositionNext(tile.x,tile.y)==null) result.add(tile);
						}
					}
				}
			}
				
		}
		
		//System.out.println(result);
		//System.out.println(result.size());
		
		return result;
	}
	
	public ArrayList<Tile> getMoveSet() {
		int moveRad = properties.getCurrentEnergy() < properties.getSpeed() ? properties.getCurrentEnergy() : properties.getSpeed();
		
		return getTilesInRadius(moveRad, false);

	}
	
	public ArrayList<Tile> getPerceptionSet() {
		return getTilesInRadius(properties.getVision(), true);
	}

}
