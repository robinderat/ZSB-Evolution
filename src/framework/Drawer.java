package framework;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import objects.Cell;




public class Drawer extends JPanel {

	private static final long serialVersionUID = 1L;
	World world;
	
	Image bg = new ImageIcon("src/art/Bg.png").getImage();
	Image select = new ImageIcon("src/art/Selected.png").getImage();
	Image infoBg = new ImageIcon("src/art/Data.png").getImage();
	
	JButton populateButton = new JButton("Populate (n)");
	JButton clearButton = new JButton("Clear (c)");
	
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
	
	JLabel eatingEnergyGainLabel = new JLabel("Eating Energy Gain", JLabel.CENTER);
	JLabel currentEatingEnergyGain = new JLabel("", JLabel.CENTER);
	JSlider eatingEnergyGainSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, 20);
	
	JLabel veryHungryThresholdLabel = new JLabel("Very Hungry Threshold", JLabel.CENTER);
	JLabel currentVeryHungryThreshold = new JLabel("", JLabel.CENTER);
	JSlider veryHungryThresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);
	
	JLabel startEnergyRateLabel = new JLabel("Starting Energy Rate", JLabel.CENTER);
	JLabel currentStartEnergyRate = new JLabel("", JLabel.CENTER);
	JSlider startEnergyRateSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);
	
	JLabel moveStrengthModifierLabel = new JLabel("Move Strength Modifier", JLabel.CENTER);
	JLabel currentMoveStrengthModifier = new JLabel("", JLabel.CENTER);
	JSlider moveStrengthModifierSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, 20);
	
	JCheckBox allowCannibalismButton = new JCheckBox("Canibalism");

	JButton settingsButtonJ = new JButton("j settings");
	JButton settingsButtonR = new JButton("r settings");
	JButton settingsButtonM = new JButton("m settings");
	JButton settingsButtonK = new JButton("k settings");
	
	JTextField iterateRest = new JTextField("Min iterates");
	String iterateRestValue = new String("");

	
	public Drawer(World c) {
		
		super.setLayout(null);
		Settings settings = Settings.getInstance();
		
		currentFillRate.setBounds(800, 0, 160, 90);
		currentFillRate.setText(String.valueOf((int)(settings.fillRate*100)));

		fillRateSlider.setValue((int)(settings.fillRate*100));
		fillRateSlider.setBounds(800, 60, 160, 30);
		fillRateSlider.addChangeListener(bl);
		fillRateSlider.setFocusable(false);
		
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
		crossOverRateSlider.setBounds(620, 200, 160, 30);
		crossOverRateSlider.addChangeListener(bl);
		crossOverRateSlider.setFocusable(false);
		
		
		mutationLabel.setBounds(825, 120, 160, 90);
		
		currentMutationRate.setBounds(825, 140, 160, 90);
		currentMutationRate.setText(String.valueOf((int)(settings.mutationRate*1000)));
		
		mutationRateSlider.setValue((int)(settings.mutationRate*1000));
		mutationRateSlider.setBounds(825, 200, 160, 30);
		mutationRateSlider.addChangeListener(bl);
		mutationRateSlider.setFocusable(false);
		
		//Hashtable labelTable = new Hashtable();
		//labelTable.put( new Integer( 0 ), new JLabel("0") );
		//labelTable.put( new Integer( 100 ), new JLabel("100") );
		//fillRateSlider.setLabelTable( labelTable );
		//fillRateSlider.setPaintLabels(true);
		
		
		matingEnergyRequirementLabel.setBounds(620, 220, 160, 90);
		
		currentMatingEnergyRequirement.setBounds(620, 240, 160, 90);
		currentMatingEnergyRequirement.setText(String.valueOf((int)(settings.matingEnergyCost*100)));
		
		matingEnergyRequirementSlider.setValue((int)(settings.matingEnergyCost*100));
		matingEnergyRequirementSlider.setBounds(620, 300, 160, 30);
		matingEnergyRequirementSlider.addChangeListener(bl);
		matingEnergyRequirementSlider.setFocusable(false);
		
		
		eatingEnergyGainLabel.setBounds(825, 220, 160, 90);
		
		currentEatingEnergyGain.setBounds(825, 240, 160, 90);
		currentEatingEnergyGain.setText(String.valueOf((int)(settings.eatingEnergyGain*100)));
		
		eatingEnergyGainSlider.setValue((int)(settings.eatingEnergyGain*100));
		eatingEnergyGainSlider.setBounds(825, 300, 160, 30);
		eatingEnergyGainSlider.addChangeListener(bl);
		eatingEnergyGainSlider.setFocusable(false);
		
		
		veryHungryThresholdLabel.setBounds(620, 320, 160, 90);
		
		currentVeryHungryThreshold.setBounds(620, 340, 160, 90);
		currentVeryHungryThreshold.setText(String.valueOf((int)(settings.veryHungryThreshold*100)));
		
		veryHungryThresholdSlider.setValue((int)(settings.veryHungryThreshold*100));
		veryHungryThresholdSlider.setBounds(620, 400, 160, 30);
		veryHungryThresholdSlider.addChangeListener(bl);
		veryHungryThresholdSlider.setFocusable(false);
		
		
		startEnergyRateLabel.setBounds(825, 320, 160, 90);
		
		currentStartEnergyRate.setBounds(825, 340, 160, 90);
		currentStartEnergyRate.setText(String.valueOf((int)(settings.startEnergyRate*100)));
		
		startEnergyRateSlider.setValue((int)(settings.startEnergyRate*100));
		startEnergyRateSlider.setBounds(825, 400, 160, 30);
		startEnergyRateSlider.addChangeListener(bl);
		startEnergyRateSlider.setFocusable(false);
		
		
		moveStrengthModifierLabel.setBounds(620, 420, 160, 90);
		
		currentMoveStrengthModifier.setBounds(620, 440, 160, 90);
		currentMoveStrengthModifier.setText(String.valueOf((int)(settings.moveStrengthModifier*100)));
		
		moveStrengthModifierSlider.setValue((int)(settings.moveStrengthModifier*100));
		moveStrengthModifierSlider.setBounds(620, 500, 160, 30);
		moveStrengthModifierSlider.addChangeListener(bl);
		moveStrengthModifierSlider.setFocusable(false);
		
		allowCannibalismButton.setBounds(825, 500, 100, 35);
	    allowCannibalismButton.setSelected(true);
	    allowCannibalismButton.setFocusable(false);
	    allowCannibalismButton.addItemListener(bl);
	    allowCannibalismButton.setSelected(settings.allowCannibalism);

		settingsButtonJ.setBounds(800, 560, 94, 20);
		settingsButtonJ.setFocusable(false);
		settingsButtonJ.setActionCommand("jSettings");
		settingsButtonJ.addActionListener(bl);
		
		settingsButtonR.setBounds(800, 580, 94, 20);
		settingsButtonR.setFocusable(false);
		settingsButtonR.setActionCommand("rSettings");
		settingsButtonR.addActionListener(bl);
		
		settingsButtonM.setBounds(894, 560, 94, 20);
		settingsButtonM.setFocusable(false);
		settingsButtonM.setActionCommand("mSettings");
		settingsButtonM.addActionListener(bl);
		
		settingsButtonK.setBounds(894, 580, 94, 20);
		settingsButtonK.setFocusable(false);
		settingsButtonK.setActionCommand("kSettings");
		settingsButtonK.addActionListener(bl);
		
		/*
		iterateRest.setBounds(849, 605, 80,20);
		iterateRest.setFocusable(false);
		iterateRest.setActionCommand("iterator");
	    iterateRest.addActionListener(bl);
	    iterateRest.setEditable(true);
	    */
		super.add(clearButton);
		
		super.add(fillRateSlider);
		super.add(currentFillRate);
		super.add(populateButton);
		
		super.add(currentMutationRate);
		super.add(mutationRateSlider);
		super.add(mutationLabel);
		
		super.add(crossOverRateSlider);
		super.add(currentCrossoverRate);
		super.add(crossoverLabel);
		
		super.add(matingEnergyRequirementLabel);
		super.add(currentMatingEnergyRequirement);
		super.add(matingEnergyRequirementSlider);
		
		super.add(eatingEnergyGainLabel);
		super.add(currentEatingEnergyGain);
		super.add(eatingEnergyGainSlider);
		
		super.add(veryHungryThresholdLabel);
		super.add(currentVeryHungryThreshold);
		super.add(veryHungryThresholdSlider);
		
		super.add(startEnergyRateLabel);
		super.add(currentStartEnergyRate);
		super.add(startEnergyRateSlider);
		
		super.add(moveStrengthModifierLabel);
		super.add(currentMoveStrengthModifier);
		super.add(moveStrengthModifierSlider);
		
		super.add(allowCannibalismButton);
		
		super.add(settingsButtonJ);
		super.add(settingsButtonM);
		super.add(settingsButtonR);
		super.add(settingsButtonK);
		
		//super.add(iterateRest);
		
		world = c;
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.drawImage(bg, 0, 0, 2500, 1000, null);
		paintTiles(g);
		paintSelected(g);
		paintProperties(g);
		paintStats(g);
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
				g.drawString("Last Behaviour: " + selectedCell.properties.lastBehaviour, 1020, 450);
			}
		}
	}
	
	// for stats
	private void paintStats(Graphics g){
		//g.drawImage(infoBg, 1000, 25, 300, 600, null);
		Tile tile = world.selected;

		if (tile != null) {
			g.drawString("X: " + Integer.toString(tile.x), 1020, 150);
			g.drawString("Y: " + Integer.toString(tile.y), 1020, 175);
			
			Cell selectedCell = world.getCellAtPositionCurrent(tile.x, tile.y);
			
			if (selectedCell != null)
			{
				g.drawString(selectedCell.toString(), 1020, 550);
				g.drawString("Stats and Stuff", 1020, 525);
				g.setColor(Color.green);
				g.drawString("-------------------", 1020, 535);
				g.setColor(Color.darkGray);
				//g.drawString("13th strange prime = 101", 1020, 550); 
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
	
	class ButtonListener implements ActionListener, ChangeListener, ItemListener {
				
	    public void actionPerformed(ActionEvent e) {
	    	
	    	if ("populate".equals(e.getActionCommand())) 
	    	{
	    		world.populate();
	    	}
	    	if ("clear".equals(e.getActionCommand()))
	    	{
	    		world.clear();
	    	}
	    	
	    	if ("jSettings".equals(e.getActionCommand())) updateSliderValues('j');
	    	if ("rSettings".equals(e.getActionCommand())) updateSliderValues('r');
	    	if ("mSettings".equals(e.getActionCommand())) updateSliderValues('m');
	    	if ("kSettings".equals(e.getActionCommand())) updateSliderValues('k');
	    	
	    	//if ("iterator".equals(e.getActionCommand())) iterateRestValue = "text1 : " + e.getActionCommand();
	    	//if (e.getSource() == iterateRest) {
			//	iterateRestValue = "text1 : " + e.getActionCommand();
			//} 
	    }
	    
	    public void stateChanged(ChangeEvent e) {
	    	Settings settings = Settings.getInstance();
	    	
	        JSlider source = (JSlider)e.getSource();
        	int value = (int)source.getValue();
        	
        	if (source == fillRateSlider)
        	{
        		if (!source.getValueIsAdjusting()) settings.fillRate = value/100.0f;
	        	
	            currentFillRate.setText(String.valueOf(value));
        	}
        	else if (source == mutationRateSlider)
        	{
        		if (!source.getValueIsAdjusting()) settings.mutationRate = value/1000.0f;
        		currentMutationRate.setText(String.valueOf(value));
        		
        	}
        	else if (source == crossOverRateSlider)
        	{
        		if (!source.getValueIsAdjusting()) settings.crossoverRate = value/100.0f;
        		
        		currentCrossoverRate.setText(String.valueOf(value));
        		
        	}
        	else if (source == matingEnergyRequirementSlider)
        	{
        		if (!source.getValueIsAdjusting()) settings.matingEnergyCost = value/100.0f;
        		currentMatingEnergyRequirement.setText(String.valueOf(value));
        	}
        	else if (source == eatingEnergyGainSlider)
        	{
        		if (!source.getValueIsAdjusting()) settings.eatingEnergyGain = value/100.0f;
        		currentEatingEnergyGain.setText(String.valueOf(value));
        	}    
        	else if (source == veryHungryThresholdSlider)
        	{
        		if (!source.getValueIsAdjusting()) settings.veryHungryThreshold = value/100.0f;
        		currentVeryHungryThreshold.setText(String.valueOf(value));
        	}
        	else if (source == startEnergyRateSlider)
        	{
        		if (!source.getValueIsAdjusting()) settings.startEnergyRate = value/100.0f;
        		currentStartEnergyRate.setText(String.valueOf(value));
        	}
        	else if (source == moveStrengthModifierSlider)
        	{
        		if (!source.getValueIsAdjusting()) settings.moveStrengthModifier = value/100.0f;
        		currentMoveStrengthModifier.setText(String.valueOf(value));
        	}
	    }
	    
	    public void itemStateChanged(ItemEvent e) {
	    	Settings settings = Settings.getInstance();
	    	Object source = e.getItemSelectable();

	        if (source == allowCannibalismButton) {
	        	if (e.getStateChange() == ItemEvent.DESELECTED) {
	        		settings.allowCannibalism = false;
	        	}
	        	else {
	        		settings.allowCannibalism = true;
	        	}
	        }
	        

	        
	    }
	}
	
	// updates a slider to fit with someone changing to their own initial settings
	private void updateSliderValues(char c){
		Settings set = Settings.getInstance();
		set.newSettings(c);
		
		currentFillRate.setText(String.valueOf((int)(set.fillRate*100)));	
		currentMutationRate.setText(String.valueOf((int)(set.mutationRate*1000)));
		currentCrossoverRate.setText(String.valueOf((int)(set.crossoverRate*100)));

		currentMatingEnergyRequirement.setText(String.valueOf((int)(set.matingEnergyCost*100)));
		currentEatingEnergyGain.setText(String.valueOf((int)(set.eatingEnergyGain*100)));
		currentVeryHungryThreshold.setText(String.valueOf((int)(set.veryHungryThreshold*100)));

		currentStartEnergyRate.setText(String.valueOf((int)(set.startEnergyRate*100)));
		currentMoveStrengthModifier.setText(String.valueOf((int)(set.moveStrengthModifier*100)));
		allowCannibalismButton.setSelected(set.allowCannibalism);
	}
}
