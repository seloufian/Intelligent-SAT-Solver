package gui;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;

public class BSOPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private JSpinner flipSpinner;
	private JCheckBox useThreadsCheckBox;
	private JSpinner maxChancesSpinner;
	private JSpinner numBeesSpinner;
	private JSpinner numLocalSearchSpinner;
	private JSpinner numIterBsoSpinner;

	public BSOPanel() {
		setLayout(null);
		setBorder(new LineBorder(new Color(128, 128, 128), 2, true));

		JLabel flipLabel = new JLabel("\u2022 Flip :");
		flipLabel.setBounds(15, 10, 44, 25);
		add(flipLabel);

		flipSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
		flipSpinner.setBounds(57, 12, 45, 23);
		add(flipSpinner);

		useThreadsCheckBox = new JCheckBox("Use threads");
		useThreadsCheckBox.setSelected(true);
		useThreadsCheckBox.setBounds(171, 10, 96, 24);
		add(useThreadsCheckBox);

		JLabel maxChancesLabel = new JLabel("\u2022 Maximum chances :");
		maxChancesLabel.setBounds(15, 46, 132, 25);
		add(maxChancesLabel);

		maxChancesSpinner = new JSpinner(new SpinnerNumberModel(15, 1, 100, 1));
		maxChancesSpinner.setBounds(145, 48, 45, 23);
		add(maxChancesSpinner);

		JLabel numBeesLabel = new JLabel("\u2022 Number of bees :");
		numBeesLabel.setBounds(15, 82, 114, 25);
		add(numBeesLabel);

		numBeesSpinner = new JSpinner(new SpinnerNumberModel(15, 1, 100, 1));
		numBeesSpinner.setBounds(127, 84, 45, 23);
		add(numBeesSpinner);

		JLabel numLocalSearchLabel = new JLabel("\u2022 Number of local searches :");
		numLocalSearchLabel.setBounds(15, 118, 172, 25);
		add(numLocalSearchLabel);

		numLocalSearchSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 100, 1));
		numLocalSearchSpinner.setBounds(185, 120, 45, 23);
		add(numLocalSearchSpinner);

		JLabel numIterBsoLabel = new JLabel("\u2022 Number of iterations :");
		numIterBsoLabel.setBounds(15, 154, 140, 25);
		add(numIterBsoLabel);

		numIterBsoSpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 1000000, 1));
		numIterBsoSpinner.setBounds(153, 156, 79, 23);
		add(numIterBsoSpinner);
	}


	public int getFlip() { return Integer.parseInt(flipSpinner.getValue().toString()); }

	public boolean getUseThreads() { return useThreadsCheckBox.isSelected(); }

	public int getMaxChances() { return Integer.parseInt(maxChancesSpinner.getValue().toString()); }

	public int getNumBees() { return Integer.parseInt(numBeesSpinner.getValue().toString()); }

	public int getNumLocalSearch() { return Integer.parseInt(numLocalSearchSpinner.getValue().toString()); }

	public int getNumIterBso() { return Integer.parseInt(numIterBsoSpinner.getValue().toString()); }
}
