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
	
	public int cellTypesAmount;
	
	// initial settings. change them here while testing the game to fit your own preferences
	// or make a case in the method below
	public Settings() {
		fillRate = 0.4f;
		mutationRate = 0.1f;
		crossoverRate = 0.81f;
		matingEnergyCost = 0.38f;
		eatingEnergyGain = 0.75f;
		veryHungryThreshold = 0.29f;
		startEnergyRate = 0.7f;
		allowCannibalism = false;
		moveStrengthModifier = 0.5f;
		cellTypesAmount = 2;
	}

	// set your own initial settings here
	public void newSettings(char c){
		switch (c) {
			case 'j': setSettings(0.2f, 0.1f, 1.0f, 0.16f, 0.75f, 0.29f, 1.0f, true, 0.3f, 2); break;
			case 'r': break;
			case 'm': break;
			case 'k': break;
		}
	}
	
	// change all settings
	public void setSettings (float fR, float mR, float cR, float mEC, float eEG, float vHT, float sER, boolean aC, float mSM, int cTA){
		fillRate = fR;
		mutationRate = mR;
		crossoverRate = cR;
		matingEnergyCost = mEC;
		eatingEnergyGain = eEG;
		veryHungryThreshold = vHT;
		startEnergyRate = sER;
		allowCannibalism = aC;
		moveStrengthModifier = mSM;
		cellTypesAmount = cTA;
	}
	
	public static Settings getInstance() {
		if (Settings.instance == null) {
			Settings.instance = new Settings();
		}
		return Settings.instance;
	}
}
