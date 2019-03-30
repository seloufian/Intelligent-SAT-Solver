package gui;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import main.ClausesSet;

public class ClausesPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private JTable clausesTable;
	private ClausesSet clset;

	public ClausesPanel() {
		setBounds(10, 11, 321, 260);
		setLayout(new BorderLayout(0, 0));

		clausesTable = new JTable();
		clausesTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		clausesTable.setEnabled(false);
		clausesTable.setRowSelectionAllowed(false);
		add(clausesTable, BorderLayout.CENTER);
		add(new JScrollPane(clausesTable));
	}


	public String loadClausesSet(String cnf_filePath) {
		clset = new ClausesSet(cnf_filePath);

		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("Clause");

		for(int i=0; i<clset.getClauseSize(); i++)
			tableModel.addColumn("Literal "+(i+1));

		String[] tableRow = new String[clset.getClauseSize()+1];

		for(int i=0; i<clset.getNumberClause(); i++) {
			tableRow[0] = String.valueOf(i);

			for(int j=1; j<=clset.getClauseSize(); j++)
				tableRow[j] = String.valueOf(clset.getClause(i).getLiteral(j-1));

			tableModel.addRow(tableRow);
		}

		clausesTable.setModel(tableModel);

		return "SAT instance loaded :  "+clset.getNumberClause()+"  clauses,  "+clset.getNumberVariables()+"  variables,  "+clset.getClauseSize()+"  variables/clause";
	}


	public ClausesSet getClausesSet() { return clset; }
}
