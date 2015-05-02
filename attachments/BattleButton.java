import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;


public class BattleButton extends JButton {
	private int x;
	private int y;
	private boolean hasShip;
	private boolean isHit;
	private Ship ship;
	private int location;
	private boolean isEnemy;
	
	BattleButton(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		hasShip = false;
		isHit = false;
		isEnemy = false;
	}
	
	public int getThisX() {
		return x;
	}
	
	public int getThisY() {
		return y;
	}
	
	public void setIsEnemy(boolean isEnemy) {
		this.isEnemy = isEnemy;
	}
	
	public boolean getHasShip() {
		return hasShip;
	}
	
	public boolean getIsHit() {
		return isHit;
	}
	
	public void setIsHit(boolean hit) {
		isHit = hit;
	}
	
	public void addShip(Ship ship, int location) {
		if (!hasShip) {
			this.ship = ship;
			this.location = location;
			this.hasShip = true;
		}
	}
	
	public Ship getShip() {
		return ship;
	}
	
	public int getPlace() {
		return location;
	}
	
	public String toString() {
		char [] ch = {' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
		if (isHit) {
			if (hasShip) {
				if (isEnemy) {
					return "You just hit the enemy at position [" + ch[x] + ", " + y + "]!";
				} else {
					return "The enemy just hit you at position [" + ch[x] + ", " + y + "]!";
				}
			} else {
				if (isEnemy) {
					return "You missed the enemy at position [" + ch[x] + ", " + y + "]!";
				} else {
					return "The enemy missed you at position [" + ch[x] + ", " + y + "]!";
				}
			}
		}
		return "";
	}

	public boolean getIsEnemy() {
		return isEnemy;
	}
}
