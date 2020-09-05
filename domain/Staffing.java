package domain;

/**
 * Genetic Algorithm
 *
 * @author Arjuna Jayasinghe
 * @version 1.0
 */
public class Staffing {
	private int need;
	private int assigned;

	public Staffing(int need) {
		this.need = need;
		this.assigned = 0;
	}

	public void add() {
		this.assigned++;
	}

	public void remove() {
		this.assigned--;
	}

	public boolean isShort() {
		return need > assigned;
	}

	public int getAssigned() {
		return this.assigned;
	}

	public int getNeed() {
		return need;
	}
}
