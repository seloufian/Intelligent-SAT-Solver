package main;
import java.util.ArrayList;

public class Solution {
	private ArrayList<Integer> solution = new ArrayList<Integer>();


	public Solution(int size) { /* Constructor that creates an empty solution */
		for(int i=0; i<size; i++)
			this.solution.add(0);
	}


	public Solution(Solution solution) {
		for(int i=0; i<solution.getSolutionSize(); i++)
			this.solution.add(solution.getLiteral(i));
	}


	public void randomSolution() { /* Create a random solution */
		int literalValue;

		for(int i=0; i<this.getSolutionSize(); i++) {
			literalValue = (int) (Math.random()*10)%2;

			this.solution.set(i, ((i+1) * (literalValue == 0 ? -1 : 1)));
		}
	}


	public int randomLiteral(int literals) { /* Generate a random literal */
		int randomLiteral;

		do {
			randomLiteral = (int) (Math.random()*100)%literals + 1;
		}while(this.getLiteral(randomLiteral-1) != 0);

		return randomLiteral * (((int) (Math.random()*10)%2) == 0 ? 1 : -1);
	}


	public ArrayList<Integer> getSolution() {
		return solution;
	}


	public int getLiteral(int position) { /* Get a literal at position "position" */
		return this.solution.get(position);
	}


	public int getSolutionSize() { /* Get number of literals in the solution */
		return this.solution.size();
	}


	public int getActiveLiterals() {
		int activeLiterals=0;

		for(int literal : this.solution)
			if(literal != 0)
				activeLiterals++;

		return activeLiterals;
	}


	public boolean changeLiteral(int position, int value) { /* Change truth value of literal in position "position" */
		if((position < 0) || (position >= this.solution.size())) /* Error : index out of array's bounds */
			return false;

		this.solution.set(position, value);

		return true;
	}


	public boolean invertLiteral(int position) { /* Invert the truth value of a literal position "position" */
		if((position < 0) || (position >= this.solution.size())) /* Error : index out of array's bounds */
			return false;

		this.solution.set(position, - this.solution.get(position));

		return true;
	}


	public boolean deleteLiteral(int position) { /* Delete literal in position "position" from this solution (set to "0") */
		if((position < 0) || (position >= this.solution.size())) /* Error : index out of array's bounds */
			return false;

		this.solution.set(position, 0);

		return true;
	}


	public Integer difference(Solution solution) { /* Find Hamiltonian Distance: The number of positions with different values between two solutions */
		int numDiff = 0;

		if(this.getSolutionSize() != solution.getSolutionSize())
			return null; /* If the size of the two solutions isn't the same, we cannot compare them */

		 for(int i=0; i<this.getSolutionSize(); i++)
			 if(this.getLiteral(i) != solution.getLiteral(i))
				 numDiff++; /* Count number of different positions */

		 return numDiff;
	}


	public int satisfiedClauses(ClausesSet clausesSet) { /* Count number of satisfied clauses by this solution */
		int count = 0; /* Counter of satisfied clauses */
		int literal; /* Store actual literal */


		for(int i=0; i<clausesSet.getNumberClause(); i++) { /* Browse all clauses of "clauses set" */
			for(int j=0; j<clausesSet.getClauseSize(); j++) { /* Browse all literals of actual clause */
				literal = clausesSet.getClause(i).getLiteral(j);

				if(literal == this.getLiteral(Math.abs(literal)-1)) {
					count++;
					break; /* At least one literal satisfies the clause ==> this clause is satisfied */
				}
			}
		}

		return count;
	}


	public boolean isSolution(ClausesSet clausesSet) { /* Check if this solution satisfies ALL CLAUSES in "clauses set" */
		return clausesSet.getNumberClause() == this.satisfiedClauses(clausesSet);
	}


	@Override
	public boolean equals(Object obj) { /* Test the equality between two solutions */
		Solution otherSol = (Solution) obj;

		if(this.getSolutionSize() != otherSol.getSolutionSize())
			return false; /* If the size of the two solutions is different, then the two solutions are NOT equal */

		for(int i=0; i<this.getSolutionSize(); i++)
			if(this.getLiteral(i) != otherSol.getLiteral(i))
				return false; /* There is at least one different literal */

		return true; /* Same size AND same literal ==> Equal solutions */
	}


	@Override
	public String toString() {
		return "Solution is : "+this.solution;
	}
}
