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
	
	public void action(){
		ArrayList<Cell> cells = new ArrayList<Cell>();
		ArrayList<Tile> tiles = getMoveSet();
		for (Tile tile : tiles) {
			if (!tile.containsCell()) {
				Cell cell = new Cell(tile);
				cells.add(cell);
				
			}
			
		}
		
		location.c.cellsToBe = cells;
	}
	
	public ArrayList<Tile> getMoveSet(){
		ArrayList<Tile> result = new ArrayList<Tile>();
		
		for (int i = 0; i < properties.speed + 1; i++) {
			int x = location.x + i * location.c.tileSize;
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = location.y + j * location.c.tileSize;
				Tile tile = location.c.getTile(x, y);
				result.add(tile);
			}
		}
		
		for (int i = 0; i < properties.speed + 1; i++) {
			int x = location.x + i * location.c.tileSize;
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = location.y - j * location.c.tileSize;
				Tile tile = location.c.getTile(x, y);
				result.add(tile);
			}
		}
		
		for (int i = 0; i < properties.speed + 1; i++) {
			System.out.println(i);
			int x = location.x - i * location.c.tileSize;
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = location.y + j * location.c.tileSize;
				Tile tile = location.c.getTile(x, y);
				result.add(tile);
			}
		}
		
		for (int i = 0; i < properties.speed + 1; i++) {
			int x = location.x - i * location.c.tileSize;
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = location.y - j * location.c.tileSize;
				Tile tile = location.c.getTile(x, y);
				result.add(tile);
			}
		}
		
		
		return result;
	}
}
