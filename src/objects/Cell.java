package objects;

import java.awt.Image;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import objects.behaviour.*;
import framework.RandomGenerator;
import framework.Settings;
import framework.Tile;
import framework.World;

public class Cell {

	//Properties of cell. 
	public Properties properties;
	
	//Image of cell
	public Image img;
	
	//List of behaviours available to cell
	public ArrayList<Behaviour> behaviours;
	
	//Current location
	public int x;			
	public int y;
	
	//Type of cell
	public int type; 
	
	public boolean isHunting;
	
	//World reference to access information from the world
	public WeakReference<World> worldRef;
	
	//Constructor for cell with random DNA
	public Cell(World w, int lx, int ly, int t) {
		this(w, lx, ly, t, "");
	}
	
	//Constructor for cell with equal properties as given cell
	public Cell(Cell other) {
		this(other.worldRef.get(), other.x, other.y, other.type, other.properties.getDNA());
	}
	
	//Constructor
	public Cell(World w, int locX, int locY, int t, String DNA) {

		//No DNA given
		if (DNA.length() == 0) {
			//Random DNA
			properties = new Properties(); 
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
		
		behaviours = new ArrayList<Behaviour>();

		//Adds behaviours to behaviourlist
		//The order in which they are added determines the priorities of the cell
		behaviours.add(new MateBehaviour());
		behaviours.add(new HuntBehaviour());
		behaviours.add(new FleeBehaviour());
		behaviours.add(new WanderBehaviour());
		behaviours.add(new StayBehaviour());
	}

	//Checks if the cell is not dead
	public boolean isAlive() {
		
		if (properties.getCurrentEnergy() < 0) {
			properties.setCurrentEnergy(0);
		}
		return properties.getIsAlive();
	}
	
	//Updates the cell in iteration
	public void update() {
		
		if (!this.isAlive()) {
			return;
		}
		
		//Loops through each behaviour and stops as soon as it finds one it can execute
		for(Behaviour behaviour : behaviours){
			if(behaviour.execute(this)){
				this.properties.lastBehaviour = behaviour.toString();
				break;
			}
		}
		if (this.properties.lastBehaviour.equals("")) this.properties.lastBehaviour = "ERROR";
	}
	
	//Checks if a cell is hungry
	public boolean isHungry(){
		int hungerThreshold = properties.getMaxEnergy() / 10 * 8;
		return properties.getCurrentEnergy() < hungerThreshold;
	}

	//Cell attacks another cell
	public void stab(Cell target) {
		
		int attackValue = properties.getStrength();
		if (attackValue <= 0) {
			attackValue = 1;
		}
		target.properties.setCurrentEnergy(target.properties.getCurrentEnergy() - attackValue);
		//If the target dies, the cell eats it and regains energy
		if (target.isAlive() == false) {			
			float eatModifier = Settings.getInstance().eatingEnergyGain;
			
			int energyValue = (int) Math.ceil(target.properties.getStrength() * eatModifier);
			if (energyValue <= 0) {
				energyValue = 1;
			}
			properties.setCurrentEnergy(properties.getCurrentEnergy() + energyValue);
		}
	}
	
	//Creates a new cell using DNA of two mating cells
	public boolean mate(Cell part){
		RandomGenerator random = RandomGenerator.getInstance();
		
		String ownDNA = properties.getDNA();
		String otherDNA = part.properties.getDNA();
		String newDNA = worldRef.get().cBreeder.breed(ownDNA, otherDNA)[random.getRandom().nextInt(2)];
		
		ArrayList<Tile> tiles = getFreeNeighbours();
		//If there are tiles next to this one
		if (!tiles.isEmpty()) {
			
			RandomGenerator gen = RandomGenerator.getInstance();
			
			int destIndex = gen.getRandom().nextInt(tiles.size());
			
			Tile babySpot = tiles.get(destIndex);
			
			//If the tile is empty
			if (babySpot.worldRef.get().getCellAtPositionNext(babySpot.x,babySpot.y) == null){
				
				double energyCost = Settings.getInstance().matingEnergyCost;
	
				int energyLostCellPart = (int)Math.ceil(part.properties.getMaxEnergy() * energyCost);
				if (energyLostCellPart <= 0) {
					energyLostCellPart = 1;
				}
				
				int energyLostCell2 = (int)Math.ceil(properties.getMaxEnergy() * energyCost);
				if (energyLostCell2 <= 0) {
					energyLostCell2 = 1;
				}  
				
				part.properties.setCurrentEnergy(part.properties.getCurrentEnergy() - energyLostCellPart);
				properties.setCurrentEnergy(properties.getCurrentEnergy() - energyLostCell2);
				
				//Creates a new cell
				Cell c = new Cell(worldRef.get(), babySpot.x, babySpot.y, type, newDNA);
	
				//Adds the cell to the new world
				worldRef.get().nextCells.add(c);
				
				worldRef.get().lastStepCellsBorn++;
			
				return true;
			}
		}
		return false;
	}

	//Cell moves to new destination
	public boolean moveTo(Tile destination) {
		boolean madeItInOneMove = true;
		
		//Loop towards until a move can be made
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
				//In this case, the cell cannot move towards its destination
				holdPosition();
				return false;
			}
		}
		
		int dist = worldRef.get().pointDistanceInWorldUnit(x, y, destination.x, destination.y);
		
		dist += (int)Math.ceil(dist * Settings.getInstance().moveStrengthModifier);
		if (dist <= 0) {
			dist = 1;
		}
		//Decrease energy based on movement
		properties.setCurrentEnergy(properties.getCurrentEnergy() - dist);
	
		//Update position
		x = destination.x;
		y = destination.y;
		
		//We dont kill him here. even if energy reaches 0, we let him live one more iteration
		worldRef.get().nextCells.add(this);

		return madeItInOneMove;
	}
	
	//The cell stays in the same spot
	//It will lose energy because it is getting hungry
	public void holdPosition() {
		int potDec = (int)Math.ceil(Settings.getInstance().moveStrengthModifier * this.properties.getStrength());
		if (potDec <= 0) {
			potDec = 1;
		}
		this.properties.setCurrentEnergy(this.properties.getCurrentEnergy() - potDec);
		
		//If still alive, add to next state of world
		this.worldRef.get().nextCells.add(this);
	}
	
	//Get a list of free tiles next to this one
	private ArrayList<Tile> getFreeNeighbours() {
		
		ArrayList<Tile> neighbours = new ArrayList<Tile>();
		World world = worldRef.get();
		Tile t = null;
		
		 t = world.getTile(x - 1 * world.TILE_SIZE , y); 
		if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) {
			neighbours.add(t);
		}
		
		 t = world.getTile(x + 1 * world.TILE_SIZE , y); 
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) {
			 neighbours.add(t);
		 }
		 
		 t = world.getTile(x, y - 1 * world.TILE_SIZE);
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) {
			 neighbours.add(t);
		 }
		 
		 t = world.getTile(x, y + 1 * world.TILE_SIZE);
		 if(t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) {
			 neighbours.add(t);
		 }
		 
		return neighbours;
	}
	
	public Tile getClosestFreeNeighbour(int askx, int asky){
		
		ArrayList<Tile> neighbours = getFreeNeighbours();
		 
		 Tile closest = null;
		 
		 //Just a number bigger than any neighbour will be
		 double bestDistance = 500;
		 //For every tile, get the distance to the cell asking for the closest tile
		 for (Tile tile : neighbours) {
			 int Dx = tile.x - askx;
			 int Dy = tile.y - asky;
			 double distance = Math.sqrt(Dx *Dx  + Dy * Dy);
			 
			 //If the distance is smaller, this tile is better
			 if (distance < bestDistance) {
				 bestDistance = distance;
				 closest = tile;
			 }
		 }
		return closest;
	}

	//Returns all viable tiles in a given radius around the cell
	public ArrayList<Tile> getTilesInRadius(int rad, boolean isViewing) {

		ArrayList<Tile> result = new ArrayList<Tile>();

		//For every quadrant
		for (int k = 0; k < 4; k++) {
			//For the full distance of the radius
			for (int i = 0; i < rad + 1; i++) {
				int _x;
				//The right side
				if (k < 2) {
					_x = x + i * worldRef.get().TILE_SIZE;	
				//The left side
				} else {
					_x = x - i * worldRef.get().TILE_SIZE;	
				} 
				//The tiles up/down
				int Dy = rad + 1 - i;
				
				for (int j = 0; j < Dy; j++) {
					int _y;
					//The tiles down
					if (k % 2 == 0) {
						_y = y +  j * worldRef.get().TILE_SIZE;
					//The tiles up
					} else {
						_y = y  - j * worldRef.get().TILE_SIZE;
					}
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
		return result;
	}
	
	//Gets all tiles a cell can move towards
	public ArrayList<Tile> getMoveSet() {
		int moveRad = properties.getCurrentEnergy() < properties.getSpeed() ? properties.getCurrentEnergy() : properties.getSpeed();
		
		return getTilesInRadius(moveRad, false);

	}
	
	//Gets all tiles a cell can see
	public ArrayList<Tile> getPerceptionSet() {
		return getTilesInRadius(properties.getVision(), true);
	}
}
