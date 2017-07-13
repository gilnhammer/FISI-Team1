package WeatherStation;

import com.tinkerforge.*;

/**
 * Created by tareklutz on 06.07.17.
 */
public class Barometer {

    private static final String HOST = "192.168.178.95";
    private static final int PORT = 4223;

    // Change XYZ to the UID of your Ambient Light Bricklet 2.0
    private static final String UID = "vNT";


    private BrickletBarometer brickletBarometer;

    public int getAirPressure() {

        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletBarometer b = new BrickletBarometer(UID, ipcon); // Create device object

        try {
            ipcon.connect(HOST, PORT); // Connect to brickd
        } catch (NetworkException e) {
            e.printStackTrace();
        } catch (AlreadyConnectedException e) {
            e.printStackTrace();
        }
        // Don't use device before ipcon is connected

        // Get current air pressure (unit is mbar/1000)
        int airPressure = 0; // Can throw com.tinkerforge.TimeoutException
        try {
            airPressure = b.getAirPressure();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }

        return airPressure;
    }


    public double getTemperature() throws TimeoutException, NotConnectedException, NetworkException, AlreadyConnectedException {
        IPConnection ipcon = new IPConnection(); // Create IP connection
        brickletBarometer = new BrickletBarometer(UID, ipcon);

        ipcon.connect(HOST, PORT); // Connect to brickd
        // Don't use device before ipcon is connected

        double temperature = brickletBarometer.getChipTemperature();

        ipcon.disconnect();

        return temperature;
    }

}
