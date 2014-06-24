package framework;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import objects.Cell;

public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener{

	World world;
	
	public MouseInput(World w) {
		world = w;
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
		int x = e.getX() - 3;
		int y = e.getY() - 20;
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			Tile t = world.getTile(x, y);
			world.select(t);
		}
		
		if (e.getButton() == MouseEvent.BUTTON2) {
			world.iterate();
			
		}
		
		if (e.getButton() == MouseEvent.BUTTON3) {
			Tile t = world.getTile(x, y);
			if (t != null && world.getCellAtPositionCurrent(t.x, t.y) == null) {				
				int cellType = 1;

				//DEBUG change cell type
				cellType = (int)(Math.random() * 2)+1;

				Cell cell = new Cell(world, t.x, t.y, cellType);
				world.addCell(cell);
				System.out.println("new cell: " + t.x + ":" + t.y);
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

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL && e.getWheelRotation() == -1) {
			world.setIterations(world.getIterations() +  e.getScrollAmount() / 3);
		}
		
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL && e.getWheelRotation() == 1) {
			world.setIterations(world.getIterations() - e.getScrollAmount() / 3);
		}
	}

}
