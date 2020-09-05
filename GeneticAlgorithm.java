import domain.Day;
import domain.Schedule;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Genetic Algorithm
 *
 * @author Arjuna Jayasinghe
 * @version 1.0
 */
public class GeneticAlgorithm {

	private Schedule scheduleTemplate; // This object is used as a template to create individual in the population.

	public GeneticAlgorithm(Schedule scheduleTemplate) {
		this.scheduleTemplate = scheduleTemplate;
	}

	public Population evolve(Population population) {
		return mutatePopulation(crossoverPopulation(population));
	}

	public Population crossoverPopulation(Population population) {
		// Create empty population
		Population crossoverPopulation = new Population(population.getSchedules().size(), this.scheduleTemplate, true);
		// Keep most fittest individual without crossing. (Most fittest individuals are at the top of the list since it is sorted by fitness)
		IntStream.range(0, GAConfig.ELITE_SCHEDULES).forEach(x -> crossoverPopulation.getSchedules().set(x, population.getSchedules().get(x)));
		// Cross the rest of population
		IntStream.range(GAConfig.ELITE_SCHEDULES, population.getSchedules().size()).forEach(x -> {
			if (GAConfig.CROSSOVER_RATE > Math.random()) {
				// Select parents by tournament.
				Schedule parentSchedule1 = selectSchedulebyTournament(population);
				Schedule parentSchedule2 = selectSchedulebyTournament(population);
				// Cross them and create an offspring.
				crossoverPopulation.getSchedules().set(x, crossoverSchedule(parentSchedule1, parentSchedule2));
			} else {
				// No crossing here.
				crossoverPopulation.getSchedules().set(x, population.getSchedules().get(x));
			}
		});
		return crossoverPopulation;
	}

	/**
	 * Creates a offspring after mating.
	 *
	 * @param parentSchedule1
	 * @param parentSchedule2
	 * @return
	 */
	public Schedule crossoverSchedule(Schedule parentSchedule1, Schedule parentSchedule2) {
		Schedule offspringSchedule = new Schedule(scheduleTemplate).fillSchedule();
		offspringSchedule.getEmployeeScheduleDays().forEach((employee, gaScheduleDays) -> {
			this.geneSwap(gaScheduleDays, parentSchedule1.getEmployeeScheduleDays().get(employee), parentSchedule2.getEmployeeScheduleDays().get(employee));
		});
		return offspringSchedule;
	}

	/**
	 * This method performs multipoint(2 point) crossover and returns offspring.
	 *
	 * @param offspringScheduleDays
	 * @param parent1ScheduleDays
	 * @param parent2ScheduleDays
	 */
	private void geneSwap(Day[] offspringScheduleDays, Day[] parent1ScheduleDays, Day[] parent2ScheduleDays) {
		Day[] tempScheduleDays;
		for (int i = 0; i < offspringScheduleDays.length; i++) {
			if (GAConfig.GENE_SWAP_RATE > Math.random()) {
				// Select a parent randomly to get a gene.(Day)
				if (0.5 > Math.random()) {
					tempScheduleDays = parent1ScheduleDays;
				} else {
					tempScheduleDays = parent2ScheduleDays;
				}
				offspringScheduleDays[i] = tempScheduleDays[i];
			}
		}
	}

	/**
	 * Mutates genes randomly.
	 *
	 * @param population
	 * @return
	 */
	public Population mutatePopulation(Population population) {
		// Creates empty population
		Population mutatePopulation = new Population(population.getSchedules().size(), this.scheduleTemplate, false);
		ArrayList<Schedule> schedules = mutatePopulation.getSchedules();
		// Elites are ignored from mutation
		IntStream.range(0, GAConfig.ELITE_SCHEDULES).forEach(x -> {
			schedules.set(x, population.getSchedules().get(x));
		});
		// This schedule is used to mutation.
		Schedule mutateSchedule = new Schedule(scheduleTemplate).fillSchedule();
		// send others to the mutation algorithm.
		IntStream.range(GAConfig.ELITE_SCHEDULES, population.getSchedules().size()).forEach(x -> {
			schedules.set(x, mutateSchedule(mutateSchedule, population.getSchedules().get(x)));
		});
		return mutatePopulation;
	}

	public Schedule mutateSchedule(Schedule mutateSchedule, Schedule schedule) {
		schedule.getEmployeeScheduleDays().forEach((employee, gaScheduleDays) -> {
			// Select one chromosome(one employee schedule) randomly for the mutation.
			if (GAConfig.MUTATION_RATE > Math.random()) {
				for (int i = 0; i < gaScheduleDays.length; i++) {
					if (GAConfig.MUTATION_GENE_RATE > Math.random()) {
						gaScheduleDays[i] = mutateSchedule.getEmployeeScheduleDays().get(employee)[i];
					}
				}
			}
		});
		return schedule;
	}

	public Schedule selectSchedulebyTournament(Population population) {
		Population tournamentPopulation = new Population(GAConfig.TOURNAMENT_SIZE, scheduleTemplate, false);
		IntStream.range(0, GAConfig.TOURNAMENT_SIZE).forEach(x -> {
			tournamentPopulation.getSchedules().set(x, population.getSchedules().get((int) (Math.random() * population.getSchedules().size())));
		});
		tournamentPopulation.sortByFitness();
		return tournamentPopulation.getFirst();
	}

	public Schedule getScheduleTemplate() {
		return scheduleTemplate;
	}
}
