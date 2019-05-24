/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gutenberg;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 *
 * @author hallur
 */
public class CSVHelper {

    public void setCorrectSecurefilePath() throws IOException, SQLException {
        String secureFilePath = SQLDataMapper.getSecureFilePath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/main/java/Files/InsertCsv.sql");
        Charset charset = StandardCharsets.UTF_8;
        String content = new String(Files.readAllBytes(path), charset);
        content = content.replaceAll("%path%", "'" + secureFilePath + "cities.csv'");
        Files.write(path, content.getBytes(charset));

        SQLDataMapper.insertCities();
        content = content.replaceAll("'" + secureFilePath + "cities.csv'", "%path%");
        Files.write(path, content.getBytes(charset));
    }
}
