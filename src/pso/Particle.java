package pso;
import java.util.ArrayList;
import java.util.Random;
import main.ClausesSet;
import main.Solution;

public class Particle {
	private Solution solution, pBest;
	private int velocity;


	public Particle(int solutionSize) {
		solution = new Solution(solutionSize);
		solution.randomSolution();
		this.pBest = solution;

		Random random = new Random();
		velocity = random.nextInt(solutionSize);
	}


	public Solution getSolution() { return solution; }
	public Solution getpBest() { return pBest; }
	public int getVelocity() { return velocity; }


	public void updateVelocity(Solution gBest, int inWeight, int const1, int const2) {
		double internalMove = inWeight * velocity;
		double cognitiveMove = const1 * Math.random() * pBest.difference(this.solution);
		double socialMove = const2 * Math.random() * gBest.difference(this.solution);

		/* Use "modulo" so that "velocity" will not increment indefinitely */
		velocity = ((int) (internalMove + cognitiveMove + socialMove))%this.solution.getSolutionSize();
	}


	public void updatePosition() {
		ArrayList<Integer> avaibleLiterals = new ArrayList<Integer>(); /* List of literals that can be reversed */
		Random random = new Random();

		for(int i=0; i<this.solution.getSolutionSize(); i++) /* Initialize the list (all literals can be reversed) */
			avaibleLiterals.add(i);

		for(int i=0; i<velocity; i++) /* Reverse literals (chosen randomly) and delete them from "avaibleLiterals" (to not choose them again) */
			this.solution.invertLiteral(avaibleLiterals.remove(random.nextInt(avaibleLiterals.size())));
	}


	public void updatePBest(ClausesSet clset) {
		if(solution.satisfiedClauses(clset) > pBest.satisfiedClauses(clset))
			pBest = solution;
	}


	@Override
	public String toString() {
		return "Particle [solution=" + solution + ", pBest=" + pBest + ", velocity=" + velocity + "]";
	}
}
