package blindSearch;

import java.util.LinkedList;
import java.util.Stack;
import main.ClausesSet;
import main.Solution;

public abstract class BlindSearch {

	public static Solution DepthFirstSearch(ClausesSet clset, long execTimeMillis) {
		Solution solution = new Solution(clset.getNumberVariables()); /* Store actual solution */
		Stack<NodeBlind> open = new Stack<NodeBlind>(); /* Store candidate nodes to develop */

		Solution bestSolution = new Solution(solution);

		int actual = -1, randomLiteral;

		long startTime = System.currentTimeMillis(); /* Save the start time of the search */

		do {
			if((System.currentTimeMillis() - startTime) >= execTimeMillis)
				break; /* If the search time has reached (or exceeded) the allowed run time, finish the search */

			if(! open.empty()) { /* There are "nodes" in "open" to develop */
				actual = open.peek().getValue(); /* Get the first (head, last added) element of "open" */
				solution.changeLiteral(Math.abs(actual)-1, actual); /* Add this element (value of literal) to the solution */
			}

			if(solution.getActiveLiterals() == clset.getNumberVariables()) { /* Maximum number of literals in the solution reached, moreover it isn't solution of SAT problem */
				do { /* Recursive delete (go backwards in the tree by removing the nodes) */
					solution.deleteLiteral(Math.abs(open.peek().getValue())-1); /* Delete from solution the first (head, last added) element of "open" */
					actual = open.pop().getChildNumber(); /* Get number [1 : First child, 2 : Second child] of deleted node */
				}while((actual == 2) && (! open.empty())); /* If deleted node is a second child, go backwards in the tree and remove his father */

				continue;
			}

			randomLiteral = solution.randomLiteral(clset.getNumberVariables()); /* Generate a random literal (which doesn't already belong to the solution) */

			for(int i=0; i<2; i++) { /* Loop TWO times for the chosen literal (left child) and its negation (right child) */
				solution.changeLiteral(Math.abs(randomLiteral)-1, i==0 ? randomLiteral : -randomLiteral); /* Add (generated literal) or (its negation) to actual solution */

				if(solution.satisfiedClauses(clset) > bestSolution.satisfiedClauses(clset))
					bestSolution = new Solution(solution); /* If current solution is better, update the best solution */

				if(solution.isSolution(clset)) /* If this solution satisfies all clauses in "clset", return it */
					return bestSolution;

				open.push(new NodeBlind(i==0 ? -randomLiteral : randomLiteral, actual, i==0 ? 2 : 1)); /* Add (the NEGATION of generated literal) or (this literal) to "open" */
			}

			solution.deleteLiteral(Math.abs(randomLiteral)-1); /* Delete generated from actual solution */

		}while(! open.empty());

		return bestSolution;
	}


	public static Solution BreadthFirstSearch(ClausesSet clset, long execTimeMillis) {
		LinkedList<Solution> open = new LinkedList<Solution>();
		Solution currentSol = new Solution(clset.getNumberVariables());

		Solution bestSolution = new Solution(currentSol);

		int randomLiteral;

		long startTime = System.currentTimeMillis(); /* Save the start time of the search */

		do {
			if((System.currentTimeMillis() - startTime) >= execTimeMillis)
				break; /* If the search time has reached (or exceeded) the allowed run time, finish the search */

			if(! open.isEmpty())
				currentSol = open.removeFirst();

			if(currentSol.getActiveLiterals() == clset.getNumberVariables())
				continue;

			randomLiteral = currentSol.randomLiteral(clset.getNumberVariables());

			for(int i=0; i<2; i++) { /* Loop TWO times for the chosen literal (left child) and its negation (right child) */
				currentSol.changeLiteral(Math.abs(randomLiteral)-1, i==0 ? randomLiteral : -randomLiteral);

				if(currentSol.satisfiedClauses(clset) > bestSolution.satisfiedClauses(clset))
					bestSolution = new Solution(currentSol); /* If current solution is better, update the best solution */

				if(currentSol.isSolution(clset)) /* If this solution satisfies all clauses in "clset", return it */
					return bestSolution;

				open.add(new Solution(currentSol));
			}
		}while(! open.isEmpty());

		return bestSolution;
	}
}
