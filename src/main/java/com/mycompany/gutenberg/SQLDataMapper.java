/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gutenberg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author hallur
 */
public class SQLDataMapper {

    public static void createSchema() throws SQLException {
        Connection con = SqlConnector.getConnection();
        Statement stmt = con.createStatement();
        try {
            File f = new File("/home/hallur/NetBeansProjects/GutenbergDatabaseExamProject/src/main/java/Files/GutenbergScript.sql"); // source path is the absolute path of dumpfile.

            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line = null, old = "";
            line = bf.readLine();
            while (line != null) {
                //q = q + line + "\n";
                if (line.endsWith(";")) {
                    stmt.executeUpdate(old + line);
                    old = "";
                } else {
                    old = old + "\n" + line;
                }
                line = bf.readLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void insertCities() throws SQLException {
        Connection con = SqlConnector.getConnection();
        Statement stmt = con.createStatement();
        try {
            File f = new File("/home/hallur/NetBeansProjects/GutenbergDatabaseExamProject/src/main/java/Files/InsertCsv.sql"); // source path is the absolute path of dumpfile.

            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line = null, old = "";
            line = bf.readLine();
            while (line != null) {
                //q = q + line + "\n";
                if (line.endsWith(";")) {
                    stmt.executeUpdate(old + line);
                    old = "";
                } else {
                    old = old + "\n" + line;
                }
                line = bf.readLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
}
}
