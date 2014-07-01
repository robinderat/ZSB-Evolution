package framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import objects.Cell;
import objects.breeders.CrossoverBreeder;

public class World {

	Screen frame;
	public final int TILE_SIZE = 15;
	public final int TILE_COUNT = 40;
	public final int MEMORY_SIZE = TILE_COUNT * TILE_COUNT;
	
	//Determines if iteration should be done in the while loop
	public boolean doIterate; 
	
	//Offset of the board
	public int xOffSet = 0;
	public int yOffSet = 0;
	
	//Number of cell types
	public static int maxTypes = 9;
	
	//Number of iterations should be done
	private int iterations = 1;
	
	//The selected tile
	Tile selected;		
	
	//All tiles in the world
	private Tile[][] tileArray = new Tile[TILE_COUNT][TILE_COUNT];
	
	//Creates ArrayList of cells with enough memory
	public ArrayList<Cell> currentCells = new ArrayList<Cell>(MEMORY_SIZE);
	public ArrayList<Cell> nextCells = new ArrayList<Cell>(MEMORY_SIZE);
	
	//Creates breeder object
	public CrossoverBreeder cBreeder =  new CrossoverBreeder();
	
	//If true, the board moves in that direction
	public boolean movingUp = false;
	public boolean movingDown = false;
	public boolean movingLeft = false;
	public boolean movingRight = false;
	
	//Changing cells variables
	public int lastStepCellsDied;
	public int lastStepCellsBorn;
	
	//Constructor
	public World(Screen f){
		frame = f;
		createTileset();
		doIterate = false;
		
		lastStepCellsDied = 0;
		lastStepCellsBorn = 0;
	}
	
	//Method that runs the program
	public void run(){
		
		while (true) {
			//Sleep for efficiency
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
				
			//Iterates
			if (doIterate && iterations > 0) {
				iterate();
				int newIt = iterations - 1;
				
				//Stop iterating
				if (newIt == 0) {
					doIterate = false;
					
					//Set the new starting value of iterations after iterating
					setIterations(Settings.getInstance().iterationRestAmount+1);

					frame.notifyIterationsEnd();
				}
				setIterations(iterations - 1);
			}
			//Moves the board
			moveBoardView();
			//Paints the changes
			frame.repaint();
		}
	}
	
	//Fills the world with cells
	public void populate() {
		Settings settings = Settings.getInstance();
		
		Random random = RandomGenerator.getInstance().getRandom();
		
		int minType = 1;
		int diffTypes = settings.cellTypesAmount;            
		float percentageWorldFilled = settings.fillRate; 	
		float probabilityCellGen = Math.abs(1.0f - percentageWorldFilled);
		
		boolean goOn = true;
		
		//Iterate until min threshold is reached
		while (goOn && (currentCells.size() / (float)(TILE_COUNT * TILE_COUNT)) < percentageWorldFilled) {
	
			//Follow world from left-top to bottom-right and randomly generate cells
			for (Tile[] cols : tileArray) {
				for (Tile tile : cols) {
					if (goOn && 
						getCellAtPositionCurrent(tile.x, tile.y) == null &&
						Math.random() > probabilityCellGen) {
						//Generate tile on cell with random type
						int cellType = minType + (int)(random.nextDouble() * (((minType + diffTypes) - minType - 1) + 1));						
						currentCells.add(new Cell(this, tile.x, tile.y, cellType));
					}
					goOn = (currentCells.size() / (float)(TILE_COUNT * TILE_COUNT)) < percentageWorldFilled;
				}
			}
		}

		sortCellsBySpeed(currentCells);
		System.out.println("n cells after populate: " + currentCells.size());
	}

	//Iterate one step
	public void iterate() {
		
		lastStepCellsDied = 0;
		lastStepCellsBorn = 0;
		
		//Update each cell
		for (Cell c : currentCells) {
			c.update();	
		}
		
		//Calculate death rate
		for (Cell c : currentCells) {
			if (c.isAlive() == false) {
				lastStepCellsDied++;
			}
		}
		
		StatisticManager.getInstance().takeSnapshot(this, 1);
		
		//Interesting data
		System.out.println("cells died: " + lastStepCellsDied);
		System.out.println("cells born: " + lastStepCellsBorn);
		StatisticManager.getInstance().printCellStatistics();
		
		currentCells = new ArrayList<Cell>(MEMORY_SIZE);
		 
		for (Cell nc : nextCells) {
			if (nc.isAlive()) {
				currentCells.add(nc);
			}
		}		
		
		//Sort cells according to speed.
		sortCellsBySpeed(currentCells);
		
		//Interesting data
		System.out.println("n cells alive: " + currentCells.size());
		
		nextCells = new ArrayList<Cell>(MEMORY_SIZE);
	}
	
	//Sorts the cells so fast cell will move first
	public void sortCellsBySpeed(ArrayList<Cell> cellArray) {
		Collections.sort(cellArray, new Comparator<Cell>() {
			@Override
			public int compare(Cell c1, Cell c2) {
				return Integer.signum(c2.properties.getSpeed() - c1.properties.getSpeed());
			}
		});
	}
		
	//Get number of iterations
	public int getIterations() {
		return iterations;
	}
	
	//Set number of iterations
	public void setIterations(int n) {
		if (n < 1) n = 1;
		iterations = n;
	}
	
	//Create the tiles of the world
	private void createTileset(){
		for (int i = 0; i < TILE_COUNT; i++) {
			for (int j = 0; j < TILE_COUNT; j++) {
				Tile t = new Tile(TILE_SIZE + TILE_SIZE * j,TILE_SIZE + TILE_SIZE * i , this);
				tileArray[i][j] = t;
			}
		}
	}
	
	//Returns the list of tiles
	public Tile[][] getTiles(){
		return tileArray;
	}
	
	//returns the euclidean distance between two points in world units
	public int pointDistanceInWorldUnit(int x1, int y1, int x2, int y2) {
		int DX = Math.abs((x1 + xOffSet) - x2);
		int DY = Math.abs((y1 + yOffSet) - y2);
		return (int)Math.ceil(Math.sqrt(DX * DX  + DY * DY) / TILE_SIZE);
	}
	
	
	//Returns the tile closest to a specific x and y
	public Tile getTile(int x, int y) {
		
		int startx = tileArray[0][0].x;
		int starty = tileArray[0][0].y;
		int endx = tileArray[0][TILE_COUNT -1].x; 
		int endy = tileArray[TILE_COUNT -1][0].y;
		
		int xTiles = -1;
		int yTiles = -1;
		
		//Too much to the right
		if (x > endx){
			xTiles = TILE_COUNT - 1;
		}
		
		//Too much to the left
		if (x < startx){
			xTiles = 0;
		}
		
		//Too far down
		if (y > endy){
			yTiles = TILE_COUNT - 1;
		}
		
		//Too far up
		if (y < starty){
			yTiles = 0;
		}
		
		//Inside the field on x-axis
		if (xTiles == -1) {
			xTiles = (x - startx)/TILE_SIZE;
		}
		//Inside the field on y-axis
		if (yTiles == -1) {
			yTiles = (y - starty)/TILE_SIZE;
		}
		
		return tileArray[yTiles][xTiles];
	}
	
	//Selects a tile
	public void select(Tile tile){
		selected = tile;
	}
	
	//Adds a cell
	public void addCell(Cell c){
		currentCells.add(c);
	}
	
	//Removes a cell
	public void removeCell(Cell cell) {
		currentCells.remove(cell);
	}
	
	//Returns list of cells in the current position
	public ArrayList<Cell> getCells() {
		return currentCells;
	}
	
	//Moves the board
	private void moveBoardView() {
		
		//Up
		if(movingUp){
			moveUp();
		}
		
		//Down
		if(movingDown){
			moveDown();
		}
		
		//Left
		if(movingLeft){
			moveLeft();
		}
		
		//Right
		if(movingRight){
			moveRight();
		}
	}
	
	//Moves board 5 pixels up
	private void moveUp(){
		yOffSet -= 5;
	}
	
	//Moves board 5 pixels down
	private void moveDown(){
		yOffSet += 5;
	}
	
	//Moves board 5 pixels left
	private void moveLeft(){
		xOffSet -= 5;
	}
	
	//Moves board 5 pixels right
	private void moveRight(){
		xOffSet += 5;
	}
	
	//Gets bottom right tile
	public Tile getWorldEndPoint() {
		return tileArray[TILE_COUNT - 1][TILE_COUNT - 1]; 
	}
	
	//Gets top left tile
	public Tile getWorldStartPoint() {
		return tileArray[0][0];
	}
	
	//Gets cell at x and y position from list of cells in the next turn
	public Cell getCellAtPositionNext(int px, int py) {
		for (Cell c : nextCells) {
			if (c.x == px && c.y == py){
				return c;
			}
		}
		return null;
	}
	
	//Gets cell at x and y position from list of cells in this turn
	public Cell getCellAtPositionCurrent(int px, int py) {
		for (Cell c : currentCells) {
			if (c.x == px && c.y == py) {
				return c;
			}
		}
		return null;
	}

	//Clears the world from all cells
	public void clear() {
		currentCells = new ArrayList<Cell>(MEMORY_SIZE);
	}
}
