package framework;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.swing.ImageIcon;


public class Tile {

	//Tile's variables
	public int x;
	public int y;
	Image img;
	Point2D.Double[] sides = new Point2D.Double[4];
	public WeakReference<World> worldRef;
	
	//Constructor
	public Tile(int x, int y, World world){
		this.x = x;
		this.y = y;
		worldRef = new WeakReference<World>(world);
		img = new ImageIcon("src/art/Tile.png").getImage();
		createSides();
	}
	
	private void createSides(){
		sides[0] =  new Point2D.Double(x - worldRef.get().TILE_SIZE / 2, y);
		sides[1] =  new Point2D.Double(x + worldRef.get().TILE_SIZE / 2, y);
		sides[2] =  new Point2D.Double(x, y - worldRef.get().TILE_SIZE / 2);
		sides[3] =  new Point2D.Double(x, y + worldRef.get().TILE_SIZE / 2);
	}
	
	
	//Returns list of tiles next to this tile 
	public ArrayList<Tile> getNeighbors(){
		ArrayList<Tile> arr = new ArrayList<Tile>();
		
		Tile[][] r = worldRef.get().getTiles();
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
	
	
	//Returns image of tile
	public Image getImage(){
		return img;
	}
}
