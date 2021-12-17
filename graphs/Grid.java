package graphs;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public class Grid extends GraphComponent
{
	private final Color BACK_COLOR = new Color(0f, 0f, 0f, 0f);
	private final Color LINE_COLOR = Color.LIGHT_GRAY;

	private int numCols;
	private int numRows;
	private double offsetRow;
	private double offsetCol;

	// Move this to util class
	static double floorMod(double a, double n)
	{
		return a < 0 ? (a % n + n) % n : a % n;
	}

	public Grid(Graph graph, int numCols, int numRows, double offsetCol, double offsetRow)
	{
		super(graph);

		this.setNumCols(numCols);
		this.setNumRows(numRows);
		// Truncates rows to range between 0 and 1 if not already 
		this.offsetCol = Grid.floorMod(offsetCol, 1d);
		this.offsetRow = Grid.floorMod(offsetRow, 1d);
	}

	public Grid(Graph graph, int numCols, int numRows)
	{
		this(graph, numCols, numRows, 0d, 0d);
	}

	public void setNumCols(int numCols)
	{
		if (numCols < 0)
		{
			throw new IllegalArgumentException("Cannot have negative number of columns.");
		}
		this.numCols = numCols;
	}

	public void setNumRows(int numRows)
	{
		if (numRows < 0)
		{
			throw new IllegalArgumentException("Cannot have negative number of rows.");
		}
		this.numRows = numRows;
	}

	public int getNumCols()
	{
		return this.numCols;
	}

	public int getNumRows()
	{
		return this.numRows;
	}

	protected double cellWidth()
	{
		return (double) this.getWidth() / this.numCols;
	}

	protected double cellHeight()
	{
		return (double) this.getHeight() / this.numRows;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		// Paint background
		g2.setColor(BACK_COLOR);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());

		// Paint gridlines
		g2.setColor(LINE_COLOR);
		for (int i = 0; i <= this.numCols; i++)
		{
			int xPos = (int) (this.cellWidth() * (i + this.offsetCol));
			g2.drawLine(xPos, 0, xPos, this.getHeight());
		}
		for (int i = 0; i <= this.numRows; i++)
		{
			int yPos = (int) (this.cellHeight() * (i + this.offsetRow));
			g2.drawLine(0, yPos, this.getWidth(), yPos);
		}
	}
}