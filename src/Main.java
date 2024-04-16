import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private HT linkToTable;
    public static void main(String[] args) {
        try {
            //Main main = new Main();
            //main.storeTables(10);
            StorageHandler sh = new StorageHandler();
            sh.storeTables(5);

            ArrayList<HT> tempCorpus = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                FileInputStream fis = new FileInputStream("src/links/webpage" + i);
                ObjectInputStream ois = new ObjectInputStream(fis);
                HT table = (HT) ois.readObject();
                tempCorpus.add(table);
            }

            TFIDF tfidf = new TFIDF();

            //System.out.println(tempCorpus.get(tfidf.getDoc(tempCorpus, "Blue")));
            System.out.println(sh.getLinkToTable().getKeySet());

            //After storing info, this is what it would look like to read the info from the persistent stores
            //Likewise with the reader
            //reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void storeTables(int num) throws Exception {
        ArrayList<List<String>> corpus = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader("src/ListOfLinks"));
        TFIDF tfidf = new TFIDF();
        //Add the webpages to the corpus
        for (int j = 0; j < num; j++){
            String line = reader.readLine();
            System.out.println(line);
            WebPage webPage = new WebPage(line);
            corpus.add(Arrays.asList(webPage.turnToArray(webPage.fetchAndCleanContent())));
        }
        BufferedReader reader2 = new BufferedReader(new FileReader("src/ListOfLinks"));
        for (int i = 0; i < num; i++) {
            WebPage webPage = new WebPage(reader2.readLine());
            System.out.println(webPage.getURL());
            HT tfTable = tfidf.calculateTFTable(webPage.turnToArray((webPage.fetchAndCleanContent())));
            HT idfTable = tfidf.calculateIDFTable(corpus, webPage.turnToArray(webPage.fetchAndCleanContent()));
            HT tfidfTable = tfidf.calculateTFIDFTable(tfTable, idfTable);
            //BufferedWriter writer = new BufferedWriter(new FileWriter("src/links/link" + i));

            FileOutputStream fileOut = new FileOutputStream("src/links/webpage" + i);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tfidfTable);
            out.close();
            fileOut.close();

            //Need to close writer, otherwise it won't write to the file
        }
        reader.close();
    }
}
