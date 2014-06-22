package framework;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import objects.Cell;


public class Clicker implements MouseListener, MouseMotionListener{

	Core c;
	
	public Clicker(Core core) {
		c = core;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX() - 10;
		int y = e.getY() - 30;
		
		if(e.getButton() == e.BUTTON1){
			Tile t = c.getTile(x, y);
			c.select(t);
		}
		
		if(e.getButton() == e.BUTTON3){
			Tile t = c.getTile(x, y);
			if(t != null){
				Cell cell = new Cell(t);
				c.addCell(cell);
			}
			
		}
		
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	
		
	}

}
