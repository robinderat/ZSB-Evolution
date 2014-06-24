package objects;

import java.util.Random;

public class Properties {

	// DNA settings
	private static final int maxEnergyBitLength = 6;
	private static final int speedBitLength = 5;
	private static final int visionRangeBitLength = 3;
	private static final int strengthBitLength = 6;
	
	
	// survival statistics
	public int currentEnergy;
	public int currentLifeSpan;
	
	
	//public int fitness;

	//public int type;
	
	// inherited by DNA
	private int maxEnergy;
	private int speed;
	private int visionRange;
	private int strength;		
	 
	//public int maxLifeSpan;
	//public int minMatingAge;
	//public int maxMatingAge;

	public String DNA;
	
	
	public Properties() {
		generateDNA();
		decodeDNAProperties();
		
		currentEnergy = maxEnergy;
	}
	
	public Properties(int[] stats) {
	}
	
	public Properties(String newDNA) {
		DNA = newDNA;
		decodeDNAProperties();
		
		currentEnergy = maxEnergy;
	}
	
	/* converts the DNA string into properties 
	 * the order of the properties in the DNA is
	 * maxEnergy -> speed -> visionRange -> strength */
	private void decodeDNAProperties() {
		
		int start = 0;
		int end = maxEnergyBitLength;
		setMaxEnergy(DNA.substring(0, end));
		
		
		start = end;
		end = end + speedBitLength;
		setSpeed(DNA.substring(start, end));
		
		
		start = end;
		end = end + visionRangeBitLength;
		setVisionRange(DNA.substring(start, end));
		
		
		start = end;
		end = end + strengthBitLength;
		setStrength(DNA.substring(start, end));
	}
	
	/* converts the maxEnergy DNA substring to 
	 * a maxEnergy property. The value is binary 
	 * encoded in the DNA */
	private void setMaxEnergy(String energyDNA) {
		maxEnergy = 0;
		
		for (int i = 0; i < energyDNA.length(); i++) {
			maxEnergy += ((int)energyDNA.charAt(i)-48) * Math.pow(2, i);
		}
	}
	
	/* converts the speed DNA substring to
	 * a speed property. The value is encoded
	 * in the DNA boolean like (5 one's means 
	 * a value of 5) */
	private void setSpeed(String speedDNA) {
		speed = 0;
		
		for (int i = 0; i < speedDNA.length(); i++) {
			speed += ((int)speedDNA.charAt(i)-48) * 1; 
		}
	}
	
	/* converts the sight DNA substring to
	 * a vision property. The value is binary 
	 * encoded in the DNA */
	private void setVisionRange(String visionRangeDNA) {
		visionRange = 0;
		
		for (int i = 0; i < visionRangeDNA.length(); i++) {
			visionRange += ((int)visionRangeDNA.charAt(i)-48) * Math.pow(2, i);
		}
	}
	
	/* converts the strength DNA substring to 
	 * a strength property. The value is binary 
	 * encoded in the DNA */
	private void setStrength(String strengthDNA) {
		strength = 0;
		
		for (int i = 0; i < strengthDNA.length(); i++) {
			strength += ((int)strengthDNA.charAt(i)-48) * Math.pow(2, i);
		}
	}
	
	/* generates a DNA string randomly with
	 * the correct length based on the bit length
	 * of the properties */
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
	
	/* prints all the DNA properties of 
	 * the cell */
	private void printDNAProperties(){
		System.out.println("DNA: " +DNA);
		System.out.println("Max Energy: " +maxEnergy);
		System.out.println("Speed: " +speed);
		System.out.println("Vision: " +visionRange);
		System.out.println("Strength: " +strength);
	}
	
	/* retrieves the strength property */
	public int getStrength() { 
		return strength; 
	}
	
	/* retrieves the speed property */
	public int getSpeed() {
		return speed;
	}
	
	/* retrieves the vision property */
	public int getVision() {
		return visionRange;
	}
	
	/* retrieves the maxEnergy property */
	public int getMaxEnergy() {
		return maxEnergy;
	}
}