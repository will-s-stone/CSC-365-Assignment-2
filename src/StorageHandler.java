import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StorageHandler {
    private HT linkToTable = new HT();

    public HT getLinkToTable() {
        return linkToTable;
    }

    HT getUrlRecordTable() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("src/links/UrlToRecordTable");
        ObjectInputStream ois = new ObjectInputStream(fis);
        HT table = (HT) ois.readObject();
        return table;
    }

    // To be performed after successful storage of objects.
    public List<HT> getListOfTables() throws IOException, ClassNotFoundException {
        List<HT> listOfTables = new ArrayList<>();
        //Replace linkToTable with UrlRecordTable
        linkToTable = getUrlRecordTable();
        for (int i = 0; i < linkToTable.getValues().size(); i++) {
            //Read the file (value in the HT) *close
            FileInputStream fis = new FileInputStream((String) linkToTable.getValues().get(i));
            //Add the HT object we read in
            ObjectInputStream ois = new ObjectInputStream(fis);
            HT table = (HT) ois.readObject();
            listOfTables.add(table);
            fis.close();
            ois.close();

        }
        return listOfTables;
    }

    public void linkTables(int numOfLinks) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/ListOfLinks"));
        for (int i = 0; i < numOfLinks; i++) {
            String url = reader.readLine();
            linkToTable.add(url, ("src/links/webpage" + i));
        }
    }

    public void serializeLinkTables() throws IOException {
        FileOutputStream fileOut = new FileOutputStream("src/links/UrlToRecordTable");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(linkToTable);
        //Link the url to the table
        out.close();
        fileOut.close();
    }

    public List<List<String>> getClusters() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("src/links/Clusters");
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (List<List<String>>) ois.readObject();
    }



    public void storeTables(int numOfLinks) throws Exception {
        ArrayList<List<String>> corpus = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader("src/ListOfLinks"));
        TFIDF tfidf = new TFIDF();
        //Add the webpages to the corpus
        for (int j = 0; j < numOfLinks; j++){
            String line = reader.readLine();
            //System.out.println(line);
            WebPage webPage = new WebPage(line);
            corpus.add(Arrays.asList(webPage.turnToArray(webPage.fetchAndCleanContent())));
        }
        BufferedReader reader2 = new BufferedReader(new FileReader("src/ListOfLinks"));
        for (int i = 0; i < numOfLinks; i++) {
            WebPage webPage = new WebPage(reader2.readLine());
            //System.out.println(webPage.getURL());
            HT tfTable = tfidf.calculateTFTable(webPage.turnToArray((webPage.fetchAndCleanContent())));
            HT idfTable = tfidf.calculateIDFTable(corpus, webPage.turnToArray(webPage.fetchAndCleanContent()));
            HT tfidfTable = tfidf.calculateTFIDFTable(tfTable, idfTable);
            //BufferedWriter writer = new BufferedWriter(new FileWriter("src/links/link" + i));

            FileOutputStream fileOut = new FileOutputStream("src/links/webpage" + i);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tfidfTable);
            //Link the url to the table
            linkToTable.add(webPage.getURL(), ("src/links/webpage" + i));
            out.close();
            fileOut.close();


            //Need to close writer, otherwise it won't write to the file
        }
        reader.close();
    }


    public static void main(String[] args) throws Exception {
        StorageHandler storageHandler = new StorageHandler();
        //storageHandler.storeTables(200);
        //storageHandler.linkTables(200);
        //storageHandler.serializeLinkTables();
        //storageHandler.getListOfTables();
        //storageHandler.getLinkToTable().printAll();

        List<List<String>> clusterStrings = storageHandler.getClusters();

        //KMedoidsClusterer clusterer = new KMedoidsClusterer(5, storageHandler.getListOfTables(), 9);
        //List<List<Integer>> clusterAssignments = clusterer.cluster();

        //List<List<String>> clusterStrings = clusterer.getClusters(storageHandler.getUrlRecordTable(), clusterAssignments.get(clusterAssignments.size()-1));
        //System.out.println(clusterStrings);
        for (int i = 0; i < clusterStrings.size(); i++) {
            System.out.println("Cluster #" + i + " is as follows:" );
            for (int j = 0; j < clusterStrings.get(i).size(); j++) {
                System.out.println(clusterStrings.get(i).get(j));
            }
        }
    }


}
