/**
 * Genetic Algorithm
 *
 * @author Arjuna Jayasinghe
 * @version 1.0
 */
public class GAConfig {
	public static final int POPULATION_SIZE = 30;
	public static final double MUTATION_RATE = 0.2;
	public static final double MUTATION_GENE_RATE = 0.6;
	public static final double CROSSOVER_RATE = 0.9;
	public static final double GENE_SWAP_RATE = 0.8;
	public static final int TOURNAMENT_SIZE = 3;
	public static final int ELITE_SCHEDULES = 2;
	public static final int MAX_EVOLUTIONS = 50000;
	public static final int BREAK_ON_NOFITNESSCHANGE = 5000;	
}
