package objects.breeders;

public abstract class Breeder {
	String[] breed(String DNA1, String DNA2) {
		return mutate(merge(DNA1, DNA2));
	}
	
	String[] mutate(String[] DNAs) {
		return DNAs;
	}
	
	abstract String[] merge(String DNA1, String DNA2);
}
