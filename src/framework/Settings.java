package framework;

public class Settings {
	static private Settings instance = null;
	
	public float fillRate;
	public float mutationRate;
	public float crossoverRate;
	public float matingEnergyCost;
	public float strengthWanderModifier;
	public float eatingEnergyGain;
	
	
	public Settings() {
		fillRate = 0.2f;
		mutationRate = 0.001f;
		crossoverRate = 0.7f;
		matingEnergyCost = 0.55f;
		strengthWanderModifier = 0.2f;
		eatingEnergyGain = 0.8f;
	}
	
	public static Settings getInstance() {
		if (Settings.instance == null) {
			Settings.instance = new Settings();
		}
		return Settings.instance;
	}
}
