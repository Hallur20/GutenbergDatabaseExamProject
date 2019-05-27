package entitymanager;

import entity.Book;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.DCTerms;

public class BookManager {

    public List<Book> readBooks() {
        List<Book> books = new ArrayList();
        String[] folderNames = new File(System.getProperty("user.dir") + "/rdf files/").list();

        for (String folderName : folderNames) {
            int id = Integer.parseInt(folderName);

            // create an empty model
            Model model = ModelFactory.createDefaultModel();

            InputStream in = FileManager.get().open(System.getProperty("user.dir") + "/rdf files/" + folderName + "/pg" + folderName + ".rdf");
            if (in == null) {
                throw new IllegalArgumentException("File: " + folderName + " not found");
            }

            // read the RDF/XML file
            model.read(in, "");

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
}
