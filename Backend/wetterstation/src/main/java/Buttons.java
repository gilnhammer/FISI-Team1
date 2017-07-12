import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;

/**
 * Created by tareklutz on 10.07.17.
 */
public class Buttons {

    private static final String HOST = "192.168.178.95";
    private static final int PORT = 4223;

    // Change XYZ to the UID of your LCD 20x4 Bricklet
    private static final String UID = "vBn";

    // Note: To make the example code cleaner we do not handle exceptions. Exceptions
    //       you might normally want to catch are described in the documentation
    public static void main(String args[]) throws Exception {
        IPConnection ipcon = new IPConnection(); // Create IP connection
        BrickletLCD20x4 lcd = new BrickletLCD20x4(UID, ipcon); // Create device object

        ipcon.connect(HOST, PORT); // Connect to brickd
        // Don't use device before ipcon is connected

        // Add button pressed listener
        lcd.addButtonPressedListener(new BrickletLCD20x4.ButtonPressedListener() {
            public void buttonPressed(short button) {
                System.out.println("Button Pressed: " + button);
            }
        });

        // Add button released listener
        lcd.addButtonReleasedListener(new BrickletLCD20x4.ButtonReleasedListener() {
            public void buttonReleased(short button) {
                System.out.println("Button Released: " + button);
            }
        });

        System.out.println("Press key to exit"); System.in.read();
        ipcon.disconnect();
    }
}

