package framework;

import java.util.ArrayList;

import objects.Cell;

public class World {

	Screen frame;
	public final int tileSize = 15;
	public final int tileCount = 40;
	

	public int xOffSet = 0;
	public int yOffSet = 0;
	
	private int iterations = 1;
	
	Tile selected;		
			
	private Tile[][] tileArray = new Tile[tileCount][tileCount];
	
	public ArrayList<Cell> currentCells = new ArrayList<Cell>();
	public ArrayList<Cell> nextCells = new ArrayList<Cell>();
	
	public boolean movingUp = false;
	public boolean movingDown = false;
	public boolean movingLeft = false;
	public boolean movingRight = false;
	
	public World(Screen f){
		frame = f;
		createTileset();
	}
	
	public void run(){
		
		while (true) {
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			moveBoardView();
			frame.repaint();
		}
	}
	
	/**
	 * populates world with cells. doesn't clear before
	 */
	public void populate() {
		// to-do: replace variables by values from settings
		int numberDifferentTypes = 2 + (int)(Math.random() * ((4 - 2) + 1));	 // 2,3 or 4 
		float percentageWorld = 0.6f; 	// fill 60% of world
		
		while (true) {
			
		}
	}

	public void iterate() {
		for (int i = 0; i < iterations; i++) {		
			for (int j = 0; j < currentCells.size(); j++){
				
				Cell c = currentCells.get(j);
				c.update();
				
			}
			
			currentCells = new ArrayList<Cell>();
			
			for (Cell nc : nextCells) {
				currentCells.add(nc);
			}
			
			nextCells = new ArrayList<Cell>();
			
			System.out.println(currentCells);
			DEBUGprintCells(currentCells);
		}
	}
	
	private void DEBUGprintCells(ArrayList<Cell> cells){
		for (Cell cell : cells){
			System.out.println(""+ cell.x + " "+ cell.y);
		}
		System.out.println("n cells =" + cells.size());
		
	}
	
	
		
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
		for (int i = 0; i < tileCount; i++) {
			for (int j = 0; j < tileCount; j++) {
				Tile t = new Tile(tileSize + tileSize * j,tileSize + tileSize * i , this);
				tileArray[i][j] = t;
			}
		}
	}
	
	public Tile[][] getTiles(){
		return tileArray;
	}
	
	public Tile getTile(int x, int y){
		
		double smallestLength = 50;
		Tile closest = null;
		
		for (Tile[] tiles : tileArray) {
			for (Tile t : tiles) {
				int DX = (t.x + xOffSet) - x;
				int DY = (t.y + yOffSet) - y;
				double length = Math.sqrt(DX *DX  + DY * DY);
				if (length < smallestLength) {
					smallestLength = length;
					closest = t;
				}
			}
		}
		
		return closest;
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
		return tileArray[tileCount - 1][tileCount - 1]; 
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
}
