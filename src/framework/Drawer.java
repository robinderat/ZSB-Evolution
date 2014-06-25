package framework;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	JButton populateButton = new JButton("populate");
	JButton clearButton = new JButton("clear");
	
	JLabel mutationLabel = new JLabel("Mutation Rate", JLabel.CENTER);
	JLabel currentMutationRate = new JLabel("", JLabel.CENTER);
	JSlider mutationRateSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 20);
	
	JLabel currentFillRate = new JLabel("", JLabel.CENTER);
	JSlider fillRateSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);
	
	JLabel crossoverLabel = new JLabel("Crossover Rate", JLabel.CENTER);
	JLabel currentCrossoverRate = new JLabel("", JLabel.CENTER);
	JSlider crossOverRateSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);
	
	JLabel matingEnergyRequirementLabel = new JLabel("Mating Energy Requirement", JLabel.CENTER);
	JLabel currentMatingEnergyRequirement = new JLabel("", JLabel.CENTER);
	JSlider matingEnergyRequirementSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);

	
	public Drawer(World c) {
		
		super.setLayout(null);
		Settings settings = Settings.getInstance();
		
		
		populateButton.setBounds(620, 60, 140, 30);
		populateButton.setFocusable(false);
		populateButton.setActionCommand("populate");
		populateButton.addActionListener(bl);
		
		clearButton.setBounds(620, 100, 140, 30);
		clearButton.setFocusable(false);
		clearButton.setActionCommand("clear");
		clearButton.addActionListener(bl);
		

		crossoverLabel.setBounds(620, 120, 160, 90);
		
		currentCrossoverRate.setBounds(620, 140, 160, 90);
		currentCrossoverRate.setText(String.valueOf((int)(settings.crossoverRate*100)));
		
		crossOverRateSlider.setValue((int)(settings.crossoverRate*100));
		crossOverRateSlider.setBounds(620, 200, 120, 30);
		crossOverRateSlider.addChangeListener(bl);
		crossOverRateSlider.setFocusable(false);
		
		
		mutationLabel.setBounds(830, 120, 160, 90);
		
		currentMutationRate.setBounds(825, 140, 160, 90);
		currentMutationRate.setText(String.valueOf((int)(settings.mutationRate*1000)));
		
		mutationRateSlider.setValue((int)(settings.mutationRate*1000));
		mutationRateSlider.setBounds(830, 200, 160, 30);
		mutationRateSlider.addChangeListener(bl);
		mutationRateSlider.setFocusable(false);
		
		
		currentFillRate.setBounds(825, 0, 160, 90);
		currentFillRate.setText(String.valueOf((int)(settings.fillRate*100)));

		fillRateSlider.setValue((int)(settings.fillRate*100));
		fillRateSlider.setBounds(800, 60, 160, 30);
		fillRateSlider.addChangeListener(bl);
		fillRateSlider.setFocusable(false);
		
		//Hashtable labelTable = new Hashtable();
		//labelTable.put( new Integer( 0 ), new JLabel("0") );
		//labelTable.put( new Integer( 100 ), new JLabel("100") );
		//fillRateSlider.setLabelTable( labelTable );
		//fillRateSlider.setPaintLabels(true);
		
		
		crossoverLabel.setBounds(620, 120, 160, 90);
		
		currentCrossoverRate.setBounds(620, 140, 160, 90);
		currentCrossoverRate.setText(String.valueOf((int)(settings.crossoverRate*100)));
		
		crossOverRateSlider.setValue((int)(settings.crossoverRate*100));
		crossOverRateSlider.setBounds(620, 200, 160, 30);
		crossOverRateSlider.addChangeListener(bl);
		crossOverRateSlider.setFocusable(false);
		
		
		matingEnergyRequirementLabel.setBounds(620, 220, 160, 90);
		
		currentMatingEnergyRequirement.setBounds(620, 240, 160, 90);
		currentMatingEnergyRequirement.setText(String.valueOf((int)(settings.matingEnergyCost*100)));
		
		matingEnergyRequirementSlider.setValue((int)(settings.matingEnergyCost*100));
		matingEnergyRequirementSlider.setBounds(620, 300, 160, 30);
		matingEnergyRequirementSlider.addChangeListener(bl);
		matingEnergyRequirementSlider.setFocusable(false);
	
		
		super.add(populateButton);
		super.add(clearButton);
		super.add(fillRateSlider);
		super.add(currentFillRate);
		super.add(currentMutationRate);
		super.add(mutationRateSlider);
		super.add(mutationLabel);
		super.add(crossOverRateSlider);
		super.add(currentCrossoverRate);
		super.add(crossoverLabel);
		super.add(matingEnergyRequirementLabel);
		super.add(currentMatingEnergyRequirement);
		super.add(matingEnergyRequirementSlider);
		
		
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
			int drawX = cell.x + world.xOffSet - world.TILE_SIZE / 2;
			int drawY = cell.y + world.yOffSet - world.TILE_SIZE / 2; 
			
			g.drawImage(cell.img, drawX, drawY, world.TILE_SIZE, world.TILE_SIZE, null);
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
	        	if (source == fillRateSlider)
	        	{
		        	settings.fillRate = value/100.0f;
		            currentFillRate.setText(String.valueOf(value));
	        	}
	        	else if (source == mutationRateSlider)
	        	{
	        		settings.mutationRate = value/1000.0f;
	        		currentMutationRate.setText(String.valueOf(value));
	        		world.cBreeder.setMutationRate(value/1000.0f);
	        	}
	        	else if (source == crossOverRateSlider)
	        	{
	        		settings.mutationRate = value/100.0f;
	        		currentCrossoverRate.setText(String.valueOf(value));
	        		world.cBreeder.setCrossoverRate(value/100.0f);
	        	}
	        	else if (source == matingEnergyRequirementSlider)
	        	{
	        		settings.matingEnergyCost = value/100.0f;
	        		currentMatingEnergyRequirement.setText(String.valueOf(value));
	        	}
	        	else if (source == matingEnergyRequirementSlider)
	        	{
	        		settings.matingEnergyCost = value/100.0f;
	        		currentMatingEnergyRequirement.setText(String.valueOf(value));
	        	}
	        }    
	    }
	}
}
