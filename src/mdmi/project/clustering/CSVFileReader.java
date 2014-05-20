package mdmi.project.clustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The CSVFileReader class is used to load a csv file
 * @author andershh and jang
 *
 */
public class CSVFileReader {
	/**
	 * The read method reads in a csv file as a two dimensional string array.
	 * Please note it is assumed that ';' is used as separation character.
	 * @param csvFile Path to file
	 * @param useNullForBlank Use empty string for missing values?
	 * @return Two dimensional string array containing the data from the csv file
	 * @throws IOException
	 */
	public static String[][] read(String csvFile, boolean useNullForBlank) throws IOException {
		List<String[]> lines = new ArrayList<>();

		BufferedReader bufRdr = new BufferedReader(new FileReader(new File(csvFile)));
		// read the header
		String line = bufRdr.readLine();
		StringTokenizer tok = new StringTokenizer(line, "\t");
		final int numberOfColumns = tok.countTokens();

		// read each line of text file
		while ((line = bufRdr.readLine()) != null) {
			int col = 0;
			StringTokenizer st = new StringTokenizer(line, "\t");
			String[] lineTokens = new String[numberOfColumns];
			while (st.hasMoreTokens()) {
				// get next token and store it in the array
				lineTokens[col] = st.nextToken();
				if (!useNullForBlank && lineTokens[col] == null)
					lineTokens[col] = "";
				col++;
			}
			// If last column was null
			if (!useNullForBlank) {
				while (col < numberOfColumns) {
					if (lineTokens[col] == null)
						lineTokens[col] = "";
					col++;
				}
			}

			lines.add(lineTokens);
		}
		String[][] ret = new String[lines.size()][];
		bufRdr.close();
		return lines.toArray(ret);
	}

	/**
	 * The read method reads in a csv file as a two dimensional string array.
	 * This method is utilizes the string.split method for splitting each line of the data file.
	 * @param csvFile File to load
	 * @param seperationChar Character used to seperate entries
	 * @param nullValue What to insert in case of missing values
	 * @return Data file content as a 2D string array
	 * @throws IOException
	 */
	public static String[][] readDataFile(String csvFile, String seperationChar, String nullValue) throws IOException {
		List<String[]> lines = new ArrayList<>();

		BufferedReader bufRdr = new BufferedReader(new FileReader(new File(csvFile)));
		// read the header
		String line = bufRdr.readLine();
		
		while ((line = bufRdr.readLine()) != null) {
			String[] arr = line.split(seperationChar); 
			
			for(int i = 0; i < arr.length; i++)
			{
				if(arr[i].equals(""))
				{
					arr[i] = nullValue;
				}				
			}			
			lines.add(arr);
		}
		
		String[][] ret = new String[lines.size()][];
		bufRdr.close();
		return lines.toArray(ret);
	}
}
