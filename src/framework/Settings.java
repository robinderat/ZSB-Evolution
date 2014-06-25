package framework;

public class Settings {
	static private Settings instance = null;
	
	public double fillRate;
	public float mutationRate;
	public float crossoverRate;
	
	
	public Settings() {
		fillRate = 0.2;
		mutationRate = 0.001f;
		crossoverRate = 0.7f;
	}
	
	public static Settings getInstance() {
		if (Settings.instance == null) {
			Settings.instance = new Settings();
		}
		return Settings.instance;
	}
}
