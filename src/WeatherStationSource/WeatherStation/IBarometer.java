package WeatherStation;

/**
 * Intereface to be implemented by (simulated) barometers.
 * rawReading will return a Double.
 *
 * Initial Author
 *      @author Brennan Jackson
 */
public interface IBarometer{

    /**
     * @return The raw reading from the barometer sensor.
     */
    double rawReading();
}