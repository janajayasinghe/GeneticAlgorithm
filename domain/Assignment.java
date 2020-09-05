package domain;

import java.time.LocalTime;

/**
 * Genetic Algorithm
 *
 * @author Arjuna Jayasinghe
 * @version 1.0
 */
public class Assignment implements Cloneable {
	private LocalTime start;
	private LocalTime end;

	public Assignment(LocalTime start, LocalTime end) {
		this.start = start;
		this.end = end;
	}

	public LocalTime getStart() {
		return start;
	}

	public LocalTime getEnd() {
		return end;
	}

	@Override
	protected Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
