package pso;
import java.util.ArrayList;
import main.ClausesSet;
import main.Solution;

public abstract class PSO {

	public static Solution searchPSO(ClausesSet clset, int numParticles, int const1, int const2, int inWeight, int maxIterations, long execTimeMillis) {
		ArrayList<Particle> particles = new ArrayList<Particle>();
		Solution gBest = new Solution(clset.getNumberVariables());

		long startTime = System.currentTimeMillis(); /* Save the start time of the search */

		for(int i=0; i<numParticles; i++) {
			particles.add(new Particle(clset.getNumberVariables()));
			if(gBest.satisfiedClauses(clset) < particles.get(i).getSolution().satisfiedClauses(clset))
				gBest = particles.get(i).getSolution();
		}

		for(int i=0; i<maxIterations; i++) {
			if((System.currentTimeMillis() - startTime) >= execTimeMillis)
				break; /* If the search time has reached (or exceeded) the allowed run time, finish the search */

			for(Particle particle : particles) {
				particle.updateVelocity(gBest, inWeight, const1, const2);
				particle.updatePosition();
				particle.updatePBest(clset);
			}

			for(Particle particle : particles)
				if(gBest.satisfiedClauses(clset) < particle.getSolution().satisfiedClauses(clset))
					gBest = particle.getSolution();

			if(gBest.isSolution(clset))
				break;
		}

		return gBest;
	}
}
