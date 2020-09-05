package domain;

/**
 * Genetic Algorithm
 *
 * @author Arjuna Jayasinghe
 * @version 1.0
 */
public class Employee {
	private String name;
	private Grade grade;

	public Employee(String name, Grade grade) {
		this.name = name;
		this.grade = grade;
	}

	public String getName() {
		return name;
	}

	public Grade getGrade() {
		return grade;
	}

	@Override
	public String toString() {
		return name + "(" + grade + ")";
	}
}
