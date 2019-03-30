package gui;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import main.ClausesSet;
import main.Solution;

public class ResultPanel extends JPanel{
	private static final long serialVersionUID = 1L;


	public class Result{
		private Solution solution;
		private float satisfiability;
		private long time;


		public Result(ClausesSet clset, Solution solution, long time) {
			this.solution = solution;
			this.satisfiability = (float) (Math.round(100 * (float)solution.satisfiedClauses(clset) / (float)clset.getNumberClause()) / 100.0);
			this.time = time;
		}


		public Solution getSolution() { return solution; }
		public float getSatisfiability() { return satisfiability; }
		public long getTime() { return time; }
	}


	private DefaultCategoryDataset dataset;
	private JFreeChart barChart;
	private ArrayList<Result> resultData;

	public ResultPanel() {
		this.dataset = new DefaultCategoryDataset();
		this.resultData = new ArrayList<Result>();

		barChart = ChartFactory.createBarChart("", "", "Satisfied clauses", this.dataset, PlotOrientation.VERTICAL, false, false, false);

		CategoryPlot plot = barChart.getCategoryPlot();

		((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());

		plot.getDomainAxis().setMaximumCategoryLabelLines(10);

		barChart.setBackgroundPaint(Color.decode("#D6D9DF"));

		CategoryItemRenderer CatRenderer = ((CategoryPlot) barChart.getPlot()).getRenderer();
		CatRenderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		CatRenderer.setDefaultItemLabelsVisible(true);
		ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,TextAnchor.TOP_CENTER);
		CatRenderer.setDefaultPositiveItemLabelPosition(position);

		ChartPanel chartPanel = new ChartPanel( barChart );
		chartPanel.setDomainZoomable(false);
		chartPanel.setRangeZoomable(false);
		chartPanel.setPopupMenu(null);
		chartPanel.setLayout(null);
		chartPanel.setPreferredSize(new Dimension(683, 417));
		add(chartPanel);

		setBackground(Color.decode("#D6D9DF"));
	}
	
	
	public void setUpperBound(int upperBound) {
		barChart.getCategoryPlot().getRangeAxis().setUpperBound(upperBound);
	}


	public void addData(ClausesSet clset, Solution solution, long time, int numAttempt) {
		Result result = new Result(clset, solution, time);

		resultData.add(result);
		this.dataset.setValue(solution.satisfiedClauses(clset), "SAT", "Attempt "+numAttempt+"\n("+Math.round(100*time/1000.0)/100.0+"  sec)");
	}


	public void clearData() {
		resultData.clear();
		dataset.clear();
	}


	public float getSatisfiabilityRate() {
		float sumSatisfiedClausesPerAttempt = 0;

		for(int i=0; i<resultData.size(); i++)
			sumSatisfiedClausesPerAttempt += resultData.get(i).getSatisfiability();

		return (float) (Math.round(1000000 * sumSatisfiedClausesPerAttempt / resultData.size()) / 10000.0);
	}


	public float getAverageSearchTime() {
		long sumSearchTimePerAttempt = 0;

		for(int i=0; i<resultData.size(); i++)
			sumSearchTimePerAttempt += resultData.get(i).getTime();

		return (float) (Math.round(1000 * sumSearchTimePerAttempt / resultData.size()) / 1000.0);
	}


	public void makeTitle(String searchMethodName) {
		if(! resultData.isEmpty())
			this.barChart.setTitle("Satisfied clauses per attempt (using \""+searchMethodName+"\")\nSatisfiability rate :  "+getSatisfiabilityRate()+" %  ("
									+Math.round(1000*getAverageSearchTime()/1000.0)/1000.0+"  sec)");
	}
}
