package bso;
import java.util.ArrayList;
import main.ClausesSet;
import main.Solution;

public abstract class BSO {

	public static Solution searchBSO(ClausesSet clset, int flip, int numBees, int maxChances, int numLocalSearch, int maxIterations, boolean withThreads, long execTimeMillis) {
		ArrayList<Taboo> taboo = new ArrayList<Taboo>();

		DanceList dance = new DanceList();

		ArrayList<Bee> bees;

		int iteration = 0;
		
		long startTime = System.currentTimeMillis(); /* Save the start time of the search */

		Taboo sRef = new Taboo(clset.getNumberVariables(), maxChances);
		sRef.getSolution().randomSolution();
		taboo.add(sRef);

		Solution bestSolution = sRef.getSolution(); /* Initialize the best solution as the one generated randomly */

		/* The code must be duplicated to improve performance (the "IF" condition is outside the "WHILE" loop) */
		if(withThreads) { /* "With threads" version choosed : Each bee is a thread */
			while((iteration != maxIterations) && (! sRef.getSolution().isSolution(clset))) {

				if((System.currentTimeMillis() - startTime) >= execTimeMillis)
					break; /* If the search time has reached (or exceeded) the allowed run time, finish the search */

				/* Construct the list of artificial bees, each bee has a solution (search point) determined from the reference solution "sRef" */
				bees = determineSearchPoints(sRef, flip, numBees, clset, numLocalSearch);

				for(Bee bee : bees) /* For each bee, search locally for a better solution from its research point */
					bee.start();

				for(Bee bee : bees) { /* Add the solution found for each bee in the table "dance" */
					try { bee.join(); } catch (InterruptedException e) { e.printStackTrace(); }
					dance.add(bee.getSolution());
				}

				if(sRef.getSolution().satisfiedClauses(clset) > bestSolution.satisfiedClauses(clset))
					bestSolution = sRef.getSolution();

				sRef = bestSolution(sRef, taboo, dance, clset, maxChances); /* Find the best solution in the set {"sRef" union "dance"} */

				iteration++;

				dance.clear();		bees.clear(); /* Clear "dance" and "bees" tables for the next iteration */
			}
		}
		else { /* Sequential version choosed (without threads) */
			while((iteration != maxIterations) && (! sRef.getSolution().isSolution(clset))) {

				if((System.currentTimeMillis() - startTime) >= execTimeMillis)
					break; /* If the search time has reached (or exceeded) the allowed run time, finish the search */

				/* Construct the list of artificial bees, each bee has a solution (search point) determined from the reference solution "sRef" */
				bees = determineSearchPoints(sRef, flip, numBees, clset, numLocalSearch);

				for(Bee bee : bees) /* For each bee, search locally for a better solution from its research point */
					bee.localSearch();

				for(Bee bee : bees) /* Add the solution found for each bee in the table "dance" */
					dance.add(bee.getSolution());

				if(sRef.getSolution().satisfiedClauses(clset) > bestSolution.satisfiedClauses(clset))
					bestSolution = sRef.getSolution();

				sRef = bestSolution(sRef, taboo, dance, clset, maxChances); /* Find the best solution in the set {"sRef" union "dance"} */

				iteration++;

				dance.clear();		bees.clear(); /* Clear "dance" and "bees" tables for the next iteration */
			}
		}

		return sRef.getSolution();
	}


	private static ArrayList<Bee> determineSearchPoints(Taboo sRef, int flip, int numBees, ClausesSet clset, int numLocalSearch) {
		ArrayList<Bee> bees = new ArrayList<Bee>(); /* List of generated artificial bees */
		Solution sol; /* Solution of the current bee */
		int step = 0, p; /* step : Value to add to the multiple of "flip" , p : Value to be multiplied by "flip" */

		while((numBees > 0) && (step < flip)) {
			sol = new Solution(sRef.getSolution());
			p = 0;

			do { /* Invert literals in positions : multiples of "flip" with the step "step" */
				sol.invertLiteral(flip*p+step);
				p++;
			}while(flip*p+step < sRef.getSolution().getSolutionSize()); /* While size of the solution "not reached" or "not exceeded" */

			bees.add(new Bee(sol, clset, numLocalSearch));
			step++;
			numBees--; /* A search point assigned to a bee, decrement "numBees" */
		}

		return bees;
	}


	private static Taboo bestSolution(Taboo sRef, ArrayList<Taboo> taboo, DanceList dance, ClausesSet clset, int maxChances) {
		Integer indexBest; /* Index current best solution in quality/diversity in "dance" */

		do { /* Find the best solution in quality/diversity in the set {"sRef" union "dance"} */
			indexBest = dance.bestSolutionQuality(clset); /* Find the index of best solution in QUALITY in "dance" */

			/* Calculate "deltaF": The difference in the number of satisfied clauses between the best solution in quality in "dance" and the current referece solution "sRef" */
			int deltaF = dance.get(indexBest).satisfiedClauses(clset) - sRef.getSolution().satisfiedClauses(clset);

			if(deltaF > 0) { /* The best solution in quality in "dance" is better in quality than "sRef" */
				if(taboo.contains(new Taboo(dance.get(indexBest)))) { /* If "taboo" already contains this solution */
					dance.remove(dance.get(indexBest)); /* Remove it */
					indexBest = null; /* The best solution in quality has not been found yet */
				}
			}
			else { /* If current "sRef" still better in quality */
				indexBest = -1; /* The best solution still the old "sRef" */
				sRef.setChances(sRef.getChances()-1); /* Decrement its number of chances */

				if(sRef.getChances() <= 0) { /* If all chances of "sRef" were used */
					do { /* Find the best solution in diversity in "dance" which isn't already in "taboo" */
						indexBest = dance.bestSolutionDiversity(clset); /* find the best solution in DIVERSITY in "dance" */
						if(taboo.contains(new Taboo(dance.get(indexBest)))) { /* If "taboo" already contains this solution */
							dance.remove(dance.get(indexBest)); /* Remove it */
							indexBest = null; /* The best solution in diversity has not been found yet */
						}
					}while((! dance.isEmpty()) && (indexBest == null));
				}
			}
		}while((! dance.isEmpty()) && (indexBest == null));

		if(indexBest == null) { /* If ALL of the solutions in the set {"sRef" union "dance"} already exist in "taboo" */
			Solution s;
			do { /* Generate a random solution that doesn't already exist in "taboo" */
				s = new Solution(clset.getNumberVariables());
				s.randomSolution();
				sRef = new Taboo(s, maxChances);
			}while(taboo.contains(sRef));
		}
		else if(indexBest >= 0) { /* There is a better solution in quality/diversity in "dance" that isn't already in "taboo" */
				sRef = new Taboo(dance.get(indexBest), maxChances);
				taboo.add(sRef);
			}
		else { /* The current "sRef" still better in quality (indexBest == -1) */
			taboo.remove(sRef); /* Delete "sRef" (to recreate it with the new number of chances) */
			taboo.add(new Taboo(sRef)); /* Add the new reference solution "sRef" */
		}

		return sRef;
	}
}
