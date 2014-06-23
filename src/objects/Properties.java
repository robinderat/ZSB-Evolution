package objects;

import java.util.Random;

public class Properties {

	// DNA settings
	private static final int maxEnergyBitLength = 6;
	private static final int speedBitLength = 5;
	private static final int visionRangeBitLength = 3;
	private static final int strengthBitLength = 6;
	
	
	// variable
	public int currentEnergy = 1;
	public int currentLifeSpan;
	
	
	//public int fitness;

	public int type;
	
	// inherited by DNA
	public int maxEnergy;
	public int speed;
	public int visionRange;
	public int strength;		
	 
	//public int maxLifeSpan;
	//public int minMatingAge;
	//public int maxMatingAge;

	public String DNA;
	
	
	public Properties() {
		generateDNA();
		decode(DNA);
	}
	
	public Properties(int[] stats) {
	}
	
	public Properties(String newDNA) {
		decode(DNA);
		DNA = newDNA;
	}
	
	private void decode(String DNA) {
		
		int start = 0;
		int end = maxEnergyBitLength;
		
		
		setMaxEnergy(DNA.substring(0, end));
		
		
		start = end;
		end = end + speedBitLength;
		
		setSpeed(DNA.substring(start, end));
		
		
		start = end;
		end = end + visionRangeBitLength;
		
		setSight(DNA.substring(start, end));
		
		
		start = end;
		end = end + strengthBitLength;
		
		setStrength(DNA.substring(start, end));
	}
	
	private void setMaxEnergy(String energyDNA) {
		maxEnergy = 0;
		
		for (int i = 0; i < energyDNA.length(); i++) {
			maxEnergy += ((int)energyDNA.charAt(i)-48) * Math.pow(2, i);
		}
	}
	
	private void setSpeed(String speedDNA) {
		speed = 0;
		
		for (int i = 0; i < speedDNA.length(); i++) {
			speed += ((int)speedDNA.charAt(i)-48) * 1; 
		}
	}
	
	private void setSight(String sightDNA) {
		visionRange = 0;
		
		for (int i = 0; i < sightDNA.length(); i++) {
			visionRange += ((int)sightDNA.charAt(i)-48) * Math.pow(2, i);
		}
	}
	
	private void setStrength(String strengthDNA) {
		strength = 0;
		
		for (int i = 0; i < strengthDNA.length(); i++) {
			strength += ((int)strengthDNA.charAt(i)-48) * Math.pow(2, i);
		}
	}
	
	private void generateDNA() {
		int totalLength = maxEnergyBitLength + speedBitLength + visionRangeBitLength + strengthBitLength;
		Random random = new Random(10);
		
		DNA = "";
		
		for (int i = 0; i < totalLength; i++) {
			if (random.nextBoolean()) {
				DNA = DNA + '1';
			}
			else {
				DNA = DNA + '0';
			}
		}
	}
	
	private void printProperties(){
		System.out.println("DNA: " +DNA);
		System.out.println("Max Energy: " +maxEnergy);
		System.out.println("Speed: " +speed);
		System.out.println("Vision: " +visionRange);
		System.out.println("Strength: " +strength);
	}
}
