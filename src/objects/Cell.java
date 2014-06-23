package objects;

import java.awt.Image;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import objects.behaviour.Behaviour;
import objects.behaviour.MoveRightBehaviour;
import framework.Tile;

public class Cell {

	public WeakReference<Tile> locationRef;
	public Properties properties;
	public Image img;
	public ArrayList<Behaviour> behaviours;
	public boolean performingMultiTurnBehaviour; // in case we do multi turn complex behaviours
	
	public Cell(Tile t) {
		locationRef = new WeakReference<Tile>(t);
		properties = new Properties();
		img = new ImageIcon("src/art/Cell1.png").getImage();
		t.giveCell(this);
		
		behaviours = new ArrayList<Behaviour>();
		behaviours.add(new MoveRightBehaviour());
	}

	public void moveTo(Tile t) {
		locationRef = new WeakReference<Tile>(t);
	}
	
	// moves the cell: check what it should do and then go toward that position
	public void update() {

		// possibly check if following multi-turn behaviour
		// if (performingMultiTurnBehaviour) then continue that
		
		
		
		// else:
		
		Behaviour behaviour;
		
		// pick behaviour from behaviours. picks first possible one from list
		for (int i = 0; i < behaviours.size(); i++){
			// if (behaviours.get(i).isPossible()) {
				// behaviour = behaviours.get(i);
				// break;
			//}
		}
		
		// TEMPORARY TEST LINE : Always make first behaviour the chosen one :
		behaviour = behaviours.get(0);

		
		// get destination from behaviour
		// Tile dest = behaviour.getDest();
		
		
		// go there
		behaviour.execute(this);
	}
	

	public void eat(Cell cell){
		int energyValue = 1;
		if (properties.currentEnergy < properties.maxEnergy - energyValue){
			properties.currentEnergy += energyValue;
		}else{
			properties.currentEnergy = properties.maxEnergy;
		}
		locationRef.get().coreRef.get().cellsToBeRemoved.add(cell);
		locationRef.get().coreRef.get().removeCellsDelayed();
		
	}
	
	public void attack(Cell target){
		target.properties.currentEnergy -= properties.getStrength();
		if(target.properties.currentEnergy < 0){
			eat(target);
		}
	}
	
	public void action(Tile destination){
		/*ArrayList<Cell> cells = new ArrayList<Cell>();

		ArrayList<Tile> tiles = getMoveSet();
		System.out.println(tiles);
		for (Tile tile : tiles) {
			if (!tile.containsCell()) {
				Cell cell = new Cell(tile);

				cells.add(cell);
				
			}
			

				tile.c.addCell(cell);
				System.out.println("add cell");
			}
			System.out.println("end Actoin");

		}
		
		location.c.cellsToBeAdded = cells;
		*/
		moveTo(destination);
	}
	
	public ArrayList<Tile> getMoveSet(){
		ArrayList<Tile> result = new ArrayList<Tile>();
		
		for (int i = 0; i < properties.speed + 1; i++) {
			int x = locationRef.get().x + locationRef.get().coreRef.get().xOffSet + i * locationRef.get().coreRef.get().tileSize;	
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = locationRef.get().y + locationRef.get().coreRef.get().yOffSet + j * locationRef.get().coreRef.get().tileSize;
				Tile tile = locationRef.get().coreRef.get().getTile(x, y);
				result.add(tile);
			}
		}
		
		for (int i = 0; i < properties.speed + 1; i++) {
			int x = locationRef.get().x + locationRef.get().coreRef.get().xOffSet  + i * locationRef.get().coreRef.get().tileSize;
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = locationRef.get().y + locationRef.get().coreRef.get().yOffSet  - j * locationRef.get().coreRef.get().tileSize;
				Tile tile = locationRef.get().coreRef.get().getTile(x, y);
				result.add(tile);
			}
		}
		
		for (int i = 0; i < properties.speed + 1; i++) {
			System.out.println(i);
			int x = locationRef.get().x + locationRef.get().coreRef.get().xOffSet  - i * locationRef.get().coreRef.get().tileSize;
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = locationRef.get().y + locationRef.get().coreRef.get().yOffSet  + j * locationRef.get().coreRef.get().tileSize;
				Tile tile = locationRef.get().coreRef.get().getTile(x, y);
				result.add(tile);
			}
		}
		
		for (int i = 0; i < properties.speed + 1; i++) {
			int x = locationRef.get().x  + locationRef.get().coreRef.get().xOffSet  - i * locationRef.get().coreRef.get().tileSize;
			int Dy = properties.speed + 1 - i;
			for (int j = 0; j < Dy; j++) {
				int y = locationRef.get().y + locationRef.get().coreRef.get().yOffSet  - j * locationRef.get().coreRef.get().tileSize;
				Tile tile = locationRef.get().coreRef.get().getTile(x, y);
				result.add(tile);
			}
		}
		
		
		return result;
	}
}
