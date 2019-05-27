package entitymanager;

import com.opencsv.CSVReader;
import entity.City;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class CityManager {

    public List<City> readCities() throws FileNotFoundException, IOException {
        List<City> cities = new ArrayList();
        String fileName = System.getProperty("user.dir") + "/src/main/java/files/cities15000.txt";
        CSVReader reader = new CSVReader(new FileReader(fileName), '\t');
        String[] nextLine;

        while ((nextLine = reader.readNext()) != null) {
            cities.add(new City(Integer.parseInt(nextLine[0]), nextLine[1], Double.parseDouble(nextLine[4]), Double.parseDouble(nextLine[5]), Integer.parseInt(nextLine[14]), nextLine[8], nextLine[17]));
        }

        System.out.println("Total cities read: " + cities.size());

        return cities;
    }

    public void createCitiesCSV(List<City> cities) throws IOException {
        Writer writer = new FileWriter(System.getProperty("user.dir") + "/src/main/java/files/citiesForDocker.csv");

        writer.append("\"id\", \"cityName\", \"latitude\", \"longitude\", \"population\", \"countryCode\", \"continent\"\n");
        for (int i = 0; i < cities.size(); i++) {
            writer.append("\"" + cities.get(i).getId() + "\",\"" + cities.get(i).getCityName() + "\"," + cities.get(i).getLatitude() + ","
                    + cities.get(i).getLongitude() + "," + cities.get(i).getPopulation() + ",\"" + cities.get(i).getCountryCode() + "\",\"" + cities.get(i).getContinent() + "\"\n");
        }
        writer.close();

        System.out.println("Created CSV file with cities");
    }


}
