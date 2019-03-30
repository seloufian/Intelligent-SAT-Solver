package bso;
import main.Solution;

public class Taboo {
	private Solution solution;
	private int chances;


	public Taboo(Solution solution, int chances) { /* First Constructor */
		this.solution = new Solution(solution);
		this.chances = chances;
	}

	public Taboo(Solution solution) { /* Second Constructor */
		this.solution = new Solution(solution);
		this.chances = 0;
	}

	public Taboo(int sizeSolution, int chances) { /* Third Constructor */
		this.solution = new Solution(sizeSolution);
		this.chances = chances;
	}

	public Taboo(Taboo taboo) { /* Fourth Constructor */
		this.solution = new Solution(taboo.getSolution());
		this.chances = new Integer(taboo.getChances());
	}


	public Solution getSolution() { return solution; }
	public int getChances() { return chances; }


	public void setChances(int chances) { this.chances = chances; }


	@Override
	public boolean equals(Object obj) { /* The "equals" method uses that of the class "Solution" */
		return this.getSolution().equals(((Taboo) obj).getSolution());
	}


	@Override
	public String toString() {
		return "Taboo [solution=" + solution + ", chances=" + chances + "]";
	}
}
