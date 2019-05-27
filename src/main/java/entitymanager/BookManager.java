package entitymanager;

import entity.Book;
import gutenberg.SQLDataMapper;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<Book> readBooks() throws IOException, ClassCastException, ClassNotFoundException, SQLException {
        List<Book> books = new ArrayList();
        String[] folderNames = new File(System.getProperty("user.dir") + "/rdf files/").list();
        List<String> cityNames = SQLDataMapper.getCityNames();

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
            int quantity = 0;
            for (int i = 0; i < cityNames.size(); i++) {
                quantity += countStringInFile(cityNames.get(i), System.getProperty("user.dir") + "/books/" + folderName + "/" + folderName + ".txt");
                System.out.println(cityNames.get(i) + " count is: " + quantity);
                quantity = 0;
            }

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
                books.add(new Book(id, title, authorNames));
            } else {
                System.out.println("Nothing were found in the file");
            }
            counter++;
            if (counter == 3) {
                break;
            }
        }
        System.out.println("Total books read: " + books.size());

        return books;
    }

    public void createBooksCSV(List<Book> books) throws IOException {
//        Writer writer = new FileWriter(System.getProperty("user.dir") + "/src/main/java/files/citiesForDocker.csv");
//
//        writer.append("\"id\", \"cityName\", \"latitude\", \"longitude\", \"population\", \"countryCode\", \"continent\"\n");
//        for (int i = 0; i < cities.size(); i++) {
//            writer.append("\"" + cities.get(i).getId() + "\",\"" + cities.get(i).getCityName() + "\"," + cities.get(i).getLatitude() + ","
//                    + cities.get(i).getLongitude() + "," + cities.get(i).getPopulation() + ",\"" + cities.get(i).getCountryCode() + "\",\"" + cities.get(i).getContinent() + "\"\n");
//        }
//        writer.close();
//
//        System.out.println("Created CSV file with books");
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
