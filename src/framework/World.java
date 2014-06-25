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
	
	public boolean doIterate; // bool if iteration should be done in while loop
	
	public int xOffSet = 0;
	public int yOffSet = 0;
	
	public static int maxTypes = 9; // voor koen
	
	private int iterations = 1;
	
	Tile selected;		
			
	private Tile[][] tileArray = new Tile[TILE_COUNT][TILE_COUNT];
	
	// create ArrayList of cells with enough memory
	public ArrayList<Cell> currentCells = new ArrayList<Cell>(MEMORY_SIZE);
	public ArrayList<Cell> nextCells = new ArrayList<Cell>(MEMORY_SIZE);
	
	public CrossoverBreeder cBreeder =  new CrossoverBreeder();
	
	public boolean movingUp = false;
	public boolean movingDown = false;
	public boolean movingLeft = false;
	public boolean movingRight = false;
	
	public World(Screen f){
		frame = f;
		createTileset();
		doIterate = false;
	}
	
	public void run(){
		
		while (true) {
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			if (doIterate && iterations > 0) {
				iterate();
				int newIt = iterations - 1;
				// done with iterations
				if (newIt == 0) {
					doIterate = false;
				}
				setIterations(iterations - 1);
				
			}
			moveBoardView();
			frame.repaint();
		}
	}
	
	/**
	 * populates world with cells. doesn't clear before
	 */
	public void populate() {
		Settings settings = Settings.getInstance();
		
		// to-do: replace variables by values from settings
		Random random = RandomGenerator.getInstance().getRandom();
		
		int minType = 1;
		int diffTypes = 2 + (int)(random.nextDouble() * ((maxTypes - 2) + 1));	 // 2,3 or 4 
		float percentageWorldFilled = settings.fillRate; 	// fill 60% of world
		
		System.out.println("different types: " + diffTypes);
		
		float probabilityCellGen = Math.abs(1.0f - percentageWorldFilled); // 30% prob that a cell gets placed on a tile
		
		int cellCount = 0;
		
		boolean goOn = true;
		// we iterate until min threshold is reached
		while (goOn && (currentCells.size() / (float)(TILE_COUNT * TILE_COUNT)) < percentageWorldFilled) {
	
			// walk world from left-top to bottom-down and randomly generate cells
			for (Tile[] cols : tileArray) {
				for (Tile tile : cols) {
					if (goOn && 
						getCellAtPositionCurrent(tile.x, tile.y) == null &&
						Math.random() > probabilityCellGen) {
						// generate tile on cell
						// choose randomly a type
						int cellType = minType + (int)(random.nextDouble() * (((minType + diffTypes) - minType - 1) + 1));						
						currentCells.add(new Cell(this, tile.x, tile.y, cellType));
						cellCount ++;
					}
					//System.out.println((currentCells.size() / (float)(tileCount * tileCount)));
					goOn = (currentCells.size() / (float)(TILE_COUNT * TILE_COUNT)) < percentageWorldFilled;
				}
			}
		}

		sortCellsBySpeed(currentCells);
	}

	/*
	 * do one world step
	 */
	public void iterate() {
		
		for (int j = 0; j < currentCells.size(); j++){
			Cell c = currentCells.get(j);
			c.update();	
		}
		
		StatisticManager.getInstance().takeSnapshot(this, 1);
		
		StatisticManager.getInstance().printCellStatistics();
		
		currentCells = new ArrayList<Cell>(MEMORY_SIZE);
		
		for (Cell nc : nextCells) {
			if (nc.isAlive()) {
				currentCells.add(nc);
			}
		}
		
		// sort cells according to speed.
		// that will guarantee that fast cells move first
		sortCellsBySpeed(currentCells);
		//for (Cell c : currentCells) { System.out.println(c.properties.getSpeed()); }
		
		System.out.println("n cells alive: " + currentCells.size());
		
		nextCells = new ArrayList<Cell>(MEMORY_SIZE);
	}
	
	public void sortCellsBySpeed(ArrayList<Cell> cellArray) {
		Collections.sort(cellArray, new Comparator<Cell>() {
			@Override
			public int compare(Cell c1, Cell c2) {
				return Integer.signum(c2.properties.getSpeed() - c1.properties.getSpeed());
			}
		});
	}
	
	/*
	private void DEBUGprintCells(ArrayList<Cell> cells){
		for (Cell cell : cells){
			System.out.println(""+ cell.x + " "+ cell.y);
		}
		System.out.println("n cells =" + cells.size());
	}
	*/
		
	/**
	 * getter iterations
	 */
	public int getIterations() {
		return iterations;
	}
	
	/**
	 * set iterations
	 */
	public void setIterations(int n) {
		if (n < 1) n = 1;
		iterations = n;
	}
	
	/**
	 * creates all tiles
	 */
	private void createTileset(){
		for (int i = 0; i < TILE_COUNT; i++) {
			for (int j = 0; j < TILE_COUNT; j++) {
				Tile t = new Tile(TILE_SIZE + TILE_SIZE * j,TILE_SIZE + TILE_SIZE * i , this);
				tileArray[i][j] = t;
			}
		}
	}
	
	public Tile[][] getTiles(){
		return tileArray;
	}
	
	/*
	 * returns euclidean distance between two points in world units
	 */
	public int pointDistanceInWorldUnit(int x1, int y1, int x2, int y2) {
		int DX = Math.abs((x1 + xOffSet) - x2);
		int DY = Math.abs((y1 + yOffSet) - y2);
		return (int)Math.ceil(Math.sqrt(DX * DX  + DY * DY) / TILE_SIZE);
	}
	
	public Tile getTile(int x, int y) {
		
		int startx = tileArray[0][0].x;
		int starty = tileArray[0][0].y;
		int endx = tileArray[0][TILE_COUNT -1].x;
		int endy = tileArray[TILE_COUNT -1][0].y;
		
		int xTiles = -1;
		int yTiles = -1;
		
		if (x > endx){
			xTiles = TILE_COUNT - 1;
		}
		if (x < startx){
			xTiles = 0;
		}
		if (y > endy){
			yTiles = TILE_COUNT - 1;
		}
		if (y < starty){
			yTiles = 0;
		}
		if (xTiles == -1) {
			xTiles = (x - startx - xOffSet)/TILE_SIZE;
		}
		if (yTiles == -1) {
			yTiles = (y - starty - yOffSet)/TILE_SIZE;
		}
		
		return tileArray[yTiles][xTiles];
	}
	
	public void select(Tile tile){
		selected = tile;
	}
	
	public void addCell(Cell c){

		currentCells.add(c);
	}
	
	public ArrayList<Cell> getCells() {
		return currentCells;
	}
	
	private void moveBoardView() {
		if(movingUp){
			moveUp();
		}
		if(movingDown){
			moveDown();
		}
		if(movingLeft){
			moveLeft();
		}
		if(movingRight){
			moveRight();
		}
	}
	
	private void moveUp(){
		yOffSet -= 5;
	}
	
	private void moveDown(){
		yOffSet += 5;
	}
	
	private void moveLeft(){
		xOffSet -= 5;
	}
	
	private void moveRight(){
		xOffSet += 5;
	}
	
	public Tile getWorldEndPoint() {
		return tileArray[TILE_COUNT - 1][TILE_COUNT - 1]; 
	}
	
	public Tile getWorldStartPoint() {
		return tileArray[0][0];
	}
	
	public Cell getCellAtPositionNext(int px, int py) {
		for (Cell c : nextCells) {
			if (c.x == px && c.y == py) return c;
		}
		return null;
	}
	
	public Cell getCellAtPositionCurrent(int px, int py) {
		for (Cell c : currentCells) {
			if (c.x == px && c.y == py) return c;
		}
		return null;
	}

	// clears the world
	public void clear() {
		currentCells = new ArrayList<Cell>(MEMORY_SIZE);
	}
	
	public void removeCell(Cell cell) {
		currentCells.remove(cell);
	}
}
