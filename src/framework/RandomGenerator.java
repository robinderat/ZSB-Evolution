package framework;

import java.util.Random;

public class RandomGenerator {
	static private RandomGenerator instance = null;
	
	private Random random;
	
	private RandomGenerator(long seed) {
		random = new Random(seed);
	}
	
	public Random getRandom() { 
		return random;
	}
	
	public static RandomGenerator getInstance() {
		if (RandomGenerator.instance == null) {
			RandomGenerator.instance = new RandomGenerator(System.currentTimeMillis());
		}
		return RandomGenerator.instance;
	}
}
