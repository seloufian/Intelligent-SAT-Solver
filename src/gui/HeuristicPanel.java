package gui;
import java.awt.Color;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

public class HeuristicPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private JRadioButton heuristic1Radio;
	private JRadioButton heuristic2Radio;
	private JRadioButton heuristic3Radio;


	public HeuristicPanel() {
		setLayout(null);
		setBorder(new LineBorder(new Color(128, 128, 128), 2, true));

		ButtonGroup heuristicGroup = new ButtonGroup();

		heuristic1Radio = new JRadioButton("Heuristic 1");
		heuristic1Radio.setSelected(true);
		heuristic1Radio.setBounds(86, 31, 121, 24);
		heuristicGroup.add(heuristic1Radio);
		add(heuristic1Radio);

		heuristic2Radio = new JRadioButton("Heuristic 2");
		heuristic2Radio.setBounds(86, 86, 121, 24);
		heuristicGroup.add(heuristic2Radio);
		add(heuristic2Radio);

		heuristic3Radio = new JRadioButton("Heuristic 3");
		heuristic3Radio.setBounds(86, 141, 121, 24);
		heuristicGroup.add(heuristic3Radio);
		add(heuristic3Radio);
	}


	public int getSelectedHeuristicRadio() {
		if(heuristic1Radio.isSelected())
			return 1;
		if(heuristic2Radio.isSelected())
			return 2;
		return 3;
	}
}
