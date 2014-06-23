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
	/*
	public ArrayList<Tile> getTilesInRange(){
		ArrayList<Tile> result = new ArrayList<Tile>();
		for (Tile[] tiles : location.c.getTiles()) {
			for (Tile tile: tiles) {
				if (tile.x == 1) {
					
				}
			}
		}
		
		return result;
	}*/
}
