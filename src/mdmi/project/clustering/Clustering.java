package mdmi.project.clustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Class for doing guided k-means clustering.
 * @author Per
 */
public class Clustering {
    public static void main(String[] args) {
        try {
            String[][] data = CSVFileReader.readDataFile("food.out.csv", "\t", "-");
            List<Entry> entries = Cleaner.clean(data);
            List<Cluster> clusters = kMeans(entries);
            
            String s = "id\tnormCarbs\tnormFat\tnormProtein\tcluster\n";
            int count = 1;
            for(Cluster c : clusters)
                s += c.toCSV(count++);
            System.out.println(s);
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
        }
    }
    
    /**
     * Computes the distribution of entries to clusters using k-means.
     * @param entries
     * @return A list of clusters
     */
    public static List<Cluster> kMeans(List<Entry> entries) {
        double min = 0.0;
        double max = 1.0;
        
        // make initial entries
        Entry e1 = new Entry(); e1.normCarbs = max; e1.normFat = min; e1.normProtein = min;
        Entry e2 = new Entry(); e2.normCarbs = min; e2.normFat = max; e2.normProtein = min;
        Entry e3 = new Entry(); e3.normCarbs = min; e3.normFat = min; e3.normProtein = max;
        List<Entry> initial = new ArrayList<>();
        initial.add(e3);
        initial.add(e1);
        initial.add(e2);
        
        // find initial k entries
        /*Random r = new Random();
        List<Entry> kInitial = new ArrayList<>();
        while(kInitial.size() < 3) {
            int i = r.nextInt(entries.size());
            Entry e = entries.get(i);
            
            if(kInitial.contains(e))
                continue; // don't add duplicates
            
            kInitial.add(e);
        }*/
        
        // make initial clusters from initial entries
        List<Cluster> clusters = new ArrayList<>();
        HashMap<Entry, Cluster> entryCluster = new HashMap<>();
        for(Entry e : initial) {
            Cluster c = new Cluster(e, true);
            clusters.add(c);
            //entryCluster.put(e, c);
        }
        
        // add all entries to the nearest cluster
        for(Entry e : entries) {
            /*if(kInitial.contains(e)) {
                continue;
            }*/
            
            Cluster bestCluster = clusters.get(0);
            for(Cluster c : clusters) {
                if(distance(e, c.mean()) <= distance(e, bestCluster.mean()))
                    bestCluster = c;
            }
            
            bestCluster.add(e);
            entryCluster.put(e, bestCluster);
        }
        
        // disregard the initial centroids
        for(Cluster c : clusters)
            c.forcedInitialRun = false;
        
        // do iterative clustering based on similarity
        boolean change = true;
        while(change) {
            change = false;
            for(Entry e : entries) {
                Cluster bestCluster = entryCluster.get(e);
                for(Cluster c : clusters) {
                    if(distance(e, c.mean()) < distance(e, bestCluster.mean())) {
                        bestCluster = c;
                        change = true; // a change in the clusters has occurred, while loop should run for another iteration
                    }
                }
                
                // re-assign entry to a new, better cluster
                entryCluster.get(e).remove(e);
                bestCluster.add(e);
                entryCluster.put(e, bestCluster);
            }
        }
        
        return clusters;
    }
    
    /**
     * Compute the euclidian distance between two entries.
     * @param first
     * @param second
     * @return The distance
     */
    private static double distance(Entry first, Entry second) {
        double carbDiff = Math.pow(Math.abs(first.normCarbs - second.normCarbs), 2);
        double fatDiff = Math.pow(Math.abs(first.normFat - second.normFat), 2);
        double protDiff = Math.pow(Math.abs(first.normProtein - second.normProtein), 2);
        
        return Math.sqrt(carbDiff + fatDiff + protDiff);
    }
}
