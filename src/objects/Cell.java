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
		System.out.println("action");
		ArrayList<Tile> tiles = getMoveSet();
		System.out.println(tiles);
		for (Tile tile : tiles) {
			if (!tile.containsCell()) {
				Cell cell = new Cell(tile);
				tile.c.addCell(cell);
				System.out.println("add cell");
			}
			System.out.println("end Actoin");
		}
	}
	
	public ArrayList<Tile> getMoveSet(){
		ArrayList<Tile> result = new ArrayList<Tile>();
		
		for (int i = 0 - properties.speed; i < properties.speed; i++) {
			int x = location.x + i * location.c.tileSize;
			int Dy = properties.speed - i;
			if(i < 0){
				Dy = - Dy;
			}
			
			for(int j = 0-Dy; j < Dy; j++){
				int y = location.y - j * location.c.tileSize;
				Tile tile = location.c.getTile(x, y);
				result.add(tile);
			}
			
			
		}
		
		
		
		return result;
	}
}
