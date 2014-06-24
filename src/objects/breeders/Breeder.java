package objects.breeders;

import java.util.Random;

public abstract class Breeder {
	
	protected float mutationRate;
	protected static Random random;
	
	public Breeder(float rate)
	{
		mutationRate = rate;
		random = new Random(10);
	}
	
	public Breeder()
	{
		mutationRate = 0.001f;
		random = new Random(10);
	}
	
	String[] breed(String DNA1, String DNA2) {
		return mutate(merge(DNA1, DNA2));
	}
	
	String[] mutate(String[] DNAs) {
		for (int i = 0; i < DNAs.length; i++) {
			DNAs[i] = mutateString(DNAs[i]);
		}

		return DNAs;
	}
	
	private String mutateString(String DNA) {
		String newDNA = "";
		
		for (int i = 0; i < DNA.length(); i++) {
			if (random.nextFloat() < mutationRate) {
				if (DNA.charAt(i) == '0')
				{
					newDNA = newDNA + '1';
				}
				else
				{
					newDNA = newDNA + '0';
				}
			}
			else
			{
				newDNA = newDNA + DNA.charAt(i);
			}
		}
		return newDNA;
	}
	
	abstract String[] merge(String DNA1, String DNA2);
}
