package WatherStation;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.NetworkException;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;
import javafx.concurrent.Task;
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

        }, 0, 5000); //alle 5 sekunden...

        try {
            lcdAnzeige.setLcd();
        } catch (Exception e) {
            e.printStackTrace();
        }


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

        new Thread(new Runnable() {
            public void run() {

                try {
                    connection = dataSource.getConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                String simpleUpdateStr = "insert into wetterdaten (temperatur, luftdruck, luftfeuchtigkeit, helligkeit, time) values (?,?,?,?,?)";
                PreparedStatement simpleUpdate = null;
                try {
                    simpleUpdate = connection.prepareStatement(simpleUpdateStr);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    simpleUpdate.setDouble(1, temperatur);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    simpleUpdate.setDouble(2, luftdruck);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    simpleUpdate.setDouble(3, feuchtigkeit);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    simpleUpdate.setDouble(4, helligkeit);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    simpleUpdate.setString(5, timestamp.toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    int rowsModified = simpleUpdate.executeUpdate();
                    System.out.println("Rows modified: " + rowsModified);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        }).start();









    }



}
