package gui;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;

public class PSOPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private JSpinner numParticlesSpinner;
	private JSpinner maxVelocitySpinner;
	private JSpinner constant1Spinner;
	private JSpinner constant2Spinner;
	private JSpinner inWeightSpinner;
	private JSpinner numIterPsoSpinner;

	public PSOPanel() {
		setLayout(null);
		setBorder(new LineBorder(new Color(128, 128, 128), 2, true));

		JLabel numParticlesLabel = new JLabel("\u2022 Number of particles :");
		numParticlesLabel.setBounds(15, 10, 136, 25);
		add(numParticlesLabel);

		numParticlesSpinner = new JSpinner(new SpinnerNumberModel(15, 1, 100, 1));
		numParticlesSpinner.setBounds(149, 11, 45, 23);
		add(numParticlesSpinner);

		JLabel maxVelocityLabel = new JLabel("\u2022 Maximum velocity :");
		maxVelocityLabel.setBounds(15, 46, 125, 25);
		add(maxVelocityLabel);

		maxVelocitySpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 100000, 1));
		maxVelocitySpinner.setBounds(138, 48, 68, 23);
		add(maxVelocitySpinner);

		JLabel constant1Label = new JLabel("\u2022 Constant 1 :");
		constant1Label.setBounds(15, 82, 85, 25);
		add(constant1Label);

		constant1Spinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		constant1Spinner.setBounds(98, 84, 45, 23);
		add(constant1Spinner);

		JLabel constant2Label = new JLabel("\u2022 Constant 2 :");
		constant2Label.setBounds(149, 83, 85, 25);
		add(constant2Label);

		constant2Spinner = new JSpinner(new SpinnerNumberModel(2, 1, 100, 1));
		constant2Spinner.setBounds(231, 85, 45, 23);
		add(constant2Spinner);

		JLabel inWeightLabel = new JLabel("\u2022 Inner weight :");
		inWeightLabel.setBounds(15, 118, 94, 25);
		add(inWeightLabel);

		inWeightSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
		inWeightSpinner.setBounds(107, 120, 45, 23);
		add(inWeightSpinner);

		JLabel numIterPsoLabel = new JLabel("\u2022 Number of iterations :");
		numIterPsoLabel.setBounds(15, 154, 140, 25);
		add(numIterPsoLabel);

		numIterPsoSpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 1000000, 1));
		numIterPsoSpinner.setBounds(153, 156, 79, 23);
		add(numIterPsoSpinner);
	}

	public int getNumParticles() { return Integer.parseInt(numParticlesSpinner.getValue().toString()); }

	public int getMaxVelocity() { return Integer.parseInt(maxVelocitySpinner.getValue().toString()); }

	public int getConstant1() { return Integer.parseInt(constant1Spinner.getValue().toString()); }

	public int getConstant2() { return Integer.parseInt(constant2Spinner.getValue().toString()); }

	public int getInWeight() { return Integer.parseInt(inWeightSpinner.getValue().toString()); }

	public int getNumIterPso() { return Integer.parseInt(numIterPsoSpinner.getValue().toString()); }
}
