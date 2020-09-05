import domain.Schedule;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Genetic Algorithm
 *
 * @author Arjuna Jayasinghe
 * @version 1.0
 */
public class Population {

	private ArrayList<Schedule> schedules;
	private Schedule scheduleTemplate;

	public Population(int size, Schedule scheduleTemplate2, boolean fillAssignment) {
		this.schedules = new ArrayList<>(size);
		this.scheduleTemplate = new Schedule(scheduleTemplate2);
		IntStream.range(0, size).forEach(x -> {
			this.schedules.add(fillAssignment && this.scheduleTemplate != null ? this.scheduleTemplate.fillSchedule() : null);
		});
	}

	public Schedule getScheduleTemplate() {
		return scheduleTemplate;
	}

	public ArrayList<Schedule> getSchedules() {
		return schedules;
	}

	/**
	 * Sorts schedules by fitness.
	 */
	public void sortByFitness() {
		this.schedules.sort((schedule1, schedule2) -> {
			int returnValue = 0;
			if (schedule1.getFitness() > schedule2.getFitness()) {
				returnValue = -1;
			} else if (schedule1.getFitness() < schedule2.getFitness()) {
				returnValue = 1;
			}
			return returnValue;
		});
	}

	/**
	 * returns the fitness of the first schedule after sorting.
	 *
	 * @return
	 */
	public double getBestFitness() {
		sortByFitness();
		return getFirst().getFitness();
	}

	/**
	 * Returns the first schedule.
	 *
	 * @return
	 */
	public Schedule getFirst() {
		return getSchedules().get(0);
	}
}
