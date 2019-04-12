package heuristicSearch;

import java.util.ArrayList;
import java.util.Collections;
import main.ClausesSet;
import main.Solution;

public abstract class HeuristicSearch {

	public static Solution heuristicSearch(ClausesSet clset, long execTimeMillis) {
		int clsetSat = clset.getNumberClause(); /* Number of clauses of "clset", to optimize execution time (memory access) */

		ArrayList<NodeHeuristic> open = new ArrayList<NodeHeuristic>();

		Solution currentSol = new Solution(clset.getNumberVariables());
		int currentSolSat = currentSol.satisfiedClauses(clset); /* Number of satisfied clauses by current solution, to optimize execution time */

		NodeHeuristic currentNode = new NodeHeuristic(currentSol, 0, clsetSat-currentSolSat);

		Solution bestSolution = new Solution(currentSol);
		int bestSolutionSat = currentSolSat; /* Number of satisfied clauses by the best solution */

		int randomLiteral;

		long startTime = System.currentTimeMillis(); /* Save the start time of the search */

		do {
			if((System.currentTimeMillis() - startTime) >= execTimeMillis)
				break; /* If the search time has reached (or exceeded) the allowed run time, finish the search */

			Collections.sort(open); /* Sort in ascending order the nodes using the evaluation function (f = g + h) */

			if(! open.isEmpty()) {
				currentNode = open.remove(0); /* Remove the first element in the "open" list */
				currentSol = new Solution(currentNode.getSolution());
			}

			if(currentSol.getActiveLiterals() == clset.getNumberVariables())
				continue; /* Maximum number of literals in the solution reached, moreover it isn't solution of SAT problem */

			randomLiteral = currentSol.randomLiteral(clset.getNumberVariables());

			for(int i=0; i<2; i++) { /* Loop TWO times for the chosen literal (left child) and its negation (right child) */
				currentSol.changeLiteral(Math.abs(randomLiteral)-1, i==0 ? randomLiteral : -randomLiteral);
				currentSolSat = currentSol.satisfiedClauses(clset);

				if(currentSolSat > bestSolutionSat) /* If current solution is better, update the best solution */
					bestSolution = new Solution(currentSol);

				if(currentSolSat == clsetSat) /* If this solution satisfies all clauses in "clset", return it */
					return bestSolution;

				open.add(new NodeHeuristic(new Solution(currentSol), currentNode.getSolution().sameSatisfiedClausesAsLiteral(clset,
						i==0 ? randomLiteral : -randomLiteral), clsetSat-currentSolSat));
			}
		}while(! open.isEmpty());

		return bestSolution;
	}
}
