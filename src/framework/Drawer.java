package framework;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import objects.Cell;




public class Drawer extends JPanel {

	private static final long serialVersionUID = 1L;
	World world;
	Image bg = new ImageIcon("src/art/Bg.png").getImage();
	Image select = new ImageIcon("src/art/Selected.png").getImage();
	Image infoBg = new ImageIcon("src/art/Data.png").getImage();
	
	
	JLabel currentFillRate = new JLabel("", JLabel.CENTER);
	
	JButton populateButton = new JButton("populate");
	JButton clearButton = new JButton("clear");
	
	JSlider fillRateSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
	
	public Drawer(World c) {
		
		super.setLayout(null);
		Settings settings = Settings.getInstance();
		
		
		populateButton.setBounds(620, 60, 140, 30);
		populateButton.setFocusable(false);
		populateButton.setActionCommand("populate");
		populateButton.addActionListener(bl);
		
		
		currentFillRate.setBounds(800, 0, 120, 90);
		currentFillRate.setText(String.valueOf((int)(settings.fillRate*100)));
		
		
		fillRateSlider.setBounds(800, 60, 120, 30);
		fillRateSlider.addChangeListener(bl);
		fillRateSlider.setFocusable(false);
		
		
		
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer( 0 ), new JLabel("0") );
		labelTable.put( new Integer( 100 ), new JLabel("100") );
		fillRateSlider.setLabelTable( labelTable );

		fillRateSlider.setPaintLabels(true);
		

		
		clearButton.setBounds(620, 100, 140, 30);
		clearButton.setFocusable(false);
		clearButton.setActionCommand("clear");
		clearButton.addActionListener(bl);
		
		super.add(populateButton);
		super.add(clearButton);
		super.add(fillRateSlider);
		super.add(currentFillRate);
		
		
		
		world = c;
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.drawImage(bg, 0, 0, 2500, 1000, null);
		paintTiles(g);
		paintSelected(g);
		paintProperties(g);
		paintProperties2(g);
		paintCells(g);
	}
	
	private void paintTiles(Graphics g) {
		for (Tile[] tiles : world.getTiles()) {
			for (Tile t : tiles) {
				g.drawImage(t.getImage(), t.x + world.xOffSet - world.TILE_SIZE / 2 , t.y + world.yOffSet - world.TILE_SIZE /2, world.TILE_SIZE , world.TILE_SIZE , null);
			}
		}
	}
	
	private void paintProperties(Graphics g) {
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
				g.drawString("-----------", 1020, 400);
				g.drawString("Current Energy: " + selectedCell.properties.getCurrentEnergy(), 1020, 425); 
			}
		}
	}
	
	private void paintProperties2(Graphics g){
		//g.drawImage(infoBg, 1000, 25, 300, 600, null);
		Tile tile = world.selected;

		if (tile != null) {
			g.drawString("X: " + Integer.toString(tile.x), 1020, 150);
			g.drawString("Y: " + Integer.toString(tile.y), 1020, 175);
			
			Cell selectedCell = world.getCellAtPositionCurrent(tile.x, tile.y);
			
			if (selectedCell != null)
			{
				g.drawString("Stats and Stuff", 1020, 500);
				g.drawString("-----------", 1020, 525);
				g.drawString("Current Energy: " + selectedCell.properties.getCurrentEnergy(), 1020, 550); 
			}
		}
	}


	private void paintSelected(Graphics g) {
		Tile t = world.selected;
		if (t != null) {
			g.drawImage(select, t.x + world.xOffSet - world.TILE_SIZE / 2 , t.y + world.yOffSet - world.TILE_SIZE /2, world.TILE_SIZE , world.TILE_SIZE , null);
		}
	}
		
	private void paintCells(Graphics g) {
		for(Cell cell : world.getCells()){
			g.drawImage(cell.img, cell.x + world.xOffSet - world.TILE_SIZE / 2 , cell.y + world.yOffSet - world.TILE_SIZE /2, world.TILE_SIZE, world.TILE_SIZE, null);
		}
		
	}
	
	private ButtonListener bl = new ButtonListener();
	
	class ButtonListener implements ActionListener, ChangeListener {
				
	    public void actionPerformed(ActionEvent e) {
	    	
	    	if ("populate".equals(e.getActionCommand())) 
	    	{
	    		world.populate();
	    	}
	    	if ("clear".equals(e.getActionCommand()))
	    	{
	    		world.clear();
	    	}
	    }
	    
	    public void stateChanged(ChangeEvent e) {
	    	Settings settings = Settings.getInstance();
	    	
	        JSlider source = (JSlider)e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	int value = (int)source.getValue();
	        	settings.fillRate = value/100.0f;
	            currentFillRate.setText(String.valueOf(value));
	        }    
	    }
	}
}
