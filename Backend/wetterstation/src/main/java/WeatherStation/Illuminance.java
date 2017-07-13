package WeatherStation;

import com.tinkerforge.*;



/**
 * Created by tareklutz on 06.07.17.
 */
public class Illuminance {

    private static final String HOST = "192.168.178.95";
    private static final int PORT = 4223;

    // Change XYZ to the UID of your Ambient Light Bricklet 2.0
    private static final String UID = "yhw";

    public double  getIllumiance() throws NetworkException, AlreadyConnectedException, TimeoutException, NotConnectedException {
        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletAmbientLightV2 al = new BrickletAmbientLightV2(UID, ipcon); // Create device object

        ipcon.connect(HOST, PORT); // Connect to brickd
        // Don't use device before ipcon is connected

        // Get current illuminance (unit is Lux/100)
        long illuminance = al.getIlluminance(); // Can throw com.tinkerforge.TimeoutException
        //System.out.println("Illuminance: " + illuminance/100.0 + " Lux");

        ipcon.disconnect();

        return illuminance;
    }

}
