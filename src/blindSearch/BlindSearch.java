package blindSearch;
import java.util.LinkedList;
import java.util.Stack;
import main.ClausesSet;
import main.Solution;

public abstract class BlindSearch {

	public static Solution DepthFirstSearch(ClausesSet clset, long execTimeMillis) {
		Solution solution = new Solution(clset.getNumberVariables()); /* Store actual solution */
		Stack<NodeBlind> open = new Stack<NodeBlind>(); /* Store candidate nodes to develop */

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

			solution.changeLiteral(Math.abs(randomLiteral)-1, randomLiteral); /* Add generated literal to actual solution */
			if(solution.isSolution(clset)) /* If this solution satisfies all clauses in "clset", exit the loop */
				break;

			solution.changeLiteral(Math.abs(randomLiteral)-1, -randomLiteral); /* Add the NEGATION of generated literal to actual solution */
			if(solution.isSolution(clset)) /* If this solution satisfies all clauses in "clset", exit the loop */
				break;

			open.push(new NodeBlind(-randomLiteral, actual, 2)); /* Add the NEGATION of generated literal to "open" */
			open.push(new NodeBlind(randomLiteral, actual, 1)); /* Add generated literal to "open" */
			solution.deleteLiteral(Math.abs(randomLiteral)-1); /* Delete generated from actual solution */
		}while(! open.empty());

		return solution;
	}


	public static Solution BreadthFirstSearch(ClausesSet clset, long execTimeMillis) {
		LinkedList<Solution> open = new LinkedList<Solution>();
		Solution currentSol = new Solution(clset.getNumberVariables());

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

			currentSol.changeLiteral(Math.abs(randomLiteral)-1, randomLiteral); /* Add generated literal to actual solution */
			if(currentSol.isSolution(clset)) /* If this solution satisfies all clauses in "clset", exit the loop */
				break;
			open.add(new Solution(currentSol));

			currentSol.changeLiteral(Math.abs(randomLiteral)-1, -randomLiteral); /* Add the NEGATION of generated literal to actual solution */
			if(currentSol.isSolution(clset)) /* If this solution satisfies all clauses in "clset", exit the loop */
				break;
			open.add(new Solution(currentSol));
		}while(! open.isEmpty());

		return currentSol;
	}
}
