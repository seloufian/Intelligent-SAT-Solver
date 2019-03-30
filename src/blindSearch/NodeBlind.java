package blindSearch;

public class NodeBlind {
	private int value;
	private int antecedent;
	private int childNumber;


	public NodeBlind(int value, int antecedent, int childNumber) {
		this.value = value;
		this.antecedent = antecedent;
		this.childNumber = childNumber;
	}


	public int getValue() { return value; }
	public int getAntecedent() { return antecedent; }
	public int getChildNumber() { return childNumber; }


	public void setValue(int value) { this.value = value; }
	public void setAntecedent(int antecedent) { this.antecedent = antecedent; }
	public void setChildNumber(int childNumber) { this.childNumber = childNumber; }


	@Override
	public String toString() {
		return "Node [value=" + value + ", antecedent=" + antecedent + ", childNumber=" + childNumber + "]";
	}
}
