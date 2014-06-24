package objects.breeders;

import java.util.Random;

public class CrossOverBreeder extends Breeder {
	
	private float crossOverRate = 0.7f;
	private Random random;
	
	@Override
	public String[] merge(String DNA1, String DNA2) {
		String[] newDNAs = new String[2];
		
		if (random.nextFloat() < 0.7)
		{
		}
		
		return newDNAs;
	}
	
	public CrossOverBreeder()
	{
		crossOverRate = 0.7f;
		random = new Random(10);
	}
	
	public CrossOverBreeder(float crossRate)
	{
		crossOverRate = crossRate;
		random = new Random(10);
	}

}
