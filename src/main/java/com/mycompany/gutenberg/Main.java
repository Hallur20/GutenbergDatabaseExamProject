/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gutenberg;

import com.opencsv.CSVReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author hallur
 */
public class Main {

    static ArrayList<City> cities = new ArrayList<>();

    public static void main(String[] args) throws SQLException, Exception {
CSVHelper csvh = new CSVHelper();
//        Connection con = SqlConnector.getConnection();
        String fileName = System.getProperty("user.dir") + "/src/main/java/Files/cities15000.txt";
        CSVReader reader = new CSVReader(new FileReader(fileName), '\t');
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            cities.add(new City(Integer.parseInt(nextLine[0]), nextLine[1], Double.parseDouble(nextLine[4]), Double.parseDouble(nextLine[5]), Integer.parseInt(nextLine[14]), nextLine[8], nextLine[17]));
        }
        System.out.println(cities.size());

        Writer writer = new FileWriter(System.getProperty("user.dir") + "/src/main/java/Files/citiesForDocker.csv");

        writer.append("\"id\", \"cityName\", \"latitude\", \"longitude\", \"population\", \"countryCode\", \"continent\"\n");
        for (int i = 0; i < cities.size(); i++) {
            writer.append("\"" + cities.get(i).getId() + "\",\"" + cities.get(i).getCityName() + "\"," + cities.get(i).getLatitude() + ","
                    + cities.get(i).getLongitude() + "," + cities.get(i).getPopulation() + ",\"" + cities.get(i).getCountryCode() + "\",\"" + cities.get(i).getContinent() + "\"\n");
        }
        writer.close();

        SQLDataMapper.createSchema();
        
        csvh.executeMySqlCommands("some-mysql");
        csvh.executeMongoCommands("dbms");
        
        
        csvh.setCorrectSecurefilePath();
    }
}
