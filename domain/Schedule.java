package domain;

import java.util.*;

/**
 * Genetic Algorithm
 *
 * @author Arjuna Jayasinghe
 * @version 1.0
 */
public class Schedule {
	private HashMap<Employee, Day[]> employeeScheduleDays;
	private HashMap<Grade, Staffing[]> staffings;
	private ListMap<Grade, Employee> employeesByGrade;
	private ListMap<Grade, Assignment> assignmentsByGrade;
	private boolean isFitnessChanged;
	private double fitness = -1;
	private int conflictCount = 0;

	public Schedule() {
		this.employeeScheduleDays = new HashMap<>();
		this.staffings = new HashMap<>();
		this.employeesByGrade = new ListMap<>();
		this.assignmentsByGrade = new ListMap<>();
	}

	/**
	 * Copy constructor
	 *
	 * @param schedule
	 */
	public Schedule(Schedule schedule) {
		this.employeeScheduleDays = copyEmployeeScheduleDays(schedule.getEmployeeScheduleDays());
		this.staffings = copyStaffings(schedule.getStaffings());
		this.employeesByGrade = schedule.getEmployeesByGrade();
		this.assignmentsByGrade = schedule.getAssignmentsByGrade();
	}

	public HashMap<Employee, Day[]> getEmployeeScheduleDays() {
		return employeeScheduleDays;
	}

	public void setEmployeeScheduleDays(HashMap<Employee, Day[]> employeeScheduleDays) {
		this.isFitnessChanged = true;
		this.employeeScheduleDays = employeeScheduleDays;
		if (employeeScheduleDays != null) {
			employeeScheduleDays.keySet().forEach(e -> {
				employeesByGrade.add(e.getGrade(), e);
			});
		}
	}

	public HashMap<Grade, Staffing[]> getStaffings() {
		return staffings;
	}

	public void setStaffings(HashMap<Grade, Staffing[]> staffings) {
		this.staffings = staffings;
	}

	public void setAssignmentsByGrade(ListMap<Grade, Assignment> assignmentsByGrade) {
		this.assignmentsByGrade = assignmentsByGrade;
	}

	public ListMap<Grade, Assignment> getAssignmentsByGrade() {
		return assignmentsByGrade;
	}

	public ListMap<Grade, Employee> getEmployeesByGrade() {
		return employeesByGrade;
	}

	/**
	 * FIlls schedule by assigning employee for shifts.(Assignment and employee will be selected randomly according to the grade of staffing)
	 * priority is to fill needs of staffing requirement.
	 *
	 * @return
	 */
	public Schedule fillSchedule() {
		staffings.entrySet().forEach((e) -> {
			Grade grade = e.getKey();
			Staffing[] staffingDays = e.getValue();
			for (int x = 0; x < staffingDays.length; x++) {
				int assigned = staffingDays[x].getAssigned();
				int noChangeItCount = 0;
				while (staffingDays[x].isShort()) {
					Assignment ass = assignmentsByGrade.getRandomValue(grade);
					Employee emp = employeesByGrade.getRandomValue(grade);
					if (addShiftToAnEmployee(emp, x, ass)) {
						staffingDays[x].add(); // Increment assigned count by one.
					}
					if (assigned == staffingDays[x].getAssigned()) {
						noChangeItCount++;
					}
					if (noChangeItCount == 5) {
						break;
					}
				}
			}
		});
		isFitnessChanged = true;
		return this;
	}

	/**
	 * Adds a shift to a specific day of employee if there is no already assigned shift.
	 *
	 * @param employee
	 * @param dayIndex
	 * @param shift
	 */
	private boolean addShiftToAnEmployee(Employee employee, int dayIndex, Assignment shift) {
		Day[] scheduleDays = employeeScheduleDays.get(employee);
		if (scheduleDays[dayIndex].getAssignment() == null) {
			scheduleDays[dayIndex].setAssignment(shift);
			return true;
		} else {
			return false;
		}
	}

	public double getFitness() {
		// Calculate fitness only if the schedule is changed.
		if (isFitnessChanged == true) {
			fitness = calculateFitness();
			isFitnessChanged = false;
		}
		return fitness;
	}

	public double calculateFitness() {
		conflictCount = 0;
		// Check consecutive assignments.
		employeeScheduleDays.forEach((emp, scheduleDays) -> {
			int consecAssignment = 0;
			for (int i = 0; i < scheduleDays.length; i++) {
				Day d = scheduleDays[i];
				if (d.getAssignment() == null) {
					consecAssignment = 0;
				} else {
					consecAssignment++;
				}
				if (consecAssignment > 2) {
					conflictCount++;
					consecAssignment = 0;
				}
			}
		});
		// Check staffing need.
		staffings.entrySet().forEach((entry) -> {
			for (Staffing staffing : entry.getValue()) {
				if (staffing.isShort()) {
					conflictCount++;
				}
			}
		});
		return 1 / (double) (conflictCount + 1);
	}

	private HashMap<Employee, Day[]> copyEmployeeScheduleDays(HashMap<Employee, Day[]> map) {
		HashMap<Employee, Day[]> returnMap = new HashMap<>();
		for (Map.Entry<Employee, Day[]> entry : map.entrySet()) {
			Day[] days = entry.getValue();
			for (int i = 0; i < days.length; i++) {
				//days[i] = new Day(days[i].getAssignment());
				days[i] = new Day();
			}
			returnMap.put(entry.getKey(), days);
		}
		return returnMap;
	}

	private HashMap<Grade, Staffing[]> copyStaffings(HashMap<Grade, Staffing[]> map) {
		HashMap<Grade, Staffing[]> returnMap = new HashMap<>();
		for (Map.Entry<Grade, Staffing[]> entry : map.entrySet()) {
			Staffing[] Staffin = entry.getValue();
			for (int i = 0; i < Staffin.length; i++) {
				Staffin[i] = new Staffing(Staffin[i].getNeed());
			}
			returnMap.put(entry.getKey(), Staffin);
		}
		return returnMap;
	}


	/**
	 * Stores multiple values for a single key.
	 *
	 * @param <K>
	 * @param <V>
	 */
	public static class ListMap<K, V> implements Cloneable {
		private HashMap<K, Set<V>> map;

		public ListMap() {
			map = new HashMap<>();
		}

		public void add(K key, V value) {
			Set<V> list = map.get(key);
			if (list == null) {
				list = new HashSet<V>();
				map.put(key, list);
			}
			list.add(value);
		}

		public V getRandomValue(K key) {
			Set<V> valueSet = map.get(key);
			if (valueSet == null || valueSet.size() == 0) {
				return null;
			}
			int randomIndex = (int) (Math.random() * valueSet.size());
			Iterator<V> it = valueSet.iterator();
			int i = 0;
			while (it.hasNext()) {
				V val = it.next();
				if (i == randomIndex) {
					return val;
				}
				i++;
			}
			return null;
		}
	}
}
