package gui;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;

public class GAPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JSpinner populationSizeSpinner;
	private JSpinner crossoverRateSpinner;
	private JSpinner mutationRateSpinner;
	private JSpinner numIterGaSpinner;

	public GAPanel() {
		setLayout(null);
		setBorder(new LineBorder(new Color(128, 128, 128), 2, true));
	
		JLabel populationSizeLabel = new JLabel("\u2022 Population size :");
		populationSizeLabel.setBounds(15, 19, 109, 25);
		add(populationSizeLabel);
		
		populationSizeSpinner = new JSpinner(new SpinnerNumberModel(15, 1, 100, 1));
		populationSizeSpinner.setBounds(123, 20, 61, 23);
		add(populationSizeSpinner);

		JLabel crossoverRateLabel = new JLabel("\u2022 Crossover rate :");
		crossoverRateLabel.setBounds(15, 63, 109, 25);
		add(crossoverRateLabel);

		crossoverRateSpinner = new JSpinner(new SpinnerNumberModel(70, 1, 100, 1));
		crossoverRateSpinner.setBounds(123, 64, 61, 23);
		add(crossoverRateSpinner);

		JLabel percentageCrossoverLabel = new JLabel("%");
		percentageCrossoverLabel.setBounds(189, 63, 17, 25);
		add(percentageCrossoverLabel);

		JLabel mutationRateLabel = new JLabel("\u2022 Mutation rate :");
		mutationRateLabel.setBounds(15, 107, 99, 25);
		add(mutationRateLabel);
		
		mutationRateSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		mutationRateSpinner.setBounds(113, 108, 61, 23);
		add(mutationRateSpinner);
		
		JLabel percentageMutationLabel = new JLabel("%");
		percentageMutationLabel.setBounds(179, 107, 17, 25);
		add(percentageMutationLabel);
		
		JLabel numIterGaLabel = new JLabel("\u2022 Number of iterations :");
		numIterGaLabel.setBounds(16, 151, 139, 25);
		add(numIterGaLabel);

		numIterGaSpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 1000000, 1));
		numIterGaSpinner.setBounds(154, 152, 84, 23);
		add(numIterGaSpinner);
	}
	
	public int getPopulationSize() { return Integer.parseInt(populationSizeSpinner.getValue().toString()); }
	
	public int getCrossoverRate() { return Integer.parseInt(crossoverRateSpinner.getValue().toString()); }
	
	public int getMutationRate() { return Integer.parseInt(mutationRateSpinner.getValue().toString()); }
	
	public int getnumIterGa() { return Integer.parseInt(numIterGaSpinner.getValue().toString()); }
}
