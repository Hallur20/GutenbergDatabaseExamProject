package gutenberg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDataMapper {

    public static void createSchema() throws SQLException {
        Connection con = SQLConnector.getConnection();
        Statement stmt = con.createStatement();

        try {
            File f = new File(System.getProperty("user.dir") + "/src/main/java/files/Gutenberg.sql"); // source path is the absolute path of dumpfile.

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

            System.out.println("Created schema Gutenberg");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void insertCities() throws SQLException {
        Connection con = SQLConnector.getConnection();
        Statement stmt = con.createStatement();
        try {
            File f = new File(System.getProperty("user.dir") + "/src/main/java/files/InsertCsv.sql"); // source path is the absolute path of dumpfile.

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

            System.out.println("Inserted all cities into MySQL");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getSecureFilePath() {
        String path = null;
        try {
            Connection con = SQLConnector.getConnection();
            PreparedStatement ps = con.prepareStatement("show variables like 'secure_file_priv';");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                path = rs.getString("Value");
                System.out.println(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

}
