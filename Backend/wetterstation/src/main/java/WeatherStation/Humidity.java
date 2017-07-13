package WeatherStation;

import com.tinkerforge.*;

/**
 * Created by tareklutz on 06.07.17.
 */
public class Humidity {

    private static final String HOST = "192.168.178.95";
    private static final int PORT = 4223;

    // Change XYZ to the UID of your Ambient Light Bricklet 2.0
    private static final String UID = "xzj";

    public int getHumidity() throws NetworkException, AlreadyConnectedException, TimeoutException, NotConnectedException {

        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletHumidity h = new BrickletHumidity(UID, ipcon); // Create device object

        ipcon.connect(HOST, PORT); // Connect to brickd
        // Don't use device before ipcon is connected

        // Get current humidity (unit is %RH/10)
        int humidity = h.getHumidity(); // Can throw com.tinkerforge.TimeoutException

        ipcon.disconnect();

        return humidity;
    }

}
