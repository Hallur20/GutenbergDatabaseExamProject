package gutenberg;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class CSVHelper {

    public void setCorrectSecurefilePath() throws IOException, SQLException {
        String secureFilePath = SQLDataMapper.getSecureFilePath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/main/java/files/InsertCsv.sql");
        Path path2 = Paths.get(System.getProperty("user.dir") + "/src/main/java/files/InsertCsv.sql");
        Charset charset = StandardCharsets.UTF_8;
        String content = new String(Files.readAllBytes(path), charset);
        String content2 = new String(Files.readAllBytes(path), charset);
        content = content.replaceAll("%path%", "'" + secureFilePath + "cities.csv'");
        content2 = content2.replaceAll("%path%", "'" + secureFilePath + "books.csv");
        Files.write(path, content.getBytes(charset));
        Files.write(path2, content2.getBytes(charset));
//        SQLDataMapper.insertCities();
        
        SQLDataMapper.insertBooks();
        
        content = content.replaceAll("'" + secureFilePath + "cities.csv'", "%path%");
        content2 = content2.replaceAll("'" + secureFilePath + "cities.csv'", "%path%");
        Files.write(path, content.getBytes(charset));
        Files.write(path2, content2.getBytes(charset));
    }

    public void executeMySqlCommands(String containerName) {
        String dockerMysqlName = containerName;
        String[] mySQLcommands = new String[]{"/bin/bash", "docker cp " + System.getProperty("user.dir") + "/src/main/java/files/citiesForDocker.csv " + dockerMysqlName + ":/home/cities.csv",
        "docker cp " + System.getProperty("user.dir") + "/src/main/java/files/booksForDocker.csv " + dockerMysqlName + ":/home/books.csv"};
        Runtime r = Runtime.getRuntime();
        Process p = null;
        String command1 = null;
        try {
            for (String command : mySQLcommands) {
                command1 = command;
                p = r.exec(command1);
            }
            System.out.println("Reading csv into Database");
        } catch (Exception e) {
            System.out.println("Error executing " + command1 + e.toString());
        }
    }

    public void executeMongoCommands(String containerName) {
        String dockerMongodbName = containerName;
        String[] mongoCommands = new String[]{"/bin/bash", "docker cp " + System.getProperty("user.dir") + "/src/main/java/Files/citiesForDocker.csv " + dockerMongodbName + ":/home/cities.csv",
            "docker exec -d dbms mongoimport -d mydb -c gutenberg --type csv --file /home/cities.csv --headerline"};
        Runtime r = Runtime.getRuntime();
        Process p = null;
        String command1 = null;
        try {
            for (String command : mongoCommands) {
                command1 = command;
                p = r.exec(command1);
            }
            System.out.println("Reading csv into Database");

        } catch (Exception e) {
            System.out.println("Error executing " + command1 + e.toString());
        }
    }
}
