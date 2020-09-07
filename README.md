# Genetic Algorithm
This algorithm creates roster for the environment where employees are allocated according to the demand.(For example, Nurse schedule) The algorithm can be fine tuned by changing variables in GAConfig. 
GATest is the main class.

# Pseudocode 
```
Creates initial population (List of schedules)
Get best individual(A schedule) by sorting the population according to the fitness.
While(fitness != 1.0){
  Evolve population (crossover and mutation)
  If(noFitnessChange || maxCount){
    Break;
  }
}
```

# Chromosome
One employee has one chromosome (Schedule.employeeScheduleDays) with multiple genes. (Day). Since the Day represents the gene, there will be genes according to the date range of schedule. If you are going to create a schedule for one week, then one chromosome has 7 genes. If the schedule consists of 10 employees, it has 10 chromosomes. 

# Crossover
Crossover algorithm does not use crossover point. Instead it randomly decides whether the parent’s gene should be included in the offspring. Parent is also selected randomly with 0.5 probability for one parent. It means the algorithm is not bias to any parent. 

