/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gutenberg;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author hallur
 */
public class Main {

    static ArrayList<City> cities = new ArrayList<>();

    public static void main(String[] args) throws SQLException, Exception {

//        Connection con = SqlConnector.getConnection();
        String fileName = "/home/hallur/NetBeansProjects/GutenbergDatabaseExamProject/src/main/java/Files/cities15000.txt";
        CSVReader reader = new CSVReader(new FileReader(fileName), '\t');
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            cities.add(new City(Integer.parseInt(nextLine[0]), nextLine[1], Double.parseDouble(nextLine[4]), Double.parseDouble(nextLine[5]), Integer.parseInt(nextLine[14]), nextLine[8], nextLine[17]));
        }
        System.out.println(cities.size());

        Writer writer = new FileWriter("/home/hallur/NetBeansProjects/GutenbergDatabaseExamProject/src/main/java/Files/citiesForDocker.csv");

        writer.append("\"id\", \"cityName\", \"latitude\", \"longitude\", \"population\", \"countryCode\", \"continent\"\n");
        for (City c : cities) {
            writer.append("\"" + c.getId() + "\",\"" + c.getCityName() + "\"," + c.getLatitude() + ","
                    + c.getLongitude() + "," + c.getPopulation() + ",\"" + c.getCountryCode() + "\",\"" + c.getContinent() + "\"\n");
        }
        SQLDataMapper.createSchema();
        String sqlDockerName = "some-mysql";
        String[] dot = new String[]{"/bin/bash", "-c", "docker cp " + System.getProperty("user.dir") + "/src/main/java/Files/citiesForDocker.csv " + sqlDockerName + ":/home/cities.csv", "with", "args"};
        new ProcessBuilder(dot).start();
        CSVHelper csvh = new CSVHelper();
        csvh.setCorrectSecurefilePath();
    }
}
