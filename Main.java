import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;

import graphs.Range;
import graphs.MapFunctionData;
import graphs.Graph;
import graphs.GraphContainer;

class Main {
  public static void main(String[] args) {
	/// Frame init
	JFrame frame = new JFrame("Graph Demo");
	frame.setSize(800, 800);
	frame.setLocation(10, 10);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	JPanel panel = new JPanel(new BorderLayout());
	panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	frame.add(panel);

	/// Graph init
	Graph graph = createGraph();

	GraphContainer container = new GraphContainer(graph);
	container.addAxis(GraphContainer.Direction.BOTTOM);
	container.addAxis(GraphContainer.Direction.LEFT);
	container.addAxis(GraphContainer.Direction.TOP);
	container.addAxis(GraphContainer.Direction.RIGHT);
	container.addNumbers(GraphContainer.Direction.BOTTOM);
	panel.add(container, BorderLayout.CENTER);

	frame.setVisible(true);
  }

  public static JLabel centeredXLabel(String text)
  {
	  JLabel label = new JLabel(text);
	  label.setAlignmentX(Component.CENTER_ALIGNMENT);
	  return label;
  }

  public static JLabel centeredYLabel(String text)
  {
	  JLabel label = new JLabel(text);
	  label.setAlignmentY(Component.CENTER_ALIGNMENT);
	  return label;
  }

  public static Graph createGraph()
  {
	MapFunctionData<Double, Integer> data = new MapFunctionData<>();
	data.set(0.5, 1);
	data.set(1.5, 2);
	data.set(2.5, 3);
	data.set(3.5, 3);
	
	MapFunctionData<BigDecimal, Integer> data2 = new MapFunctionData<>();
	data2.set(new BigDecimal("0.55"), 1);
	data2.set(new BigDecimal("1.55"), 2);
	data2.set(new BigDecimal("2.55"), 3);
	data2.set(new BigDecimal("3.55"), 3);

	Graph graph = new Graph();
	graph.setPreferredSize(new Dimension(300, 300));

	graph.setLinearPlane(new Range(0, 5), new Range(0, 10));
	graph.setGrid(5, 10);
	graph.getDataList().addData(data);
	graph.getDataList().getVisualsHandler(data).plotData();
	graph.getDataList().getVisualsHandler(data).connectData();
	graph.getDataList().addData(data2);
	graph.getDataList().getVisualsHandler(data2).plotData().setColor(Color.GREEN);
	graph.getDataList().getVisualsHandler(data2).connectData().setColor(Color.GREEN);

	return graph;
  }
}