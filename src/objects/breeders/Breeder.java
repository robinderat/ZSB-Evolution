package objects.breeders;

import framework.RandomGenerator;

public abstract class Breeder {
	
	protected float mutationRate;
	
	public void setMutationRate(float rate)
	{
		mutationRate = rate;
	}
	
	public Breeder(float rate)
	{
		mutationRate = rate;
	}
	
	public Breeder()
	{
		mutationRate = 0.001f;
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
		RandomGenerator instance = RandomGenerator.getInstance();
		String newDNA = "";
		
		for (int i = 0; i < DNA.length(); i++) {
			if (instance.getRandom().nextFloat() < mutationRate) {
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
