package mdmi.project.clustering;

/**
 * Represents a single food entry
 * @author Per
 */
public class Entry {
    private static final String DELIMITER = ", ";
    
    public int id;
    public String danishName;
    public String englishName;
    public int mainGroup;
    public int subGroup;
    public double energy;
    public double carbs;
    public double fat;
    public double protein;
    
    public double normCarbs;
    public double normFat;
    public double normProtein;

    @Override
    public String toString() {
        return danishName + DELIMITER + normCarbs + DELIMITER + normFat + DELIMITER + normProtein;
    }
}
