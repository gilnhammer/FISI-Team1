package WatherStation;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.NetworkException;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tareklutz on 06.07.17.
 */
public class WeatherListener {

    Barometer barometer;
    Humidity humidity;
    Illuminance illuminance;
    LcdAnzeige lcdAnzeige;

    Connection connection;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    WeatherListener(){


        barometer = new Barometer();
        humidity = new Humidity();
        illuminance = new Illuminance();
        lcdAnzeige = new LcdAnzeige();


    }


    public static void main(String[] args) {

        LcdAnzeige lcdAnzeige = new LcdAnzeige();


        WeatherListener weatherListener = new WeatherListener();

        try {
            lcdAnzeige.setLcd();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Timer t = new Timer();

        t.schedule(new TimerTask(){

            @Override
            public void run() {
                try {
                    weatherListener.insertInto();
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                } catch (NetworkException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (AlreadyConnectedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }, 0, 50000); //alle 5 sekunden...


    }

    public void insertInto() throws NotConnectedException, NetworkException, TimeoutException, AlreadyConnectedException, SQLException {

        System.out.println("Illumiance: " + illuminance.getIllumiance());
        System.out.println("Temperatur: " + barometer.getTemperature());
        System.out.println("Humidity: " + humidity.getHumidity());
        System.out.println("AirPressure: " + barometer.getAirPressure());

        double temperatur = barometer.getTemperature();
        double luftdruck = barometer.getAirPressure();
        double feuchtigkeit = humidity.getHumidity();
        double helligkeit = illuminance.getIllumiance();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("WetterStation");
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setUser("postgres");
        dataSource.setPassword("Pa$$w0rd");

        connection = dataSource.getConnection();

        String simpleUpdateStr = "insert into wetterdaten (temperatur, luftdruck, luftfeuchtigkeit, helligkeit, time) values (?,?,?,?,?)";
        PreparedStatement simpleUpdate = connection.prepareStatement(simpleUpdateStr);
        simpleUpdate.setDouble(1, temperatur);
        simpleUpdate.setDouble(2, luftdruck);
        simpleUpdate.setDouble(3, feuchtigkeit);
        simpleUpdate.setDouble(4, helligkeit);
        simpleUpdate.setString(5, timestamp.toString());


        int rowsModified = simpleUpdate.executeUpdate();
        System.out.println("Rows modified: " + rowsModified);

    }



}
