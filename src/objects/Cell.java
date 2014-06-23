package objects;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import framework.Tile;

public class Cell {

	public Tile location;
	Behaviour behaviour;
	Properties properties;
	public Image img;
	
	
	public Cell(Tile t){
		location = t;
		behaviour = new Behaviour();
		properties = new Properties();
		img = new ImageIcon("src/art/Cell1.png").getImage();
		t.giveCell(this);
	}
	
	public Cell(Tile t, int[] stats){
		location = t;
		behaviour = new Behaviour(stats);
		properties = new Properties(stats);
		img = new ImageIcon("src/art/Cell1.png").getImage();
		t.giveCell(this);
	}
	
	public void moveTo(Tile t){
		location = t;
	}
	
	public void eat(Cell cell){
		int energyValue = 1;
		if (properties.currentEnergy < properties.maxEnergy - energyValue){
			properties.currentEnergy += energyValue;
		}else{
			properties.currentEnergy = properties.maxEnergy;
		}
		location.c.cellsToBeRemoved.add(cell);
		location.c.removeCellsDelayed();
		
	}
	
	public void attack(Cell target){
		target.properties.currentEnergy -= properties.attack;
		if(target.properties.currentEnergy < 0){
			eat(target);
		}
	}
	
	public void action(){
		/*ArrayList<Cell> cells = new ArrayList<Cell>();
		ArrayList<Tile> tiles = getMoveSet();
		for (Tile tile : tiles) {
			if (!tile.containsCell()) {
				Cell cell = new Cell(tile);
				cells.add(cell);
				
			}
			
		}
		
		location.c.cellsToBeAdded = cells;
		*/
		
		ArrayList<Tile> moves = getMoveSet();
		Tile tile = moves.get(3);
		moveTo(tile);
	}
	
	public ArrayList<Tile> getMoveSet(){
		ArrayList<Tile> result = new ArrayList<Tile>();
		
		for (int i = 0; i < properties.speed + 1; i++) {
			int x = location.x + location.c.xOffSet + i * location.c.tileSize;	
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = location.y + location.c.yOffSet + j * location.c.tileSize;
				/*if (y + location.c.yOffSet < 200 + location.c.yOffSet) {
					
				}*/
				Tile tile = location.c.getTile(x, y);
				result.add(tile);
			}
		}
		
		for (int i = 0; i < properties.speed + 1; i++) {
			int x = location.x + location.c.xOffSet  + i * location.c.tileSize;
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = location.y + location.c.yOffSet  - j * location.c.tileSize;
				Tile tile = location.c.getTile(x, y);
				result.add(tile);
			}
		}
		
		for (int i = 0; i < properties.speed + 1; i++) {
			System.out.println(i);
			int x = location.x + location.c.xOffSet  - i * location.c.tileSize;
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = location.y + location.c.yOffSet  + j * location.c.tileSize;
				Tile tile = location.c.getTile(x, y);
				result.add(tile);
			}
		}
		
		for (int i = 0; i < properties.speed + 1; i++) {
			int x = location.x  + location.c.xOffSet  - i * location.c.tileSize;
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = location.y + location.c.yOffSet  - j * location.c.tileSize;
				Tile tile = location.c.getTile(x, y);
				result.add(tile);
			}
		}
		
		
		return result;
	}
}
