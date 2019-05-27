package gutenberg;

import entity.City;
import entity.Book;
import entitymanager.BookManager;
import entitymanager.CityManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        SQLDataMapper.createSchema();

        CityManager cityManager = new CityManager();
        List<City> cities = cityManager.readCities();
        cityManager.createCitiesCSV(cities);

        BookManager bookManager = new BookManager();
        List<Book> books = bookManager.readBooks();

//        CSVHelper csvHelper = new CSVHelper();
//        csvHelper.executeMySqlCommands("some-mysql");
//        csvHelper.executeMongoCommands("dbms");
//        csvHelper.setCorrectSecurefilePath();
    }
}
