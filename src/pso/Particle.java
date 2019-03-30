package pso;
import java.util.Random;
import main.ClausesSet;
import main.Solution;

public class Particle {
	private Solution solution, pBest;
	private int velocity;


	public Particle(int solutionSize, int maxVelocity) {
		solution = new Solution(solutionSize);
		solution.randomSolution();
		this.pBest = solution;

		Random random = new Random();
		velocity = random.nextInt(maxVelocity);
	}


	public Solution getSolution() { return solution; }
	public Solution getpBest() { return pBest; }
	public int getVelocity() { return velocity; }


	public void updateVelocity(Solution gBest, int inWeight, int const1, int const2, int maxVelocity) {
		double internalMove = inWeight * velocity;
		double cognitiveMove = const1 * Math.random() * pBest.difference(this.solution);
		double socialMove = const2 * Math.random() * gBest.difference(this.solution);
		
		velocity = ((int) (internalMove + cognitiveMove + socialMove))%maxVelocity;
	}


	public void updatePosition() {
		Random random = new Random();
		
		for(int i=0; i<velocity; i++)
			this.solution.invertLiteral(random.nextInt(solution.getSolutionSize()));
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
