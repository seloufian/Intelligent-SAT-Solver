package heuristicSearch;

import main.Solution;

public class NodeHeuristic implements Comparable<NodeHeuristic> {
	private Solution solution;
	private int functionG; /* Represents the cost of the node */
	private int functionH; /* Represents the estimated cost of the node to the goal */


	public NodeHeuristic(Solution solution, int functionG, int functionH) {
		this.solution = solution;
		this.functionG = functionG;
		this.functionH = functionH;
	}


	public Solution getSolution() { return solution; }
	public int getFunctionG() { return functionG; }
	public int getFunctionH() { return functionH; }


	@Override
	public int compareTo(NodeHeuristic anotherNode) {
		if((this.functionG + this.functionH) > (anotherNode.functionG + anotherNode.functionH))
			return 1;

		if((this.functionG + this.functionH) < (anotherNode.functionG + anotherNode.functionH))
			return -1;

		return 0; /* Both nodes are equal */
	}
}
