/**
 * Main Class
 * Runs the LifeGUI 2 class.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main implements ActionListener {
	/* Actual simulation logic class, as well as the day counter variable */
	Life simulation;
	int days;
	Timer autoSimulate;
	/* Basic GUI Components */
	JFrame frame;
	JPanel contentPane;
	JPanel utilitiesPane;
	/* 2D-array of Cells - Specialized JButton Components representing the bacteria cells in a simulated world */
	JPanel worldPane;
	Cell[][] world;
	/* Set action GUI components */
	JPanel actionPane;
	JButton next;
	JButton auto;
	JLabel dayCount;
	/* Set setting GUI components */
	JPanel settingsPane;
	JComboBox presets;
	JButton save;
	JLabel stat;

	public Main() {
		/* Initialize the simulation class */
		simulation = new Life();
		days = 0;

		/* ---------------- BASIC GUI COMPONENTS ---------------- */
		/* Initialize the frame */
		frame = new JFrame("LifeGUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Initialize content pane */
		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		contentPane.setBorder(BorderFactory.createEmptyBorder(3, 74, 3, 74));
		contentPane.setBackground(Color.black);

		/* Initialize utilities pane */
		utilitiesPane = new JPanel();
		utilitiesPane.setBackground(Color.black);

		/* ---------------- WORLD GUI COMPONENTS ---------------- */
		/* Initialize world pane */
		worldPane = new JPanel();
		worldPane.setLayout(new GridLayout(0, 20, 5, 3));
		worldPane.setBorder(BorderFactory.createEmptyBorder(3, 2, 0, 2));
		worldPane.setBackground(Color.black);

		/* Initialize all cells in the world */
		world = new Cell[Life.WORLD_EDGE][Life.WORLD_EDGE];
		for (int row = 0; row < world.length; row++) {

			for (int column = 0; column < world[row].length; column++) {
				world[row][column] = new Cell(row, column, this);
				worldPane.add(world[row][column]);

			}
		}

		/* ---------------- ACTION GUI COMPONENTS ---------------- */
		/* Initialize action pane */
		actionPane = new JPanel();
		actionPane.setBackground(Color.black);

		/* Initialize the Next button */
		next = new JButton("Next");
		next.setActionCommand("Next");
		next.addActionListener(new ActionsListener());
		next.setAlignmentX(JButton.CENTER_ALIGNMENT);
		next.setBackground(Color.black);
		next.setForeground(Color.lightGray);
		actionPane.add(next);

		/* Initialize the Go/Stop (Auto) button */
		auto = new JButton("  Go  ");
		auto.setActionCommand("Go");
		auto.addActionListener(new ActionsListener());
		auto.setAlignmentX(JButton.CENTER_ALIGNMENT);
		auto.setBackground(Color.black);
		auto.setForeground(Color.green);
		actionPane.add(auto);

		/* Initialize a day counter label */
		dayCount = new JLabel("  Day 0  ");
		dayCount.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		dayCount.setForeground(Color.white);
		actionPane.add(dayCount);

		/* ---------------- SETTINGS GUI COMPONENTS ---------------- */
		/* Initialize settings pane */
		settingsPane = new JPanel();
		settingsPane.setBackground(Color.black);

		/* Initialize thet preset combo box */
		String[] presetNames = simulation.getPresetNames();
		presets = new JComboBox(presetNames);
		presets.setSelectedItem(0);
		presets.setEditable(true);
		presets.addActionListener(new SettingsListener());
		presets.setBackground(Color.black);
		presets.setForeground(Color.lightGray);
		settingsPane.add(presets);

		/* Initialize the Save button */
		save = new JButton("Save");
		save.setActionCommand("Save");
		save.addActionListener(new SettingsListener());
		save.setAlignmentX(JButton.CENTER_ALIGNMENT);
		save.setBackground(Color.black);
		save.setForeground(Color.white);
		settingsPane.add(save);
		
		/* Initialize a Stat label that displays the status of the save */
		stat = new JLabel("_____________________");
		stat.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		stat.setForeground(Color.black);
		settingsPane.add(stat);

		/* ---------------- FINAL INITAILIZING ---------------- */
		/* Add action and settings panes to utilities pane */
		utilitiesPane.add(settingsPane);
		utilitiesPane.add(actionPane);

		/* Add world and utilities panes to contentPane */
		contentPane.add(worldPane);
		contentPane.add(utilitiesPane);

		/* Complete initializing the frame */
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);

	}

	/**
	 * Reads the Cell Components and stores the boolean values of their status.
	 * @return worldStatus
	 */
	private boolean[][] readUI() {
		boolean[][] worldStatus = new boolean[world.length][world[0].length];

		for (int row = 0; row < world.length; row++) {	// Traverses each row
			
			for (int column = 0; column < world[row].length; column++) {	// Traverses each column
				worldStatus[row][column] = world[row][column].isAlive();	// Sets the boolean element based on the Cell's current life status (true = alive, false = dead)

			}
		}
		return worldStatus;
	}

	/**
	 * Updates the UI of the changed statuses in the Cells.
	 * @param newWorldStatus
	 */
	private void updateUI(boolean[][] newWorldStatus) {

		for (int row = 0; row < world.length; row++) {	// Traverses each row
			
			for (int column = 0; column < world[row].length; column++) {	// Traverses each column
				world[row][column].setAlive(newWorldStatus[row][column]);	// Sets the new status in the Cell

			}
		}
		days++;
		dayCount.setText("Day " + Integer.toString(days));
	}

	/**
	 * Simulates a day of the current world to acquire a new world status. World is automatically updated after simulation.
	 */
	private void simulate() {
		boolean[][] worldStatus = readUI();
		boolean[][] newWorldStatus = simulation.runSimulation(worldStatus);
		updateUI(newWorldStatus);

		/* Also set saving status to default */
		stat.setText("_____________________");
		stat.setForeground(Color.black);
	}

	/**
	 * Handles Cell click action from the world.
	 * @param event
	 */
	public void actionPerformed(ActionEvent event) {
		String eventName = event.getActionCommand();
		int eventNum = Integer.parseInt(eventName);
		int row = eventNum / world.length;
		int column = eventNum % world.length;

		if (world[row][column].isAlive()) {	// If selected Cell is alive
			world[row][column].setAlive(false);	// Toggle to dead status

		} else {	// If selected Cell is dead
			world[row][column].setAlive(true);	// Toggle to alive status

		}

		days = 0;
		dayCount.setText("  Day 0  ");
	}
	
	/**
	 * ActionsListener Class
	 * This listener manages any event that is related to the simulation of the game. Such buttons include Next, Go, and Stop.
	 */
	class ActionsListener implements ActionListener {

		/**
		* Handles button click action.
		* @param event
		*/
		public void actionPerformed(ActionEvent event) {
			String eventName = event.getActionCommand();

			switch (eventName) {

				case "Next":
					simulate();
					break;

				case "Go":	// Go button pressed
					/* Set the Go button to a Stop button */
					auto.setActionCommand("Stop");
					auto.setText("Stop");
					auto.setForeground(Color.red);

					/* Create and start a new timer thread that manages the simulation */
					autoSimulate = new Timer(450, new ActionsListener() {

						public void actionPerformed(ActionEvent evt) {
							simulate();
						}

					});
					autoSimulate.start();
					break;

				case "Stop":	// Stop button pressed
					/* Set the Stop button to a Go button */
					auto.setActionCommand("Go");
					auto.setText("  Go  ");
					auto.setForeground(Color.green);

					/* Stop the timer thread */
					autoSimulate.stop();
					break;

			}
		}
	}

	/**
	 * SettingsListener Class
	 * This listener listens for any event that is related to presets and any other settings in the simulation. This includes the preset selected, as well as the Save button.
	 */
	class SettingsListener implements ActionListener {
		
		/**
		 * Handles preset selection and Save button action.
		 * @param event
		 */
		public void actionPerformed(ActionEvent event) {
			String eventName = event.getActionCommand();

			if (eventName.equals("Save")) {		// Save button has been clicked
				boolean[][] worldStatus = readUI();
				String newPresetName = (String) (presets.getSelectedItem());

				/* Pass the array of coordinates for saving */
				newPresetName = simulation.createNewPreset(worldStatus, newPresetName);
				
				/* Update the settings pane Components with the new custom preset */
				presets.addItem(newPresetName);
				simulation.getPresetNames();	// Don't ask why... LOL
				stat.setText(newPresetName + " created.");
				stat.setForeground(Color.green);

			} else {	// A preset has been selected.
				String presetName = (String) (presets.getSelectedItem());
				boolean[][] presetWorld = simulation.getPresetCoordinates(presetName);
				updateUI(presetWorld);
				days = 0;
				dayCount.setText("  Day 0  ");

			}
		}
	}

	/**
	 * Create and run the GUI.
	 */
	private static void runGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);

		Main lifeGUI = new Main();
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGUI();
			}
		});
	}
}
