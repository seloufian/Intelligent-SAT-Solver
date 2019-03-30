package heuristicSearch;

import java.util.ArrayList;
import java.util.Collections;
import main.ClausesSet;
import main.Solution;

public abstract class HeuristicSearch {

	public static Solution heuristicSearch(ClausesSet clset, long execTimeMillis) {
		ArrayList<NodeHeuristic> open = new ArrayList<NodeHeuristic>();
		Solution currentSol = new Solution(clset.getNumberVariables());
		NodeHeuristic currentNode = new NodeHeuristic(currentSol, currentSol.satisfiedClauses(clset), 0);

		int randomLiteral;

		long startTime = System.currentTimeMillis(); /* Save the start time of the search */

		do {
			if((System.currentTimeMillis() - startTime) >= execTimeMillis)
				break; /* If the search time has reached (or exceeded) the allowed run time, finish the search */

			Collections.sort(open); /* Sort the nodes using the evaluation function (f = g + h) */
			
			if(! open.isEmpty()) {
				currentNode = open.remove(0); /* Remove the first element in the "open" list */
				currentSol = currentNode.getSolution();
			}

			if(currentSol.getActiveLiterals() == clset.getNumberVariables())
				continue; /* Maximum number of literals in the solution reached, moreover it isn't solution of SAT problem */

			randomLiteral = currentSol.randomLiteral(clset.getNumberVariables());

			currentSol.changeLiteral(Math.abs(randomLiteral)-1, randomLiteral); /* Add generated literal to actual solution */
			if(currentSol.isSolution(clset)) /* If this solution satisfies all clauses in "clset", exit the loop */
				break;
			open.add(new NodeHeuristic(new Solution(currentSol), currentSol.satisfiedClauses(clset), 0));

			currentSol.changeLiteral(Math.abs(randomLiteral)-1, -randomLiteral); /* Add the NEGATION of generated literal to actual solution */
			if(currentSol.isSolution(clset)) /* If this solution satisfies all clauses in "clset", exit the loop */
				break;
			open.add(new NodeHeuristic(new Solution(currentSol), currentSol.satisfiedClauses(clset), 0));
		}while(! open.isEmpty());

		return currentNode.getSolution();
	}

}
