package WeatherStation;

/**
 * Interface to be implemented by TempSensor.
 * getCelsius will return a double.
 *
 * Initial Author
 *      @author Sachi Nutulapati
 */
public interface ITempSensor{

    /**
     * Simulate a new reading based on the last reading and whether the
     * temperature is trending up or down. We assume that the temperature
     * has a 80% chance of continuing on its current trend and 20%
     * chance of changing direction. Also, we will not allow changes
     * outside of the specific min. and max. temperatures.
     *
     * @return The raw reading of the temperature sensor.
     */
    double rawReading();


    /**
     * Returns the Celsius temperature of the temperature sensor.
     * @return The Celsius temperature of the temperature sensor.
     */
    double getCelsius();


    /**
     * Returns the Fahrenheit temperature of the temperature sensor.
     * @return The Fahrenheit temperature of the temperature sensor.
     */
    double getFahrenheit();
}
