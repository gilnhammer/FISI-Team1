/**
 * Created by tareklutz on 04.07.17.
 */

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class main {

    /**
     *
     */
    Connection connection;

    public main() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("WetterStation");
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setUser("postgres");
        dataSource.setPassword("Pa$$w0rd");

        connection = dataSource.getConnection();
    }


    public void insert(int temperatur) throws SQLException {

        String simpleUpdateStr = "insert into wetterdaten (temperatur) values (?)";
        PreparedStatement simpleUpdate = connection.prepareStatement(simpleUpdateStr);
        simpleUpdate.setInt(1, temperatur);


        int rowsModified = simpleUpdate.executeUpdate();
        System.out.println("Rows modified: " + rowsModified);

    }

    public void output(int temp){

        System.out.println("Hallo: " + temp);

    }

}