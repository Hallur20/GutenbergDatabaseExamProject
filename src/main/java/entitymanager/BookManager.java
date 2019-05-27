package entitymanager;

import com.google.gson.Gson;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import entity.Book;
import entity.JsonBook;
import gutenberg.SQLDataMapper;
import static gutenberg.SQLDataMapper.getCityNames;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.ResourceFactory;
import static org.apache.jena.sparql.engine.http.Service.base;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.DCTerms;

public class BookManager {

    public static HashMap<Integer, JsonBook> jsonList = new HashMap<>();

    public static void printCitiesFromBooks(AbstractSequenceClassifier<CoreLabel> classifier, ArrayList<String> books) throws IOException, SQLException {
        List<String> cities = getCityNames();
        for (String folderName : books) {
            HashMap<String, Integer> hm = new HashMap<>();
            String text = new String(Files.readAllBytes(Paths.get("/home/hallur/NetBeansProjects/GutenbergDatabaseExamProject/books/" + folderName + "/" + folderName + ".txt")), StandardCharsets.UTF_8);

            String[] example = {text};
            for (String str : example) {
                try {
                    str = str.replaceAll("[-+.^:,]", "");
                    // This one is best for dealing with the output as a TSV (tab-separated column) file.
                    // The first column gives entities, the second their classes, and the third the remaining text in a document
                    String res = classifier.classifyToString(str, "tabbedEntities", false);
                    String[] rest = res.split("\n");
                    // System.out.println(res);
                    //int index = 1;
                    for (int i = 1; i < rest.length; i++) {
                        String[] splitLine = rest[i].split("\t");

                        try {
                            if (splitLine[1].equals("LOCATION")) {
                                if (cities.contains(splitLine[0])) {
                                    System.out.println(splitLine[0]);
                                    if (hm.containsKey(splitLine[0])) {
                                        int value = ((hm.get(splitLine[0])) + 1);
                                        hm.put(splitLine[0], value);
                                    } else {
                                        hm.put(splitLine[0], 1);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            //System.out.println("is not type: " + splitLine[0]);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            jsonList.put(Integer.parseInt(folderName), new JsonBook(Integer.parseInt(folderName), hm));
        }
    }

    public static List<Book> readBooks() throws IOException, ClassCastException, ClassNotFoundException, SQLException {
        List<Book> books = new ArrayList();
        String[] folderNames = new File(System.getProperty("user.dir") + "/rdf files/").list();
        //List<String> cityNames = SQLDataMapper.getCityNames();

        int counter = 0;
        for (String folderName : folderNames) {
            int id = Integer.parseInt(folderName);

            // create an empty model
            Model model = ModelFactory.createDefaultModel();

            InputStream rdfFile = FileManager.get().open(System.getProperty("user.dir") + "/rdf files/" + folderName + "/pg" + folderName + ".rdf");
            // InputStream bookFile = FileManager.get().open(System.getProperty("user.dir") + "/books/" + folderName + "/" + folderName + ".txt");
            /*String text = null;
            try {
                text = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")
                        + "/books/" + folderName + "/" + folderName + ".txt")), StandardCharsets.UTF_8);
            } catch (Exception e) {
                System.out.println("book with id: " + folderName + " does not exist, lets proceed");
            }*/
            //int quantity = 0;
            //for (int i = 0; i < cityNames.size(); i++) {
            //    quantity += countStringInFile(cityNames.get(i), System.getProperty("user.dir") + "/books/" + folderName + "/" + folderName + ".txt");
            //    System.out.println(cityNames.get(i) + " count is: " + quantity);
            //    quantity = 0;
            //}

            if (rdfFile == null) {
                throw new IllegalArgumentException("File: " + folderName + " not found");
            }

            // read the RDF/XML file
            model.read(rdfFile, "");

            String pgterms = "http://www.gutenberg.org/2009/pgterms/";
            Property name = ResourceFactory.createProperty(pgterms + "name");

            // select all the resources with a name/title property
            ResIterator authors = model.listResourcesWithProperty(name);
            ResIterator titles = model.listResourcesWithProperty(DCTerms.title);

            if (authors.hasNext() && titles.hasNext()) {
                List<String> authorNames = new ArrayList();
//                System.out.println("The file contains:");
                while (authors.hasNext()) {
                    authorNames.add(authors.nextResource().getRequiredProperty(name).getString());
//                    System.out.println("author: " + authors.nextResource().getRequiredProperty(name).getString());
                }
                String title = titles.nextResource().getRequiredProperty(DCTerms.title).getString();
//                while (titles.hasNext()) {
//                    System.out.println("title: " + titles.nextResource().getRequiredProperty(DCTerms.title).getString());
//                }

                if (jsonList.containsKey(id)) {
                    books.add(new Book(id, title, authorNames, jsonList.get(id).getCityQuantity()));
                    counter++;
                }

            } else {
                System.out.println("Nothing were found in the file");
            }

            if (counter == jsonList.size()) {
                break;
            }
        }
        System.out.println("Total books read: " + books.toString());

        return books;
    }

    public static void createBooksCSV(List<Book> books) throws IOException {
        Writer writer = new FileWriter(System.getProperty("user.dir") + "/src/main/java/files/booksForDocker.csv");
        Gson gson = new Gson();
        writer.append("\"id\", \"authors\", \"title\", \"citiesMentioned\"\n");
        for (Book book : books) {
            String jsonCitiedMentioned = gson.toJson(book.getCities());
            String jsonAuthors = gson.toJson(book.getAuthors());
            writer.append("\"" + book.getId() + "\";\"" + jsonAuthors + "\";\"" + book.getTitle() + "\";\"" + jsonCitiedMentioned + "\"\n");
        }
        writer.close();

        System.out.println("Created CSV file with books");
    }

    public int countStringInFile(String stringToLookFor, String fileName) {
        int count = 0;
        try {
            FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                int startIndex = strLine.indexOf(stringToLookFor);
                while (startIndex != -1) {
                    count++;
                    startIndex = base.indexOf(stringToLookFor,
                            startIndex + stringToLookFor.length());
                }
            }
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return count;
    }
}
