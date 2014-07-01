package framework;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import objects.Cell;

//Handles all mouse input
public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener{

	//World reference to access information from the world
	World world;
	
	//Constructor
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
		//Gets the x and y location of the mouse on clicking
		//Takes care of board offset and border size
		int x = e.getX() - world.xOffSet;
		int y = e.getY() - 23 - world.yOffSet;
		
		//Left mouse button
		if (e.getButton() == MouseEvent.BUTTON1) {
			Tile t = world.getTile(x, y);
			world.select(t);
		}
		
		//Middle mouse button
		if (e.getButton() == MouseEvent.BUTTON2) {
			if (world.doIterate == false) {
				world.doIterate = true;
				world.frame.draw.cycleButton.setText("Stop Cycle (space)");
			}
			else
			{
				world.doIterate = false;
				world.frame.notifyIterationsEnd();
				world.frame.draw.cycleButton.setText("Start Cycle (space)");
			} 
			
		}
		//Right mouse button
		if (e.getButton() == MouseEvent.BUTTON3) {
			Tile t = world.getTile(x, y);
			if (t != null) {
				Cell selectedCell = world.getCellAtPositionCurrent(t.x, t.y);
				if (selectedCell == null)
				{
				
					int cellType = 1;
	
					cellType = (int)(Math.random() * 2)+1;
	
					Cell cell = new Cell(world, t.x, t.y, cellType);
					world.addCell(cell);
					System.out.println("new cell: " + t.x + ":" + t.y);
				}
				else
				{
					world.removeCell(selectedCell);
				}
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

	//When scrolling
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		//Scrolling up
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL && e.getWheelRotation() == -1) {
			world.setIterations(world.getIterations() +  e.getScrollAmount() / 3);
		}
		
		//Scrolling down
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL && e.getWheelRotation() == 1) {
			world.setIterations(world.getIterations() - e.getScrollAmount() / 3);
		}
	}
}
