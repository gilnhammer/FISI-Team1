package WeatherStation;

import com.tinkerforge.*;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

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

    private static double tempOver = 30;
    private static double tempUnder = 0;

    static Connection connection;


    public static void setLcd() throws Exception {


        System.out.println("setLCD()");

        button1Lcd();

        IPConnection ipconListener = new IPConnection(); // Create IP connection
        BrickletLCD20x4 lcd = new BrickletLCD20x4(UID, ipconListener); // Create device object

        ipconListener.connect(HOST, PORT); // Connect to brickd
        // Don't use device before ipcon is connected

        new Thread(new Runnable() {
            public void run() {

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
                            } catch (NotConnectedException e) {
                                e.printStackTrace();
                            }

                        }

                        if(button == 2){
                            try {
                                button3Lcd();
                            } catch (NetworkException e) {
                                e.printStackTrace();
                            } catch (AlreadyConnectedException e) {
                                e.printStackTrace();
                            } catch (NotConnectedException e) {
                                e.printStackTrace();
                            }
                        }

                        if(button == 3){

                        }

                    }
                });

                System.out.println("Press key to exit");
                try {
                    System.in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        //ipconListener.disconnect();

    }


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

        connection.close();





    }

    public static void button1Lcd() throws NotConnectedException, NetworkException, AlreadyConnectedException, SQLException {

        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletLCD20x4 lcd = new BrickletLCD20x4(UID, ipcon); // Create device object

        ipcon.connect(HOST, PORT); // Connect to brickd

        try {
            lcd.clearDisplay();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }

        getData(1);

        //Temperatur ausgeben
        String text = String.format("Temperatur  %5.2f %cC", temperature.get(0)/100.0, 0xDF);
        try {
            lcd.writeLine((short)0, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        //Luftdruck ausgeben
        text = String.format("Luftdruck %7.2f mb", airPressure.get(0)/1000.0);
        try {
            lcd.writeLine((short)1, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        //Feuchtigkeit ausgeben
        text = String.format("Luftfeuchte   %6.2f %%", humidity.get(0)/10.0);
        try {
            lcd.writeLine((short)2, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        String zustand;

        if(temperature.get(0) <= tempOver){
            zustand = "   WARNUNG +";
        }else if (temperature.get(1) <= tempUnder){
            zustand = "   WARNUNG -";
        }else{
            zustand = "         OK";
        }

        //Zustand
        text = "Zustand " + zustand;
        try {
            lcd.writeLine((short)3, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }







        ipcon.disconnect();

    }


    public static void button2Lcd() throws NetworkException, AlreadyConnectedException, SQLException, NotConnectedException {

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

        //Helligkeit ausgeben
        String text = String.format("Luftfeuchtigkeit (%%)");
        try {
            lcd.writeLine((short)0, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        //Feuchtigkeit ausgeben
        text = String.format("Durchschnitt   %2.2f %%", humidityAvg/10.0);
        try {
            lcd.writeLine((short)1, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        //Luftdruck ausgeben
        text = String.format("Maximalwert    %3.2f %%", humidityMax/10.0);
        try {
            lcd.writeLine((short)2, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        //Temperatur ausgeben
        text = String.format("Minimalwert    %3.2f %%", humidityMin/10.0);
        try {
            lcd.writeLine((short)3, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        ipcon.disconnect();

        System.out.println("Minimal: " + humidityMin + " Maximal: " + humidityMax + " Durchschnitt: " + humidityAvg);

    }

    public static void button3Lcd() throws NetworkException, AlreadyConnectedException, NotConnectedException {

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        LocalDate date = LocalDate.now();


        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletLCD20x4 lcd = new BrickletLCD20x4(UID, ipcon); // Create device object

        ipcon.connect(HOST, PORT); // Connect to brickd

        String text = "------M/WEATHER------";
        try {
            lcd.writeLine((short)0, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        text = "    " + dateFormat.format(new Date())+ " Uhr";
        try {
            lcd.writeLine((short)1, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        String wochentag = new SimpleDateFormat("EEEE").format(new Date());

        text = "       " + wochentag;
        try {
            lcd.writeLine((short)2, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        String datum = new SimpleDateFormat("dd.MM.yyyy").format(new Date());

        text = "     " + datum;
        try {
            lcd.writeLine((short)3, (short)0, text);
        } catch(com.tinkerforge.TinkerforgeException e) {
        }

        ipcon.disconnect();
    }


}
