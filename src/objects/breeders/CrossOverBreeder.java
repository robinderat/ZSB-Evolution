package objects.breeders;

import java.util.Random;

public class CrossOverBreeder extends Breeder {
	
	private float crossOverRate;
	private Random random;
	
	@Override
	public String[] merge(String DNA1, String DNA2) {
		String[] newDNAs = new String[2];
		
		if (random.nextFloat() < crossOverRate) {
			crossOver(newDNAs, DNA1, DNA2);
		}
		
		return newDNAs;
	}
	
	public CrossOverBreeder() {
		crossOverRate = 0.7f;
		random = new Random(10);
	}
	
	public CrossOverBreeder(float crossRate) {
		crossOverRate = crossRate;
		random = new Random(10);
	}
	
	private void crossOver(String[] newDNAs, String DNA1, String DNA2)
	{
		int DNALength = DNA1.length();
		
		int crossBit = random.nextInt(DNALength);
		
		for (int i = 0; i < DNALength; i++) {
			if (i < crossBit) {
				newDNAs[1] = newDNAs[1] + DNA1.charAt(i);
				newDNAs[2] = newDNAs[2] + DNA2.charAt(i);
			}
			else {
				newDNAs[1] = newDNAs[1] + DNA2.charAt(i);
				newDNAs[2] = newDNAs[2] + DNA1.charAt(i);
			}
		}
	}

}
