package framework;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import objects.Cell;


public class Tile {

	public int x;
	public int y;
	Image img;
	Point2D.Double[] sides = new Point2D.Double[4];
	public Core c;
	Cell cell = null;
	
	public Tile(int x, int y, Core core){
		this.x = x;
		this.y = y;
		c = core;
		img = new ImageIcon("src/art/Tile.png").getImage();
		createSides();
	}
	
	private void createSides(){
		sides[0] =  new Point2D.Double(x - c.tileSize / 2, y);
		sides[1] =  new Point2D.Double(x + c.tileSize / 2, y);
		sides[2] =  new Point2D.Double(x, y - c.tileSize / 2);
		sides[3] =  new Point2D.Double(x, y + c.tileSize / 2);
		/*System.out.println(sides[0].x + "||" + sides[0].y);
		System.out.println(sides[1].x + "||" + sides[1].y);
		System.out.println(sides[2].x + "||" + sides[2].y);
		System.out.println(sides[3].x + "||" + sides[3].y);
		System.out.println("_________");*/
	}
	
	public ArrayList<Tile> getNeighbours(){
		ArrayList<Tile> arr = new ArrayList<Tile>();
		
		Tile[][] r = c.getTiles();
		for (Tile[] tr : r){
			for (Tile t : tr) {
				for (Point2D.Double p : t.sides) {
					for (Point2D.Double pOwn : sides) {
						if (p.equals(pOwn) && t != this){
							arr.add(t);
						}
					}
					
				}
			}
		}
		
		return arr;
	}
	
	public Image getImage(){
		return img;
	}
	
	public boolean containsCell(){
		if (cell != null) {
			return true;
		}else return false;
	}
	
	public void giveCell(Cell c){
		cell = c;
	}
	
	public Cell getCell(){
		return cell;
	}
}
