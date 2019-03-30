package bso;
import java.util.Random;
import main.ClausesSet;
import main.Solution;

public class Bee extends Thread{
	private Solution solution;
	private ClausesSet clset;
	private int numLocalSearch;

	public Bee(Solution solution, ClausesSet clset, int numLocalSearch) {
		this.solution = solution;
		this.clset = clset;
		this.numLocalSearch = numLocalSearch;
	}


	public Solution getSolution() {
		return solution;
	}


	@Override
	public void run() {
		this.localSearch();
	}


	public Solution localSearch() {
		Solution temporarySol = new Solution (this.getSolution());
		Random random = new Random(); /* "Random" object used instead of "Math.random()" because the latter is a SYNCHRONIZED method */

		for(int i=0; i<this.numLocalSearch; i++) {
			temporarySol.invertLiteral(random.nextInt(this.clset.getNumberVariables()));

			if(temporarySol.satisfiedClauses(this.clset) > this.solution.satisfiedClauses(this.clset))
				this.solution = new Solution(temporarySol);
		}

		return this.solution;
	}


	@Override
	public String toString() {
		return "Bee ("+this.solution+")";
	}
}
