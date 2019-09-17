import GUIs.SwingUI;
import WeatherStation.*;

/**
 * This is the main entry point into the program. Launches the SWING UI.
 *
 * Initial Author
 *      @author Zachary "Bubba" Lichvar, znl2181
 *
 * @version 1.0
 *      Initial release.
 *
 * @version 1.1
 *      Removed other UI Options and related argument parsing.
 *      Removed printHelpText() since it related to previous version of program allowing
 *      for different UI's to be selected at initialization.
 */
public class MainSwing {

    /**
     * This is the main method of the program.
     * @param args Unused.
     * @since 1.0
     */
    public static void main(String[] args) {

        // Sensors needed for the WeatherStation.
        ITempSensor tempSensor = new KTempAdapter(new TempSensor());
        IBarometer barometer = new BarometerSensor();

        // The WeatherStation instance.
        WeatherStation weatherStation = new WeatherStation(tempSensor,barometer);

        // GO!
        new SwingUI(weatherStation);
    }
}
