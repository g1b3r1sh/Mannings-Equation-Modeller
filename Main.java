import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import graphs.Range;
import graphs.Grid;
import graphs.MapFunctionData;
import graphs.Graph;

class Main {
  public static void main(String[] args) {
	MapFunctionData<Double, Integer> data = new MapFunctionData<>();
	data.set(0.5, 1);
	data.set(1.5, 2);
	data.set(2.5, 3);
	data.set(3.5, 3);

	JFrame frame = new JFrame("Graph Demo");
	frame.setSize(500, 500);
	frame.setLocation(5, 5);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	JPanel panel = new JPanel(new BorderLayout());
	panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	frame.add(panel);

	Graph graph = new Graph();
	graph.setLinearPlane(new Range(0, 5), new Range(0, 10));
	graph.getGraphComponents().add(new Grid(graph, 5, 10));
	graph.getDataList().add(data);
	graph.showData(data);
	graph.setMinimumSize(new Dimension(200, 200));

	panel.add(graph);
	frame.setVisible(true);
  }
}