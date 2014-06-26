package objects.breeders;

import framework.RandomGenerator;
import framework.Settings;

public abstract class Breeder {
	
	public Breeder()
	{
	}
	
	public String[] breed(String DNA1, String DNA2) {
		return mutate(merge(DNA1, DNA2));
	}
	
	private String[] mutate(String[] DNAs) {
		for (int i = 0; i < DNAs.length; i++) {
			DNAs[i] = mutateString(DNAs[i]);
		}

		return DNAs;
	}
	
	private String mutateString(String DNA) {
		Settings settings = Settings.getInstance();
		RandomGenerator instance = RandomGenerator.getInstance();
		
		String newDNA = "";
		
		for (int i = 0; i < DNA.length(); i++) {
			if (instance.getRandom().nextFloat() < settings.mutationRate) {
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
	
	abstract protected String[] merge(String DNA1, String DNA2);
}
