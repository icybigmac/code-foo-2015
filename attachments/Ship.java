
public class Ship {
	private int length;
	private boolean[] health;
	private String name;
	
	Ship(int length, String name) {
		this.length = length;
		health = new boolean[length];
		this.name = name;
	}
	
	public int length() {
		return length;
	}
	
	public String name() {
		return name;
	}
	
	public boolean isDestroyed() {
		for (boolean b : health) {
			if (!b) {
				return b;
			}
		}
		return true;
	}
	
	public void damage(int location) {
		health[location] = true;
	}
	
	public String toString(boolean toggle) {
		if (isDestroyed()) {
			if (toggle) {
				return "You sunk the enemy " + name + "!";
			} else {
				return "The enemy sunk your " + name + "!";
			}
		}
		return "";
	}
}
