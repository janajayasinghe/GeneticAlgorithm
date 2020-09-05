import domain.*;

import java.time.LocalTime;
import java.util.HashMap;

/**
 * Genetic Algorithm
 *
 * @author Arjuna Jayasinghe
 * @version 1.0
 */
public class GATest {

	private Schedule scheduleTemplate;

	public void initData() {
		// Grades
		Grade g1 = new Grade("G1");
		Grade g2 = new Grade("G2");
		Grade g3 = new Grade("G3");
		// Employees
		Employee e1 = new Employee("Niranja", g3);
		Employee e2 = new Employee("Arjuna", g1);
		Employee e3 = new Employee("Jayasinghe", g2);
		Employee e4 = new Employee("Anusha", g1);
		Employee e5 = new Employee("Sandamali", g2);
		Employee e6 = new Employee("Edirisinghe", g3);
		Employee e7 = new Employee("Nilantha", g2);
		Employee e8 = new Employee("Ayesha", g1);
		Employee e9 = new Employee("Sampath", g2);
		Employee e10 = new Employee("Niranjalee", g1);
		Employee e11 = new Employee("Supun", g2);
		Employee e12 = new Employee("Asanka", g3);
		Employee e13 = new Employee("Niranja1", g3);
		Employee e14 = new Employee("Arjuna1", g1);
		Employee e15 = new Employee("Jayasinghe1", g2);
		Employee e16 = new Employee("Anusha1", g1);
		Employee e17 = new Employee("Sandamali1", g2);
		Employee e18 = new Employee("Edirisinghe1", g3);

		// Create schedule template.
		scheduleTemplate = new Schedule();
		HashMap<Employee, Day[]> employeeScheduleDays = new HashMap<>();
		fillDays(employeeScheduleDays, e1);
		fillDays(employeeScheduleDays, e2);
		fillDays(employeeScheduleDays, e3);
		fillDays(employeeScheduleDays, e4);
		fillDays(employeeScheduleDays, e5);
		fillDays(employeeScheduleDays, e6);
		fillDays(employeeScheduleDays, e7);
		fillDays(employeeScheduleDays, e8);
		fillDays(employeeScheduleDays, e9);
		fillDays(employeeScheduleDays, e10);
		fillDays(employeeScheduleDays, e11);
		fillDays(employeeScheduleDays, e12);
		fillDays(employeeScheduleDays, e13);
		fillDays(employeeScheduleDays, e14);
		fillDays(employeeScheduleDays, e15);
		fillDays(employeeScheduleDays, e16);
		fillDays(employeeScheduleDays, e17);
		fillDays(employeeScheduleDays, e18);
		scheduleTemplate.setEmployeeScheduleDays(employeeScheduleDays);
		// Staffing.
		HashMap<Grade, Staffing[]> staffingMap = new HashMap<>();
		Staffing[] staffing1 = new Staffing[]{new Staffing(2), new Staffing(5), new Staffing(2), new Staffing(2), new Staffing(1), new Staffing(4), new Staffing(2), new Staffing(1), new Staffing(1), new Staffing(1)};
		Staffing[] staffing2 = new Staffing[]{new Staffing(5), new Staffing(3), new Staffing(1), new Staffing(1), new Staffing(2), new Staffing(1), new Staffing(0), new Staffing(0), new Staffing(1), new Staffing(2)};
		Staffing[] staffing3 = new Staffing[]{new Staffing(1), new Staffing(5), new Staffing(2), new Staffing(0), new Staffing(3), new Staffing(1), new Staffing(0), new Staffing(2), new Staffing(0), new Staffing(2)};

		staffingMap.put(g1, staffing1);
		staffingMap.put(g2, staffing2);
		staffingMap.put(g3, staffing3);
		scheduleTemplate.setStaffings(staffingMap);

		// Assignments by grades
		Schedule.ListMap<Grade, Assignment> assignmentsByGrade = new Schedule.ListMap<>();
		assignmentsByGrade.add(g1, new Assignment(LocalTime.of(6, 30), LocalTime.of(11, 30)));
		assignmentsByGrade.add(g1, new Assignment(LocalTime.of(10, 30), LocalTime.of(14, 30)));
		assignmentsByGrade.add(g1, new Assignment(LocalTime.of(16, 30), LocalTime.of(22, 30)));
		assignmentsByGrade.add(g1, new Assignment(LocalTime.of(2, 11), LocalTime.of(3, 15)));
		assignmentsByGrade.add(g1, new Assignment(LocalTime.of(5, 31), LocalTime.of(23, 30)));

		assignmentsByGrade.add(g2, new Assignment(LocalTime.of(8, 0), LocalTime.of(15, 0)));
		assignmentsByGrade.add(g2, new Assignment(LocalTime.of(14, 0), LocalTime.of(23, 0)));
		assignmentsByGrade.add(g2, new Assignment(LocalTime.of(14, 10), LocalTime.of(11, 0)));
		assignmentsByGrade.add(g2, new Assignment(LocalTime.of(14, 5), LocalTime.of(18, 0)));

		assignmentsByGrade.add(g3, new Assignment(LocalTime.of(7, 15), LocalTime.of(11, 45)));
		assignmentsByGrade.add(g3, new Assignment(LocalTime.of(13, 45), LocalTime.of(20, 15)));
		assignmentsByGrade.add(g3, new Assignment(LocalTime.of(20, 15), LocalTime.of(3, 45)));
		assignmentsByGrade.add(g3, new Assignment(LocalTime.of(7, 15), LocalTime.of(10, 4)));
		assignmentsByGrade.add(g3, new Assignment(LocalTime.of(17, 15), LocalTime.of(21, 40)));
		scheduleTemplate.setAssignmentsByGrade(assignmentsByGrade);
	}


	/**
	 * Creates empty schedule(Day objects for a specific day range) for an employee.
	 *
	 * @param employeeScheduleDays
	 * @param employee
	 */
	private void fillDays(HashMap<Employee, Day[]> employeeScheduleDays, Employee employee) {
		Day[] days = new Day[10];
		for (int i = 0; i < 10; i++) {
			days[i] = new Day();
		}
		employeeScheduleDays.put(employee, days);
	}

	public void runGA() {
		// Create aniInitial population
		Population population = new Population(GAConfig.POPULATION_SIZE, scheduleTemplate, true);
		GeneticAlgorithm ga = new GeneticAlgorithm(scheduleTemplate);
		int evolutionCount = 0;
		double currentBestFitness = population.getBestFitness();
		printSchedule(evolutionCount, population.getFirst());
		int noFitnessChange = 0;
		while (currentBestFitness != 1.0) {
			population = ga.evolve(population);
			evolutionCount++;
			double bestFitness = population.getBestFitness();
			if (bestFitness == currentBestFitness) {
				noFitnessChange++;
			}
			currentBestFitness = bestFitness;
			printSchedule(evolutionCount, population.getFirst());
			// Break the evolution if the evolutionCount has reached the maximum or the fitness has not been changed in last 100 iterations.
			if (noFitnessChange == 500 || evolutionCount >= GAConfig.MAX_EVOLUTIONS) {
				break;
			}
		}
	}

	public void printSchedule(int evolutionCount, Schedule finalSchedule) {
		System.out.println("Evolution count : " + evolutionCount);
		finalSchedule.getEmployeeScheduleDays().forEach((e, days) -> {
			System.out.printf("%20s -> ", e);
			for (Day d : days) {
				System.out.printf("%15s |  ", d);
			}
			System.out.println("");
		});
		System.out.println("Fitness : " + finalSchedule.getFitness());
		System.out.print("-------------------------------------------------------------");
		System.out.println("-------------------------------------------------------------");
	}
	
	public static void main(String[] args){
		GATest gaTest = new GATest();
		gaTest.initData();
		gaTest.runGA();
	}
}
