package gui;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import blindSearch.BlindSearch;
import bso.BSO;
import main.ClausesSet;
import pso.PSO;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	public Frame(String title) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 682, 476);
		setResizable(false);

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception ignored){}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 676, 448);
		contentPane.add(tabbedPane);

		JPanel dataPanel = new JPanel();
		tabbedPane.addTab("Data definition and search method", null, dataPanel, null);
		dataPanel.setLayout(null);

		ClausesPanel clausesPanel = new ClausesPanel();
		dataPanel.add(clausesPanel);

		JButton importFileButton = new JButton("Import CNF file");
		importFileButton.setBounds(85, 282, 151, 38);
		dataPanel.add(importFileButton);

		JButton startResButton = new JButton("Start resolution");
		startResButton.setEnabled(false);
		startResButton.setBounds(85, 344, 151, 38);
		dataPanel.add(startResButton);

		JLabel selectMethodLabel = new JLabel("Select a SAT resolution method :");
		selectMethodLabel.setBounds(395, 11, 201, 16);
		dataPanel.add(selectMethodLabel);

		JComboBox<String> resMethodComboBox = new JComboBox<String>();
		resMethodComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Depth-First Search (DFS)", "Breadth-First Search (BFS)", "Heuristic Search (A*)", "Bee Swarm Optimization (BSO)", "Particle Swarm Optimization (PSO)"}));
		resMethodComboBox.setBounds(370, 38, 237, 25);
		dataPanel.add(resMethodComboBox);

		JPanel optionsPanel = new JPanel();
		optionsPanel.setBounds(348, 74, 281, 196);
		dataPanel.add(optionsPanel);
		optionsPanel.setLayout(new CardLayout(0, 0));

		JPanel defaultOption = new JPanel();
		HeuristicPanel heuristicOption = new HeuristicPanel();
		BSOPanel bsoOption = new BSOPanel();
		PSOPanel psoOption = new PSOPanel();

		optionsPanel.add(defaultOption, "default");
		optionsPanel.add(heuristicOption, "heuristic");
		optionsPanel.add(bsoOption, "bso");
		optionsPanel.add(psoOption, "pso");

		JLabel numAttemptsLabel = new JLabel("Number of attempts :");
		numAttemptsLabel.setBounds(348, 295, 130, 25);
		dataPanel.add(numAttemptsLabel);

		JLabel timeAttemptLabel = new JLabel("Time per attempt (seconds) :");
		timeAttemptLabel.setBounds(348, 344, 170, 25);
		dataPanel.add(timeAttemptLabel);

		JSpinner numAttemptsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
		numAttemptsSpinner.setBounds(473, 297, 45, 23);
		dataPanel.add(numAttemptsSpinner);

		JSpinner timeAttemptSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 3600, 1));
		timeAttemptSpinner.setBounds(517, 346, 56, 23);
		dataPanel.add(timeAttemptSpinner);

		JLabel informationLabel = new JLabel("Welcome to the SAT solver. Add a CNF Benchmark file");
		informationLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		informationLabel.setForeground(Color.decode("#404040"));
		informationLabel.setBounds(4, 395, 667, 25);
		dataPanel.add(informationLabel);

		ResultPanel resultPanel = new ResultPanel();
		tabbedPane.addTab("Results and statistics", null, resultPanel, null);
		tabbedPane.setEnabledAt(1, false);

		resMethodComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cardLayout = (CardLayout) optionsPanel.getLayout();

				switch(resMethodComboBox.getSelectedIndex()) {
					case 2:
						cardLayout.show(optionsPanel, "heuristic");
						break;
					case 3:
						cardLayout.show(optionsPanel, "bso");
						break;
					case 4:
						cardLayout.show(optionsPanel, "pso");
						break;
					default:
						cardLayout.show(optionsPanel, "default");
				}
			}
		});

		importFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Conjunctive Normal Form (.cnf)", "cnf"));
				fileChooser.showOpenDialog(null);

				try {
					informationLabel.setText(clausesPanel.loadClausesSet(fileChooser.getSelectedFile().getAbsolutePath()));
					startResButton.setEnabled(true);
				} catch (NullPointerException ignore) {}
			}
		});

		startResButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				resultPanel.clearData();

				long startResolution;
				long timeAttempt = Long.parseLong(timeAttemptSpinner.getValue().toString())*1000;
				ClausesSet clset = clausesPanel.getClausesSet();
				String methodName = "";
				
				resultPanel.setUpperBound(clset.getNumberClause());

				switch(resMethodComboBox.getSelectedIndex()) {
					case 0:
						informationLabel.setText("SAT instance resolved using \"Depth-First Search (DFS)\"");
						methodName = "DFS";

						for(int i=0; i< Integer.parseInt(numAttemptsSpinner.getValue().toString()); i++) {
							startResolution = System.currentTimeMillis();
							resultPanel.addData(clset, BlindSearch.DepthFirstSearch(clset, timeAttempt),
												System.currentTimeMillis() - startResolution > timeAttempt ? timeAttempt : System.currentTimeMillis() - startResolution, i+1);
						}

						break;

					case 1:
						informationLabel.setText("SAT instance resolved using \"Breadth-First Search (BFS)\"");
						methodName = "BFS";

						for(int i=0; i< Integer.parseInt(numAttemptsSpinner.getValue().toString()); i++) {
							startResolution = System.currentTimeMillis();
							resultPanel.addData(clset, BlindSearch.BreadthFirstSearch(clset, timeAttempt),
												System.currentTimeMillis() - startResolution > timeAttempt ? timeAttempt : System.currentTimeMillis() - startResolution, i+1);
						}

						break;

					case 2:
						informationLabel.setText("SAT instance resolved using \"Heuristic "+heuristicOption.getSelectedHeuristicRadio()+"\"");
						break;

					case 3:
						informationLabel.setText("SAT instance resolved using \"Bee Swarm Optimization (BSO)\"");
						methodName = "BSO";

						for(int i=0; i< Integer.parseInt(numAttemptsSpinner.getValue().toString()); i++) {
							startResolution = System.currentTimeMillis();
							resultPanel.addData(clset, BSO.searchBSO(clset,
												bsoOption.getFlip(), bsoOption.getNumBees(), bsoOption.getMaxChances(),
												bsoOption.getNumLocalSearch(), bsoOption.getNumIterBso(), bsoOption.getUseThreads(), timeAttempt),
												System.currentTimeMillis() - startResolution > timeAttempt ? timeAttempt : System.currentTimeMillis() - startResolution, i+1);
						}

						break;

					case 4:
						informationLabel.setText("SAT instance resolved using \"Particle Swarm Optimization (PSO)\"");
						methodName = "PSO";

						for(int i=0; i< Integer.parseInt(numAttemptsSpinner.getValue().toString()); i++) {
							startResolution = System.currentTimeMillis();
							resultPanel.addData(clset, PSO.searchPSO(clset, psoOption.getNumParticles(), psoOption.getMaxVelocity(), psoOption.getConstant1(),
												psoOption.getConstant2(), psoOption.getInWeight(), psoOption.getNumIterPso(), timeAttempt),
												System.currentTimeMillis() - startResolution > timeAttempt ? timeAttempt : System.currentTimeMillis() - startResolution, i+1);
						}
				}

				resultPanel.makeTitle(methodName);

				tabbedPane.setEnabledAt(1, true);
			}
		});
	}
}
