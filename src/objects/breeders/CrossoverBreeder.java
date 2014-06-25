package objects.breeders;

import framework.RandomGenerator;

public class CrossoverBreeder extends Breeder {
	
	private float crossOverRate;
	
	@Override
	public String[] merge(String DNA1, String DNA2) {
		String[] newDNAs = new String[2];
		RandomGenerator instance = RandomGenerator.getInstance();
		
		if (instance.getRandom().nextFloat() < crossOverRate) {
			crossOver(newDNAs, DNA1, DNA2);
		}
		else {
			newDNAs[0] = DNA1;
			newDNAs[1] = DNA2;
		}
		
		return newDNAs;
	}
	
	public CrossoverBreeder() {
		super();
		crossOverRate = 0.7f;
	}
	
	public CrossoverBreeder(float crossRate) {
		super();
		crossOverRate = crossRate;
	}
	
	public CrossoverBreeder(float mutRate, float crossRate) {
		super(mutRate);
		crossOverRate = crossRate;
	}
	
	private void crossOver(String[] newDNAs, String DNA1, String DNA2) {
		int DNALength = DNA1.length();
		RandomGenerator instance = RandomGenerator.getInstance();
		
		newDNAs[0] = "";
		newDNAs[1] = "";
		
		int crossBit = instance.getRandom().nextInt(DNALength);
		
		for (int i = 0; i < DNALength; i++) {
			if (i < crossBit) {
				newDNAs[0] = newDNAs[0] + DNA1.charAt(i);
				newDNAs[1] = newDNAs[1] + DNA2.charAt(i);
			}
			else {
				newDNAs[0] = newDNAs[0] + DNA2.charAt(i);
				newDNAs[1] = newDNAs[1] + DNA1.charAt(i);
			}
		}
	}

}
