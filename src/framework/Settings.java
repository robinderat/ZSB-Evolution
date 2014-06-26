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
	
	public int iterationRestAmount;
	
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
		iterationRestAmount = 1;
	}

	// set your own initial settings here
	public void newSettings(char c){
		switch (c) {
			case 'j': setSettings(0.2f, 0.05f, 0.9f, 0.16f, 0.65f, 0.29f, 1.0f, true, 0.3f, 30); break;
			case 'r': setSettings(0.4f, 0.00f, 0.81f, 0.30f, 0.75f, 0.29f, 0.7f, false, 0.5f, 50); break;
			case 'm': setSettings(0.4f, 0.089f, 0.53f, 0.21f, 2.0f, 0.29f, 0.7f, false, 1.47f, 100); break;
			case 'k': setSettings(0.65f, 0.11f, 0.45f, 0.25f, 1.85f, 0.29f, 0.85f, false, 1.4f, 75);break;
		}
	}
	
	// change all settings
	public void setSettings (float fR, float mR, float cR, float mEC, float eEG, float vHT, float sER, boolean aC, float mSM, int iRA){
		fillRate = fR;
		mutationRate = mR;
		crossoverRate = cR;
		matingEnergyCost = mEC;
		eatingEnergyGain = eEG;
		veryHungryThreshold = vHT;
		startEnergyRate = sER;
		allowCannibalism = aC;
		moveStrengthModifier = mSM;
		iterationRestAmount = iRA;
	}
	
	public static Settings getInstance() {
		if (Settings.instance == null) {
			Settings.instance = new Settings();
		}
		return Settings.instance;
	}
}
