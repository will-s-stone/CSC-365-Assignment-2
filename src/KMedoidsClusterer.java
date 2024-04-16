import java.util.*;


public class KMedoidsClusterer {
    private int k; // Number of clusters
    private List<HT> documents; // List of document TF-IDF vectors
    private List<Integer> medoids; // Indices of current medoids
    int maxIterations;

    public KMedoidsClusterer(int k, List<HT> documents, int maxIterations) {
        this.k = k;
        this.documents = documents;
        this.medoids = initializeMedoids();
        this.maxIterations = maxIterations;
    }

    private List<Integer> initializeMedoids() {
        // Initialize medoids randomly or using some heuristic
        // For simplicity, let's assume random initialization here
        List<Integer> medoids = new ArrayList<>();
        for (int i = 0; i < k; i++) { medoids.add(i);}
        return medoids;
    }

    public List<List<Integer>> cluster() {
        List<List<Integer>> clusters = new ArrayList<>();

        // Iterate for a fixed number of iterations or until convergence
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // Assign each document to the nearest medoid
            List<Integer> assignments = assignDocumentsToMedoids();
            clusters.add(assignments);
            // Update medoids based on assigned documents
            updateMedoids(assignments);
            System.out.println(iteration + "_________________-");
        }
        return clusters;
    }

    private List<Integer> assignDocumentsToMedoids() {
        List<Integer> assignments = new ArrayList<>();
        for (HT document : documents) {
            double maxSimilarity = Double.NEGATIVE_INFINITY;
            int assignedMedoid = -1;
            for (int i = 0; i < k; i++) {
                HT medoid = documents.get(medoids.get(i));
                double similarity = TFIDF.cosineSimilarity(document, medoid);
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                    assignedMedoid = i;
                }
            }
            assignments.add(assignedMedoid);
        }
        return assignments;
    }

    private void updateMedoids(List<Integer> assignments) {
        // Update medoids based on the assigned documents
        for (int i = 0; i < k; i++) {
            int medoidIndex = findMedoid(assignments, i);
            medoids.set(i, medoidIndex);
        }
    }

    private int findMedoid(List<Integer> assignments, int clusterIndex) {
        // Find the document index with the lowest average distance to other assigned documents in the cluster
        // You may use different distance measures depending on your requirements
        // For simplicity, we use cosine similarity here

        double minAvgDistance = Double.POSITIVE_INFINITY;
        int medoidIndex = -1;

        for (int i = 0; i < documents.size(); i++) {
            if (assignments.get(i) == clusterIndex) {
                double avgDistance = calculateAverageDistance(i, assignments);
                if (avgDistance < minAvgDistance) {
                    minAvgDistance = avgDistance;
                    medoidIndex = i;
                }
            }
        }

        return medoidIndex;
    }

    private double calculateAverageDistance(int medoidIndex, List<Integer> assignments) {
        // Calculate the average distance of the given medoid to other assigned documents in the cluster
        double totalDistance = 0.0;
        int count = 0;

        for (int i = 0; i < documents.size(); i++) {
            if (assignments.get(i) == assignments.get(medoidIndex)) {
                totalDistance += 1 - TFIDF.cosineSimilarity(documents.get(i), documents.get(medoidIndex));
                count++;
            }
        }

        return count > 0 ? totalDistance / count : 0.0;
    }

    public List<List<String>> getClusters(HT recordTable, List<Integer> finalAssignment){
        List<List<String>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            ArrayList<String> cluster = new ArrayList<>();
            for (int j = 0; j < finalAssignment.size(); j++) {
                if(finalAssignment.get(j) == i){
                    //Add that URL to our cluster
                    cluster.add((String) recordTable.keySet.get(j));
                }
            }
            clusters.add(cluster);
        }
        return clusters;
    }

    public static void main(String[] args) throws Exception {


        WebPage page1 = new WebPage("https://en.wikipedia.org/wiki/Bluefish");
        WebPage page2 = new WebPage("https://en.wikipedia.org/wiki/Scup");
        WebPage page3 = new WebPage("https://en.wikipedia.org/wiki/Black_sea_bass");
        WebPage page4 = new WebPage("https://en.wikipedia.org/wiki/Ocean_sunfish");
        WebPage page5 = new WebPage("https://en.wikipedia.org/wiki/Striped_bass");
        WebPage page6 = new WebPage("https://en.wikipedia.org/wiki/Blue_mackerel");
        WebPage page7 = new WebPage("https://en.wikipedia.org/wiki/Tautog");
        WebPage page8 = new WebPage("https://en.wikipedia.org/wiki/Red_drum");
        WebPage page9 = new WebPage("https://en.wikipedia.org/wiki/Eastern_oyster");
        WebPage page10 = new WebPage("https://en.wikipedia.org/wiki/Hard_clam");
        ArrayList<WebPage> webpages = new ArrayList<>();
        webpages.add(page1);webpages.add(page2);webpages.add(page3);webpages.add(page4);webpages.add(page5);webpages.add(page6);webpages.add(page7);webpages.add(page8);webpages.add(page9);webpages.add(page10);


        String[] inputArray1 = page1.turnToArray(page1.fetchAndCleanContent());
        String[] inputArray2 = page2.turnToArray(page2.fetchAndCleanContent());
        String[] inputArray3 = page3.turnToArray(page3.fetchAndCleanContent());
        String[] inputArray4 = page4.turnToArray(page4.fetchAndCleanContent());
        String[] inputArray5 = page5.turnToArray(page5.fetchAndCleanContent());
        String[] inputArray6 = page6.turnToArray(page6.fetchAndCleanContent());
        String[] inputArray7 = page7.turnToArray(page7.fetchAndCleanContent());
        String[] inputArray8 = page8.turnToArray(page8.fetchAndCleanContent());
        String[] inputArray9 = page9.turnToArray(page9.fetchAndCleanContent());
        String[] inputArray10 = page10.turnToArray(page10.fetchAndCleanContent());


        TFIDF tfidf = new TFIDF();
        ArrayList<List<String>> corpus = new ArrayList<>();
        corpus.add(Arrays.asList(inputArray1));
        corpus.add(Arrays.asList(inputArray2));
        corpus.add(Arrays.asList(inputArray3));
        corpus.add(Arrays.asList(inputArray4));
        corpus.add(Arrays.asList(inputArray5));
        corpus.add(Arrays.asList(inputArray6));
        corpus.add(Arrays.asList(inputArray7));
        corpus.add(Arrays.asList(inputArray8));
        corpus.add(Arrays.asList(inputArray9));
        corpus.add(Arrays.asList(inputArray10));

        HT tfTable1 = tfidf.calculateTFTable(inputArray1);
        HT tfTable2 = tfidf.calculateTFTable(inputArray2);
        HT tfTable3 = tfidf.calculateTFTable(inputArray3);
        HT tfTable4 = tfidf.calculateTFTable(inputArray4);
        HT tfTable5 = tfidf.calculateTFTable(inputArray5);
        HT tfTable6 = tfidf.calculateTFTable(inputArray6);
        HT tfTable7 = tfidf.calculateTFTable(inputArray7);
        HT tfTable8 = tfidf.calculateTFTable(inputArray8);
        HT tfTable9 = tfidf.calculateTFTable(inputArray9);
        HT tfTable10 = tfidf.calculateTFTable(inputArray10);



        HT idfTable1 = tfidf.calculateIDFTable(corpus, inputArray1);
        HT idfTable2 = tfidf.calculateIDFTable(corpus, inputArray2);
        HT idfTable3 = tfidf.calculateIDFTable(corpus, inputArray3);
        HT idfTable4 = tfidf.calculateIDFTable(corpus, inputArray4);
        HT idfTable5 = tfidf.calculateIDFTable(corpus, inputArray5);
        HT idfTable6 = tfidf.calculateIDFTable(corpus, inputArray6);
        HT idfTable7 = tfidf.calculateIDFTable(corpus, inputArray7);
        HT idfTable8 = tfidf.calculateIDFTable(corpus, inputArray8);
        HT idfTable9 = tfidf.calculateIDFTable(corpus, inputArray9);
        HT idfTable10 = tfidf.calculateIDFTable(corpus, inputArray10);

        HT tfidfTable1 = tfidf.calculateTFIDFTable(tfTable1, idfTable1);
        HT tfidfTable2 = tfidf.calculateTFIDFTable(tfTable2, idfTable2);
        HT tfidfTable3 = tfidf.calculateTFIDFTable(tfTable3, idfTable3);
        HT tfidfTable4 = tfidf.calculateTFIDFTable(tfTable4, idfTable4);
        HT tfidfTable5 = tfidf.calculateTFIDFTable(tfTable5, idfTable5);
        HT tfidfTable6 = tfidf.calculateTFIDFTable(tfTable6, idfTable6);
        HT tfidfTable7 = tfidf.calculateTFIDFTable(tfTable7, idfTable7);
        HT tfidfTable8 = tfidf.calculateTFIDFTable(tfTable8, idfTable8);
        HT tfidfTable9 = tfidf.calculateTFIDFTable(tfTable9, idfTable9);
        HT tfidfTable10 = tfidf.calculateTFIDFTable(tfTable10, idfTable10);


        List<HT> tfidfTable = new ArrayList<>();
        tfidfTable.add(tfidfTable1);
        tfidfTable.add(tfidfTable2);
        tfidfTable.add(tfidfTable3);
        tfidfTable.add(tfidfTable4);
        tfidfTable.add(tfidfTable5);
        tfidfTable.add(tfidfTable6);
        tfidfTable.add(tfidfTable7);
        tfidfTable.add(tfidfTable8);
        tfidfTable.add(tfidfTable9);
        tfidfTable.add(tfidfTable10);

        KMedoidsClusterer clusterer = new KMedoidsClusterer(2, tfidfTable, 12);
        System.out.println(clusterer.cluster());

    }

    public static int mostRecentMediod(List<String> cluster){return cluster.size()/2;}

}



