package mdmi.project.clustering;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cluster of entries.
 * @author Per
 */
public class Cluster {
    private final List<Entry> entries;
    public boolean forcedInitialRun;
    private Entry forcedInitialEntry;

    public Cluster(Entry center, boolean forced) {
        entries = new ArrayList<>();
        
        if(forced) {
            forcedInitialEntry = center;
            forcedInitialRun = true;
        }
        else {
            entries.add(center);
        }
    }

    public void add(Entry e) {
        entries.add(e);
    }

    public void remove(Entry e) {
        entries.remove(e);
    }

    public boolean contains(Entry e) {
        return entries.contains(e);
    }
    
    public int size() {
        return entries.size();
    }

    /**
     * Computes an average entry from the entries in this cluster.
     * @return An entry representing the mean
     */
    public Entry mean() {
        if(forcedInitialRun)
            return forcedInitialEntry;
        
        double carbSum = 0;
        double fatSum = 0;
        double protSum = 0;
        for (Entry e : entries) {
            carbSum += e.normCarbs;
            fatSum += e.normFat;
            protSum += e.normProtein;
        }
        
        Entry e = new Entry();
        e.normCarbs = carbSum / entries.size();
        e.normFat = fatSum / entries.size();
        e.normProtein = protSum / entries.size();
        
        return e;
    }

    @Override
    public String toString() {
        String s = "Cluster:\n";
        for(Entry e : entries)
            s += "\t" + e.toString() + "\n";
        return s;
    }
    
    public String toCSV(int clusterId) {
        String sep = "\t";
        String s = "";
        for(Entry e : entries) {
            s += e.id + sep + e.normCarbs + sep + e.normFat + sep + e.normProtein + sep + clusterId + "\n";
        }
        return s;
    }
}
