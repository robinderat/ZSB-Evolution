package framework;

public class Settings {
	static private Settings instance = null;
	
	public float fillRate;
	public float mutationRate;
	public float crossoverRate;
	public float matingEnergyCost;
	
	public float eatingEnergyGain;
	public float veryHungryThreshold;
	
	public float startEnergyRate; 
	
	public float moveStrengthModifier;
	
	public boolean allowCannibalism;
	
	public Settings() {
		fillRate = 0.4f;
		mutationRate = 0.1f;
		crossoverRate = 0.81f;
		matingEnergyCost = 0.28f;
		eatingEnergyGain = 0.75f;
		veryHungryThreshold = 0.29f;
		startEnergyRate = 0.7f;
		allowCannibalism = false;
		moveStrengthModifier = 0.5f;
	}
	
	public static Settings getInstance() {
		if (Settings.instance == null) {
			Settings.instance = new Settings();
		}
		return Settings.instance;
	}
}
