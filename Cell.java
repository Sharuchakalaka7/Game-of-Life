/**
 * Cell Class.
 * Represents a Cell that contains a bacteria in an anonymous world.
 */
import javax.swing.*;
import java.awt.*;

public class Cell extends JButton {
	private int row;
	private int column;
	private boolean alive;

	/**
	 * Initialize a new Cell that is actually a button.
	 * @param row
	 * @param column
	 * @param listener
	 */
	public Cell(int row, int column, Main listener) {
		super(" ");
		this.row = row;
		this.column = column;
		alive = false;
		setupComponent(listener);
	}

	/**
	 * Sets up all of the attributes of this Cell component.
	 * @param listener
	 */
	private void setupComponent(Main listener) {
		int actionComm = 20*row + column;

		setActionCommand(Integer.toString(actionComm));
		addActionListener(listener);
		setAlignmentX(CENTER_ALIGNMENT);
		setBackground(Color.black);
		setMargin(new Insets(1, 8, 1, 8));
	}

	/**
	 * Returns the row number of the Cell.
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns the column number of the Cell.
	 * @return column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Returns whether or not the Cell is alive.
	 * @return alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Sets the Cell's living status to the given value. Also adjusts the properties of the Cell based on the value.
	 * @return alive
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
		
		if (alive) {	// Cell is alive
			setBackground(Color.white);

		} else {		// Dead Cell
			setBackground(Color.black);

		}
	}
}
