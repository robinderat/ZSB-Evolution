package objects;

import java.awt.Image;

import javax.swing.ImageIcon;

import framework.Tile;

public class Cell {

	public Tile location;
	Behaviour behaviour;
	Properties properties;
	public Image img;
	public int type = 0;
	
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
}
