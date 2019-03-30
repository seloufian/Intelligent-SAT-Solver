package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ClausesSet {
	private ArrayList<Clause> clauses = new ArrayList<Clause>();
	private int clauseSize;
	private int numberVariables;


	public ClausesSet(String cnf_filePath) {
		/* Pass the path of a CNF file (which contains the clauses) */
		File file = new File(cnf_filePath);

		/* Create a read buffer (for efficient read) */
		BufferedReader br = null;
		try { br = new BufferedReader(new FileReader(file)); } catch (FileNotFoundException ignore) {}

		String line; /* Store temporarily a line of the CNF file */
		String actualClause[]; /* Get the literals of current clause */
		ArrayList<Integer> literalsClause = new ArrayList<Integer>(); /* Store literals of current clause */

		try {
			while(! (line = br.readLine()).equalsIgnoreCase("%")){ /* Read the CNF file, line per line, until the stop character "%" */

				switch((line = line.trim()).charAt(0)) {
					case 'c': /* This line is a comment, ignore it */
						break;
					case 'p':{ /* This line is a problem-line, get number of variables */
						this.numberVariables = Integer.parseInt(line.replaceAll("[^0-9 ]", "").replaceAll("  ", " ").trim().split(" ")[0]);
						break;
					}
					default:{ /* This line represents a clause */
						actualClause = line.replaceAll(" 0$", "").split(" "); /* Divide literals of actual clause */

						if(this.clauseSize == 0) /* If the size of a clause is not yet defined (first clause of the SAT problem) */
							this.clauseSize = actualClause.length; /* Define the number of literals per clause of this SAT problem */

						for(int i=0; i<clauseSize; i++) /* Extract literals of actual clause */
							literalsClause.add(Integer.parseInt(actualClause[i]));

						this.clauses.add(new Clause(literalsClause)); /* Add new clause into "clauses set" */

						literalsClause.clear(); /* Clear the list of literals of actual clause for the next iteration */
					}
				}
			}
		} catch (NumberFormatException ignore) {} catch (IOException ignore) {}

		try { br.close(); } catch (IOException ignore) {}
	}


	public void addClause(Clause clause) { /* Add new clause into "clauses set" */
		if(clause.getNumbreLiterals() == this.clauseSize)
			this.clauses.add(clause);
	}


	public Clause getClause(int position) { /* Get clause in position "position" */
		return this.clauses.get(position);
	}


	public int getClauseSize(){ /* Get number of literals contained in each clause */
		return this.clauseSize;
	}


	public int getNumberVariables() { /* Get number of variables defined in this SAT problem */
		return numberVariables;
	}


	public boolean changeClause(int position, Clause clause) {
		if((position < 0) || (position >= this.clauses.size()))
			return false;

		this.clauses.set(position, clause);

		return true;
	}


	public int getNumberClause() { /* Get number of clauses contained in this "clauses set" */
		return this.clauses.size();
	}


	@Override
	public String toString() {
		String string = "The list of clauses is : \n";

		for(int i=0; i<this.clauses.size(); i++)
			string += i+". "+this.clauses.get(i)+"\n";

		return string;
	}
}
