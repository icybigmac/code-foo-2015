import java.util.Stack;


public class Player {
	private Ship[] fleet;
	
	Player() {
		fleet = new Ship[]{new Ship(2, "Patrol Boat"), new Ship(3, "Destroyer"), new Ship(3, "Submarine"), new Ship(4, "Battleship"), new Ship(5, "Aircraft Carrier")};
	}
	
	public Stack<Ship> getFleet() {
		Stack<Ship> ships = new Stack<>();
		for (int i = fleet.length - 1; i >= 0; i--) {
			ships.push(fleet[i]);
		}
		return ships;
	}
	
	public boolean isDefeated() {
		for (Ship ship : fleet) {
			if (!ship.isDestroyed()) {
				return ship.isDestroyed();
			}
		}
		return true;
	}
	
}
