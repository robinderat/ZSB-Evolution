package framework;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import objects.Cell;




public class Drawer extends JPanel {

	private static final long serialVersionUID = 1L;
	Core core;
	Image bg = new ImageIcon("src/art/Bg.png").getImage();
	Image select = new ImageIcon("src/art/Selected.png").getImage();
	Image infoBg = new ImageIcon("src/art/Data.png").getImage();
	
	public Drawer(Core c) {
		core = c;
	}

	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.drawImage(bg, 0, 0, 2500, 1000, null);
		paintTiles(g);
		paintSelected(g);
		paintProperties(g);
		paintCells(g);
		
	}
	
	private void paintTiles(Graphics g){
		for (Tile[] tiles : core.getTiles()) {
			for (Tile t : tiles) {
				g.drawImage(t.getImage(), t.x + core.xOffSet - core.tileSize / 2 , t.y + core.yOffSet - core.tileSize /2, core.tileSize , core.tileSize , null);
			}
		}
	}
	
	private void paintProperties(Graphics g){
		g.drawImage(infoBg, 1000, 25, 300, 600, null);
		Tile tile = core.selected;
		if (tile != null) {
			g.drawString("X: " + Integer.toString(tile.x), 1020, 50);
			g.drawString("Y: " + Integer.toString(tile.y), 1020, 75);
			
			if (tile.containsCell()) {
				Cell c = tile.getCell();
				g.drawString("CellType: " + Integer.toString(c.type), 1020, 125);
			}
			
		}
	}

	private void paintSelected(Graphics g){
		Tile t = core.selected;
		if (t != null) {
			g.drawImage(select, t.x + core.xOffSet - core.tileSize / 2 , t.y + core.yOffSet - core.tileSize /2, core.tileSize , core.tileSize , null);
		}
	}
		
	private void paintCells(Graphics g){
		for(Cell cell : core.getCells()){
			g.drawImage(cell.img, cell.location.x + core.xOffSet - core.tileSize / 2 , cell.location.y + core.yOffSet - core.tileSize /2, core.tileSize, core.tileSize, null);
		}
		
	}
	
}
