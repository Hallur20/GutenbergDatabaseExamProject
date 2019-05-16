/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gutenberg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author hallur
 */
public class SQLDataMapper {

    public static void insertCities() {
        try {
            Connection con = SqlConnector.getConnection();

//        for (City c : cities) {
            String query = "LOAD DATA LOCAL INFILE '/home/hallur/NetBeansProjects/Gutenberg/src/main/java/Files/yourfile.csv' \n"
                    + "INTO TABLE Cities \n"
                    + "FIELDS TERMINATED BY ',' \n"
                    + "ENCLOSED BY '\"'\n"
                    + "LINES TERMINATED BY '\\n'\n"
                    + "IGNORE 1 ROWS;";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.execute();
//        }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
