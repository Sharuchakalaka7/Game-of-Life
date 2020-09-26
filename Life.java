/**
 * Life Class
 * Defines the main model, or logic of the Life simulation.
 */
import java.util.ArrayList;

public class Life {
	public static final int WORLD_EDGE = 20;
	private boolean[][] world;
	private PresetFile presetFile;

	/**
	 * Initializes a world of Cells with their statuses of being alive or dead.
	 */
	public Life() {
		world = new boolean[WORLD_EDGE][WORLD_EDGE];
		presetFile = new PresetFile();
	}

	/**
	 * Retrieves the names of the presets from the PresetFile class.
	 * @return presetNames
	 */
	public String[] getPresetNames() {
		String[] presetNames = presetFile.getPresetNames();
		return presetNames;
	}

	/**
	 * Sets the world to the selected preset, according to the preset name retrieved. A new world is initialized and returned with the intention of updating the UI.
	 * @param presetName - name of the preset selected.
	 * @return world - a new world generated according to the preset.
	 */
	public boolean[][] getPresetCoordinates(String presetName) {
		/* Initialize an empty world */
		for (int row = 0; row < world.length; row++) {

			for (int column = 0; column < world.length; column++) {
				world[row][column] = false;		// Status: dead
			}

		}

		/* Retrieve preset coordinates if not "Clear". Otherwise return the empty world */
		if (!presetName.equals("Clear")) {
			int[] coordinates = presetFile.getPresetCoordinates(presetName);

			for (int coord : coordinates) {
				int xCoord = coord/WORLD_EDGE;	// x-component of the coordinate
				int yCoord = coord%WORLD_EDGE;	// y-component of the coordinate
				world[xCoord][yCoord] = true;	// Set the status of the preset coordinate to alive

			}
		}

		return world;
	}

	/**
	 * Passes the coordinates array for a new custom preset to be saved. Returns the name of the new custom preset given.
	 * @param worldStatus
	 * @param newPresetName
	 * @return newPresetName
	 */
	public String createNewPreset(boolean[][] worldStatus, String newPresetName) {
		ArrayList<Integer> npCoords = new ArrayList<Integer>();
		int[] newPresetCoordinates;

		/* Retrieve coordinates of live Cells */
		for (int row = 0; row < world.length; row++) {

			for (int column = 0; column < world.length; column++) {
				
				if (worldStatus[row][column]) {	// If particular Cell is alive
					int pn = row*WORLD_EDGE + column;
					npCoords.add(pn);

				}
			}
		}

		/* Convert ArrayList to an int array */
		newPresetCoordinates = new int[npCoords.size()];

		for (int i = 0; i < npCoords.size(); i++) {
			newPresetCoordinates[i] = npCoords.get(i);
		}

		/* Check if the given preset name would be a duplicate */
		for (String existingPresetNames : presetFile.getPresetNames()) {

			if (newPresetName.equals(existingPresetNames)) {
				newPresetName = null;
				break;
			}
		}

		/* Send coordinates for saving, as well as return given custom preset name */
		newPresetName = presetFile.createNewPreset(newPresetCoordinates, newPresetName);
		return newPresetName;
	}

	/**
	 * Simulates another day to determine the new picture of the world of Cells. Returns the results of the simulation as a new world of Cells.
	 * @param world
	 * @return newWorld
	 */
	public boolean[][] runSimulation(boolean[][] world) {
		this.world = world;
		boolean[][] newWorld = new boolean[WORLD_EDGE][WORLD_EDGE];

		for (int row = 0; row < world.length; row++) {	// Traverses each row of the existing world

			for (int column = 0; column < world[row].length; column++) {	// Traverses each column of the existing world
				int neighbors = numNeighbors(row, column);	// Obtains the number of neighbors the Cell on that particular row and column has.

				/* Applies the pseudocode from chapter 9 Exc.14 that decides whether or not the Selected cell is alive */
				/* Adds the dead or alive Cell to the new world */
				if (world[row][column]) {		// alive

					if (neighbors == 2 || neighbors == 3) {
						newWorld[row][column] = true;

					} else {
						newWorld[row][column] = false;

					}

				} else {	// dead
					
					if (neighbors == 3) {
						newWorld[row][column] = true;

					} else {
						newWorld[row][column] = false;

					}

				}
			}
		}
		return newWorld;
	}

	/**
	 * Using dimensional logic, and the arguments passed, the number of neighbors that a certain Cell has is counted based on its location. That number is then returned.
	 * @param row - the row number of the selected Cell
	 * @param column - the column number of the selected Cell
	 * @return neighbors
	 */
	private int numNeighbors(int row, int column) {
		int neighbors = 0;
		int[][] nCoordinates = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};	// Preset constants to be added to selected Cell's row and column number to obtain the corresponding neighbor's coordinates

		/* Check all neighboring Cells' (ones that do exist) statuses */
		for (int[] coordinates : nCoordinates) {	// Traverses each coordinate
			int xCoordinate = coordinates[0] + row;
			int yCoordinate = coordinates[1] + column;

			/* Includes cells only if their coordinates exist within the parameters of the world */
			if (xCoordinate >= 0 && xCoordinate < WORLD_EDGE && yCoordinate >= 0 && yCoordinate < WORLD_EDGE) {
				
				if (world[xCoordinate][yCoordinate]) {
					neighbors++;
				}
			}
		}

		return neighbors;
	}
}
