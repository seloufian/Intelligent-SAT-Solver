package ga;
import java.util.ArrayList;
import java.util.Random;
import main.ClausesSet;
import main.Solution;

public class GA {

	public static Solution searchGA(ClausesSet clset, int populationSize, int crossoverRate, int mutationRate, int maxIterations, long execTimeMillis) {
		ArrayList<Individual> population = new ArrayList<Individual>(); /* List of individuals of the population */
		Individual tempInd = null; /* Temporary individual (used to create the initial population) */
		int countInd = 0, index = 0; /* Counters (used to create the initial population) */
		int[] choosedInd = new int[2]; /* The two individuals chosen for crossover process */
		Individual[] resCrInd = new Individual[2]; /* Results of crossing between two individuals */
		Random random = new Random(); /* Random Object (for different random values) */

		long startTime = System.currentTimeMillis(); /* Save the start time of the search */

		while(countInd < populationSize) { /* Create the initial population */
			tempInd = new Individual(clset); /* Create an individual with a random solution */

			for(index=0; index<population.size(); index++) /* Check if there isn't already an individual in the population with the same solution */
				if(tempInd.getSolution().equals(population.get(index).getSolution()))
					break; /* If there is an individual with the same solution, exit the loop */

			if(index == population.size()) { /* The loop has arrived at the end of the list "population", no individual found with the same solution as "tempInd" */
				population.add(new Individual(tempInd)); /* Add the new Individual "tempInd" to the population */
				countInd++; /* Increase the number of individuals in the population */
			}
		}

		for(int iteration=0; iteration<maxIterations; iteration++) {
			if((System.currentTimeMillis() - startTime) >= execTimeMillis)
				break; /* If the search time has reached (or exceeded) the allowed run time, finish the search */

			do { /* Selection process : Choose TWO random individuals from the population */
				choosedInd[0] = random.nextInt(populationSize);
				choosedInd[1] = random.nextInt(populationSize);
			}while(choosedInd[0] == choosedInd[1]); /* We must choose two DIFFERENT individuals (The same ones have no effect in crossover process) */

			if(random.nextInt(101) < crossoverRate) { /* If "Rc" allows the crossover process (value "101" used to take the interval [0;100])*/
				resCrInd[0] = GA.crossover(clset, population.get(choosedInd[0]), population.get(choosedInd[1]), random.nextInt(clset.getNumberVariables()), true);
				resCrInd[1] = GA.crossover(clset, population.get(choosedInd[0]), population.get(choosedInd[1]), random.nextInt(clset.getNumberVariables()), false);

				if(random.nextInt(101) < mutationRate) { /* If "Rm" allows the mutation process (value "101" used to take the interval [0;100])*/
					resCrInd[0].makeMutation(clset, random.nextInt(clset.getNumberVariables()));
					resCrInd[1].makeMutation(clset, random.nextInt(clset.getNumberVariables()));
				}

				for(int i=0; i<population.size(); i++) { /* Update current population with new individuals */
					if((resCrInd[0] != null) && (population.get(i).getEvaluation() < resCrInd[0].getEvaluation())) {
						population.set(i, new Individual(resCrInd[0])); /* New individual is better, replace current individual with the new one */
						resCrInd[0] = null; /* Use first new individual only once */
					}
					else if((resCrInd[1] != null) && (population.get(i).getEvaluation() < resCrInd[1].getEvaluation())) {
						population.set(i, new Individual(resCrInd[1])); /* New individual is better, replace current individual with the new one */
						resCrInd[1] = null; /* Use second new individual only once */
					}

					if((resCrInd[0] == null) && (resCrInd[1] == null))
						break; /* The two new individuals already used (replaced an old ones in "population"), exit the loop */
				}
			}
		}

		Solution bestSol = new Solution(clset.getNumberVariables()); /* "Search time"/"maxIterations" reached, return the best solution found */
		for(Individual ind : population) /* Search for the best individual (best solution <=> best evaluation) in current population */
			if(bestSol.satisfiedClauses(clset) < ind.getSolution().satisfiedClauses(clset))
				bestSol = ind.getSolution(); /* Update the best solution */

		return bestSol;
	}


	private static Individual crossover(ClausesSet clset, Individual ind1, Individual ind2, int place, boolean firstCross) {
		Solution resultSol = new Solution(clset.getNumberVariables());

		if(firstCross) {
			for(int i=0; i<place; i++)
				resultSol.changeLiteral(i, ind1.getSolution().getLiteral(i));

			for(int i=place; i<resultSol.getSolutionSize(); i++)
				resultSol.changeLiteral(i, ind2.getSolution().getLiteral(i));
		}else { /* Second cross */
			for(int i=0; i<place; i++)
				resultSol.changeLiteral(i, ind2.getSolution().getLiteral(i));

			for(int i=place; i<resultSol.getSolutionSize(); i++)
				resultSol.changeLiteral(i, ind1.getSolution().getLiteral(i));
		}

		return(new Individual(clset, resultSol));
	}
}
