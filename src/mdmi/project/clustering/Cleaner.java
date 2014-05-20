package mdmi.project.clustering;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts string arrays into objects the rest of the program can understand.
 * @author Per
 */
public class Cleaner {
    /**
     * Cleans all the supplied entries and returns them as more specific objects.
     * @param data The entries to be cleaned.
     * @return The cleaned entries.
     */
	public static List<Entry> clean(String[][] data) {
        List<Entry> cleanedEntries = new ArrayList<>();
        
        for(String[] row : data) {
            Entry e = new Entry();
			
            e.id = Integer.parseInt(stripQuotes(row[0]));
            e.danishName = stripQuotes(row[1]);
            e.englishName = stripQuotes(row[2]);
            e.mainGroup = Integer.parseInt(stripQuotes(row[3]));
            e.subGroup = Integer.parseInt(stripQuotes(row[4]));
            e.energy = Double.parseDouble(stripQuotes(row[5])) * 0.239005736; // kJ -> kcal
            e.protein = Double.parseDouble(stripQuotes(row[6]));
            e.fat = Double.parseDouble(stripQuotes(row[8]));
            e.carbs = Double.parseDouble(stripQuotes(row[12]));
            makeNormalizedValues(e);
            
            cleanedEntries.add(e);
        }
        
        return cleanedEntries;
	}
    
    private static void makeNormalizedValues(Entry e) {
        double carbsEnergy = e.carbs * 4;
        double fatEnergy = e.fat * 9;
        double proteinEnergy = e.protein * 4;
        double energySum = carbsEnergy + fatEnergy + proteinEnergy;
        
        if(energySum == 0.0)
            return;
        
        e.normCarbs = carbsEnergy / energySum;
        e.normFat = fatEnergy / energySum;
        e.normProtein = proteinEnergy / energySum;
    }
    
    private static String stripQuotes(String original) {
        return original.replace("\"", "");
    }
}