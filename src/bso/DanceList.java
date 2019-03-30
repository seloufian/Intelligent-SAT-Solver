package bso;
import java.util.ArrayList;
import main.ClausesSet;
import main.Solution;

public class DanceList extends ArrayList<Solution>{
	private static final long serialVersionUID = 1L; /* Default serial ID (isn't important here) */


	public Integer bestSolutionQuality(ClausesSet clset) { /* Get index of best solution in quality in current "DanceList" */
		int indexBestQuality = 0; /* Index of the best solution in quality */

		if(this.isEmpty()) /* If the list is empty, the best solution in quality doesn't exist */
			return null;

		for(int i=1; i<this.size(); i++) /* Find the best solution in QUALITY (maximum number of satisfied clauses) in "DanceList" */
			if(this.get(i).satisfiedClauses(clset) > this.get(indexBestQuality).satisfiedClauses(clset))
				indexBestQuality = i; /* Better solution in quality found, update "indexBestQuality" */

		return indexBestQuality;
	}


	public Integer bestSolutionDiversity(ClausesSet clset) { /* Get index of best solution in diversity in current "DanceList" */
		int indexBestDiversity = 0; /* Index of the best solution in diversity */
		int currentSolutionDiv; /* Diversity of the current solution */
		int tempDiversity = 0; /* The diversity of the current solution of external loop [index "i"] compared to the current solution of internal loop [index "j"] */
		int bestDiversity = 0; /* Best diversity (solution of the index "indexBestDiversity") */

		if(this.isEmpty()) /* If the list is empty, the best solution in diversity doesn't exist */
			return null;

		/* To find the best solution in diversity : we must find the MAXIMUM diversity compared to the other solutions (in "DanceList")  */
		for(int i=0; i<this.size(); i++) { /* External loop */
			/* To find the diversity of the current solution (external loop), we must find the MINIMUM diversity compared to the other solutions (internal loop) */
			currentSolutionDiv = this.get(i).getSolutionSize();

			for(int j=0; j<this.size(); j = (j==i) ? j+2 : j+1) { /* Internal loop */
					tempDiversity = this.get(i).difference(this.get(j));
					if(tempDiversity < currentSolutionDiv)
						currentSolutionDiv = tempDiversity;
			}

			if(bestDiversity < currentSolutionDiv) { /* If greater diversity was found */
				indexBestDiversity = i;		bestDiversity = currentSolutionDiv;
			}
		}

		return indexBestDiversity;
	}
}
