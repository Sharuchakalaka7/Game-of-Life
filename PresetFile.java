/**
 * PresetFile Class
 * Defines, stores, and provides several preset options, as well as any custom designed preset options.
 */
import java.io.*;
import java.util.ArrayList;

public class PresetFile {
	private File presets;
	private int customIteration;	// Custom name for a new preset (ie. Custom0, Custom1, ...)

	/**
	 * Creates a new presets file object that is able to provide various preset options.
	 */
	public PresetFile() {
		presets = new File("presets.txt");
	}

	/**
	 * Returns all the names of the presets, including a default "Clear" preset name.
	 * @return presetNames - an array of all of the preset names.
	 */
	public String[] getPresetNames() {
		boolean reachedEndPreset = false;
		String line;
		String[] presetNames;
		ArrayList<String> names = new ArrayList<String>();
		FileReader in;
		BufferedReader readFile;

		names.add("Clear");
		try {
			in = new FileReader(presets);
			readFile = new BufferedReader(in);

			while ((line = readFile.readLine()) != null) {	// Reads until the entire file has been read

				if (line.equals("#")) {		// "#" is the start of a preset definition
					line = readFile.readLine();		// Name of the preset is just after the "#"
					names.add(line);
					reachedEndPreset = false;

				} else if (line.equals("?")) {	// "?" is the end of a preset definition
					reachedEndPreset = true;

				} else if (reachedEndPreset) {
					customIteration = Integer.parseInt(line);	// After the end of each custom preset (as well as the last default preset), there will be a number that signifies the iteration number of the custom presets. This remains so that the custom presets that are saved are named as follows: "Custom0", "Custom1", "Custom2", ... and so on.
				}
			}

			readFile.close();
			in.close();

		} catch (FileNotFoundException e) {
			System.out.println("File does not exist or could not be found.");
			System.err.println("FileNotFoundException: " + e.getMessage());

		} catch (IOException e) {
			System.out.println("Error reading file.");
			System.err.println("IOException: " + e.getMessage());

		}
		
		presetNames = new String[names.size()];
		for (int i = 0; i < names.size(); i++) {
			presetNames[i] = names.get(i);
		}

		return presetNames;
	}

	/**
	 * Returns the corresponding coordinates of the preset selected.
	 * @return presetCoordinates - an array of coordinates that match the preset.
	 */
	public int[] getPresetCoordinates(String presetName) {
		boolean reachedEndPreset = false;
		int[] presetCoordinates;
		String line;
		ArrayList<Integer> coords = new ArrayList<Integer>();
		FileReader in;
		BufferedReader readFile;

		try {
			in = new FileReader(presets);
			readFile = new BufferedReader(in);
			
			while ((line = readFile.readLine()) != null) {	// Reads until the entire file has been read
				
				if (line.equals("#")) {		// "#" is the start of a new preset definition
					reachedEndPreset = false;
					line = readFile.readLine();		// line now contains the name of the next preset

					if (presetName.equals(line)) {	// Checks if the preset name in line is the same as the one requested
						
						while (!(line = readFile.readLine()).equals("?")) {		// Until the end of the preset definition is reached, represented by "?"

							int cellNum = Integer.parseInt(line);
							coords.add(cellNum);

						}
					}

				}
			}
			
			readFile.close();
			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist or could not be found.");
			System.err.println("FileNotFoundException: " + e.getMessage());

		} catch (IOException e) {
			System.out.println("Error reading file.");
			System.err.println("IOException: " + e.getMessage());

		}

		presetCoordinates = new int[coords.size()];
		for (int coordinate = 0; coordinate < coords.size(); coordinate++) {
			presetCoordinates[coordinate] = coords.get(coordinate);	// Copy the cell coordinate

		}

		return presetCoordinates;
	}

	/**
	 * Creates and saves a new custom preset with the new coordinates given. The new name of the preset is also returned.
	 * @param newPresetCoordinates
	 * @return newPresetName
	 */
	public String createNewPreset(int[] newPresetCoordinates, String newPresetName) {
		FileWriter out;
		BufferedWriter writeFile;

		/* If the new preset name is a duplicate, as should be assessed by caller method, newPresetName should have the value of null, and thus should create a custom name */
		if (newPresetName == null) {
			newPresetName = "Custom" + customIteration;	// Name of the new custom preset
			customIteration++;
		}

		/* Write in file */
		try {
			out = new FileWriter(presets, true);
			writeFile = new BufferedWriter(out);

			writeFile.newLine();
			writeFile.write("#");	// Start of a new preset definition
			writeFile.newLine();
			writeFile.write(newPresetName);	// Write the name of the new preset
			writeFile.newLine();
			for (int i = 0; i < newPresetCoordinates.length; i++) {

				String npcString = Integer.toString(newPresetCoordinates[i]);
				writeFile.write(npcString);	// Write each coordinate in.
				writeFile.newLine();

			}
			writeFile.write("?");	// End of the preset definition
			writeFile.newLine();
			writeFile.write(Integer.toString(customIteration));	// Reference to the iteration number of the next custom preset name

			writeFile.close();
			out.close();

		} catch (FileNotFoundException e) {
			System.out.println("File does not exist or could not be found.");
			System.err.println("FileNotFoundException: " + e.getMessage());

		} catch (IOException e) {
			System.out.println("Error writing file.");
			System.err.println("IOException: " + e.getMessage());

		}

		return newPresetName;
	}
}
