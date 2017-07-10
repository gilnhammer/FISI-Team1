package WatherStation;

import com.tinkerforge.*;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.awt.SystemColor.text;

/**
 * Created by tareklutz on 07.07.17.
 */
public class LcdAnzeige {

    private static final String HOST = "192.168.178.95";
    private static final int PORT = 4223;

    // Change XYZ to the UID of your LCD 20x4 Bricklet
    private static final String UID = "vBn";

    private static ArrayList<Double> id = new ArrayList<Double>();
    private static ArrayList<Double> illuminance = new ArrayList<Double>();
    private static ArrayList<Double> humidity = new ArrayList<Double>();
    private static ArrayList<Double> airPressure = new ArrayList<Double>();
    private static ArrayList<Double> temperature = new ArrayList<Double>();

    private static double humidityMin;
    private static double humidityMax;
    private static double humidityAvg;

    static Connection connection;


    public static void setLcd() throws Exception {


        System.out.println("setLCD()");

        button1Lcd();
        button2Lcd();

        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletLCD20x4 lcd = new BrickletLCD20x4(UID, ipcon); // Create device object

        ipcon.connect(HOST, PORT); // Connect to brickd
        // Don't use device before ipcon is connected

        lcd.addButtonPressedListener(new BrickletLCD20x4.ButtonPressedListener() {
            public void buttonPressed(short button) {
                System.out.println("Button Pressed: " + button);
            }
        });


        // Add button pressed listener
        lcd.addButtonPressedListener(new BrickletLCD20x4.ButtonPressedListener() {
            public void buttonPressed(short button) {
                System.out.println("Button Pressed: " + button);

                try {
                    lcd.clearDisplay();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }

                try {
                    lcd.backlightOn();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }

                if(button == 0){
                    try {
                        try {
                            button1Lcd();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (NotConnectedException e) {
                        e.printStackTrace();
                    } catch (NetworkException e) {
                        e.printStackTrace();
                    } catch (AlreadyConnectedException e) {
                        e.printStackTrace();
                    }

                }

                if(button == 1){

                    try {
                        button2Lcd();
                    } catch (NetworkException e) {
                        e.printStackTrace();
                    } catch (AlreadyConnectedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

                if(button == 2){

                }

                if(button == 3){

                }

            }
        });


        ipcon.disconnect();

    }


    /*
    public static void getData(int i) throws SQLException {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("WetterStation");
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setUser("postgres");
        dataSource.setPassword("Pa$$w0rd");

        connection = dataSource.getConnection();


        String simpleQueryStr = "SELECT * from wetterdaten ORDER BY id DESC LIMIT ?";
        PreparedStatement simpleQuery = connection.prepareStatement(simpleQueryStr);
        simpleQuery.setInt(1, i);

        ResultSet resultSet = simpleQuery.executeQuery();

        while (resultSet.next()){

            id = resultSet.getDouble(1);
            temperature = resultSet.getDouble(2);
            airPressure = resultSet.getDouble(3);
            humidity = resultSet.getDouble(4);
            illuminance = resultSet.getDouble(5);

            System.out.println("ID: " + id + " Temperatur: " + temperature + " Luftdruck: " + airPressure + " Luftfeuchtigkeit: " + humidity + " Helligkeit: " + illuminance);

        }

    }
    */

    public static void getData(int i) throws SQLException {


        id.clear();
        temperature.clear();
        airPressure.clear();
        humidity.clear();
        illuminance.clear();

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("WetterStation");
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setUser("postgres");
        dataSource.setPassword("Pa$$w0rd");

        connection = dataSource.getConnection();

        String simpleQueryStr = "SELECT * from wetterdaten ORDER BY id DESC LIMIT ?";
        PreparedStatement simpleQuery = connection.prepareStatement(simpleQueryStr);
        simpleQuery.setInt(1, 288);

        ResultSet resultSet = simpleQuery.executeQuery();

        int x = 0;

        while (resultSet.next()){

            id.add(resultSet.getDouble(1));
            temperature.add(resultSet.getDouble(2));
            airPressure.add(resultSet.getDouble(3));
            humidity.add(resultSet.getDouble(4));
            illuminance.add(resultSet.getDouble(5));



            //System.out.println(temperature.get(x));

            x += 1;
        }





    }

    public static void button1Lcd() throws NotConnectedException, NetworkException, AlreadyConnectedException, SQLException {

        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletLCD20x4 lcd = new BrickletLCD20x4(UID, ipcon); // Create device object

        ipcon.connect(HOST, PORT); // Connect to brickd

        getData(1);

        //Helligkeit ausgeben
        String text = String.format("Helligkeit %1.2f lx", illuminance.get(1)/100.0);
        try {
            lcd.writeLine((short)0, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        //Feuchtigkeit ausgeben
        text = String.format("Luftfeuchte   %6.2f %%", humidity.get(1)/10.0);
        try {
            lcd.writeLine((short)1, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        //Luftdruck ausgeben
        text = String.format("Luftdruck %7.2f mb", airPressure.get(1)/1000.0);
        try {
            lcd.writeLine((short)2, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        //Temperatur ausgeben
        text = String.format("Temperatur  %5.2f %cC", temperature.get(1)/100.0, 0xDF);
        try {
            lcd.writeLine((short)3, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        ipcon.disconnect();

    }


    public static void button2Lcd() throws NetworkException, AlreadyConnectedException, SQLException {

        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletLCD20x4 lcd = new BrickletLCD20x4(UID, ipcon); // Create device object

        ipcon.connect(HOST, PORT); // Connect to brickd

        try {
            getData(288);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        humidityMin = humidity.get(0);
        humidityMax = humidity.get(0);
        humidityAvg = humidity.get(0);

        for (double i : humidity) {

            if(i < humidityMin){
                humidityMin = i;
            }

            if(i > humidityMax){
                humidityMax = i;
            }

        }


        System.out.println("Minimal: " + humidityMin + " Maximal: " + humidityMax + " Durchschnitt: " + humidityAvg);

    }
}
