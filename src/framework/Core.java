package framework;

import java.util.ArrayList;

import objects.Cell;




public class Core {

	Screen frame;
	public final int tileSize = 15;
	public final int tileNumber = 40;
	

	public int xOffSet = 0;
	public int yOffSet = 0;
	
	Tile selected;		
			
	private Tile[][] tileArray = new Tile[tileNumber][tileNumber];
	private ArrayList<Cell> cellArray = new ArrayList<Cell>();
	
	public boolean movingUp = false;
	public boolean movingDown = false;
	public boolean movingLeft = false;
	public boolean movingRight = false;
	
	public Core(Screen f){
		frame = f;
		fillArray();
	}
	public void run(){
		
		while (true) {
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			move();
			frame.repaint();
		}
	}
	
	private void fillArray(){
		for (int i = 0; i < tileNumber; i++) {
			for (int j = 0; j < tileNumber; j++) {
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
		cellArray.add(c);
	}
	
	public ArrayList<Cell> getCells(){
		
		return cellArray;
	}
	
	private void move(){
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
}
