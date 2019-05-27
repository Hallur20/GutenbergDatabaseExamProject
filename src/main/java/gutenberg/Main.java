package gutenberg;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import entity.Book;
import entitymanager.BookManager;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import threads.Thread1;
import threads.Thread2;
import threads.Thread3;
import threads.Thread4;
import threads.Thread5;
import threads.Thread6;
import threads.Thread7;
import threads.Thread8;

public class Main {

    public static void main(String[] args) throws IOException, SQLException, ClassCastException, ClassNotFoundException, InterruptedException {
        //SQLDataMapper.createSchema();

        //CityManager cityManager = new CityManager();
        //List<City> cities = cityManager.readCities();
        //cityManager.createCitiesCSV(cities);
        ;
        String[] folderNames = new File("/home/hallur/NetBeansProjects/GutenbergDatabaseExamProject/books/").list();
        String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
        ArrayList<String> firstPart = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            firstPart.add(folderNames[i]);
        }
        ArrayList<String> secondPart = new ArrayList<>();
        for (int i = 3; i < 6; i++) {
            secondPart.add(folderNames[i]);
        }
        ArrayList<String> thirdPart = new ArrayList<>();
        for (int i = 6; i < 9; i++) {
            thirdPart.add(folderNames[i]);
        }
        ArrayList<String> fourthPart = new ArrayList<>();
        for (int i = 9; i < 12; i++) {
            fourthPart.add(folderNames[i]);
        }
        ArrayList<String> fifthPart = new ArrayList<>();
        for (int i = 12; i < 15; i++) {
            fifthPart.add(folderNames[i]);
        }
        ArrayList<String> sixthPart = new ArrayList<>();
        for (int i = 15; i < 18; i++) {
            sixthPart.add(folderNames[i]);
        }
        ArrayList<String> seventhPart = new ArrayList<>();
        for (int i = 18; i < 21; i++) {
            seventhPart.add(folderNames[i]);
        }
        ArrayList<String> eightPart = new ArrayList<>();
        for (int i = 21; i < 24; i++) {
            eightPart.add(folderNames[i]);
        }
        Thread1 t1 = new Thread1(firstPart, classifier);
        Thread2 t2 = new Thread2(secondPart, classifier);
        Thread3 t3 = new Thread3(thirdPart, classifier);
        Thread4 t4 = new Thread4(fourthPart, classifier);
        Thread5 t5 = new Thread5(fifthPart, classifier);
        Thread6 t6 = new Thread6(sixthPart, classifier);
        Thread7 t7 = new Thread7(seventhPart, classifier);
        Thread8 t8 = new Thread8(eightPart, classifier);
        Thread[] threads = new Thread[]{t1,t2,t3,t4,t5,t6,t7,t8};
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 8; i++) {
            es.execute(threads[i]);
        }
        es.shutdown();
        boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);
        //System.out.println("size? : " + BookManager.jsonList.get(1).getCityQuantity());
        
        //BookManager bookManager = new BookManager();
        List<Book> books = BookManager.readBooks();
        BookManager.createBooksCSV(books);
       // System.out.println(books.get(13).getCities().toString());
// all tasks have finished or the time has been reached.

        CSVHelper csvHelper = new CSVHelper();
        csvHelper.executeMySqlCommands("some-mysql");
        csvHelper.setCorrectSecurefilePath();
        
//        csvHelper.executeMongoCommands("dbms");
    }
}
