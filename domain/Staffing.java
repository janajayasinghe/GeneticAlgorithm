package domain;

/**
 * Genetic Algorithm
 *
 * @author Arjuna Jayasinghe
 * @version 1.0
 */
public class Staffing {
	private int demand;
	private int supply;

	public Staffing(int demand) {
		this.demand = demand;
		this.supply = 0;
	}

	public void add() {
		this.supply++;
	}

	public void remove() {
		this.supply--;
	}

	public boolean isShort() {
		return demand > supply;
	}

	public int getSupply() {
		return this.supply;
	}

	public int getDemand() {
		return demand;
	}
}
