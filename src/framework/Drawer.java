package framework;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import objects.Cell;




public class Drawer extends JPanel {

	private static final long serialVersionUID = 1L;
	World world;
	Image bg = new ImageIcon("src/art/Bg.png").getImage();
	Image select = new ImageIcon("src/art/Selected.png").getImage();
	Image infoBg = new ImageIcon("src/art/Data.png").getImage();
	
	public Drawer(World c) {
		world = c;
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
		for (Tile[] tiles : world.getTiles()) {
			for (Tile t : tiles) {
				g.drawImage(t.getImage(), t.x + world.xOffSet - world.tileSize / 2 , t.y + world.yOffSet - world.tileSize /2, world.tileSize , world.tileSize , null);
			}
		}
	}
	
	private void paintProperties(Graphics g){
		g.drawImage(infoBg, 1000, 25, 300, 600, null);
		Tile tile = world.selected;
		g.drawString("Steps in next iteration", 1020, 50);
		g.drawString(Integer.toString(world.getIterations()), 1220, 50);
		
		if (tile != null) {
			g.drawString("X: " + Integer.toString(tile.x), 1020, 150);
			g.drawString("Y: " + Integer.toString(tile.y), 1020, 175);
			
			Cell selectedCell = world.getCellAtPositionCurrent(tile.x, tile.y);
			
			if (selectedCell != null)
			{
				g.drawString("DNA Properties", 1020, 250);
				g.drawString("DNA: " + selectedCell.properties.getDNA(), 1020, 275);
				g.drawString("Max Energy: " + selectedCell.properties.getMaxEnergy(), 1020, 300);
				g.drawString("Speed: " + selectedCell.properties.getSpeed(), 1020, 325);
				g.drawString("Vision: " + selectedCell.properties.getVision(), 1020, 350);
				g.drawString("Strength: " + selectedCell.properties.getStrength(), 1020, 375);
			}
		}
	}

	private void paintSelected(Graphics g){
		Tile t = world.selected;
		if (t != null) {
			g.drawImage(select, t.x + world.xOffSet - world.tileSize / 2 , t.y + world.yOffSet - world.tileSize /2, world.tileSize , world.tileSize , null);
		}
	}
		
	private void paintCells(Graphics g){
		for(Cell cell : world.getCells()){
			g.drawImage(cell.img, cell.x + world.xOffSet - world.tileSize / 2 , cell.y + world.yOffSet - world.tileSize /2, world.tileSize, world.tileSize, null);
		}
		
	}
	
}
