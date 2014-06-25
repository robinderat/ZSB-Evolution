package framework;

public class Settings {
	static private Settings instance = null;
	
	public float fillRate;
	public float mutationRate;
	public float crossoverRate;
	public float matingEnergyCost;
	
	public float strengthWanderModifier;
	public float eatingEnergyGain;
	public float veryHungryThreshold;
	
	public float startEnergyRate; 
	
	public float moveStrengthModifier;
	
	public boolean allowCannibalism;
	
	
	public Settings() {
		fillRate = 0.2f;
		mutationRate = 0.01f;
		crossoverRate = 0.7f;
		matingEnergyCost = 0.45f;
		strengthWanderModifier = 0.2f;
		eatingEnergyGain = 0.8f;
		veryHungryThreshold = 0.25f;
		startEnergyRate = 0.8f;
		allowCannibalism = true;
		moveStrengthModifier = 0.3f;
	}
	
	public static Settings getInstance() {
		if (Settings.instance == null) {
			Settings.instance = new Settings();
		}
		return Settings.instance;
	}
}
