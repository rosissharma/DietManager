package WeatherStation;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class for a simple computer based weather station that reports the current
 * temperature (in Celsius) every second. The station is attached to a
 * sensor that reports the temperature as a 16-bit number (0 to 65535)
 * representing the Kelvin temperature to the nearest 1/100th of a degree.
 *
 *      This class is implements Runnable so that it can be embedded in a Thread
 * which runs the periodic sensing.
 *
 * Initial Author
 *      @author Michael J. Lutz
 *
 * Other Contributers
 *      @author Zachary "Bubba" Lichvar
 *      @author Rosis Sharma
 */
public class WeatherStation extends Observable {

    // Temperature Sensor.
    private final ITempSensor tempSensor;
    // Pressure Sensor.
    private final IBarometer pressureSensor;

    // 1 sec = 1000ms.
    private final long PERIOD = 1000;      // 1 sec = 1000 ms.

    // Inches of Mercury to Millibars constant.
    private final double MercuryToMillibars = 33.8639;

    // The current reading.
    private double tempReading;
    private double pressureReading;

    /**
     * When a WeatherStation object is created, it in turn creates the sensor
     * object it will use.
     *
     * @param tempSensor The temperature sensor instance that this WeatherStation
     *                   will use to monitor the temperature.
     * @param pressureSensor The pressure sensor instance that this WeatherStation
     *                   will use to monitor the atmospheric pressure.
     */
    public WeatherStation(ITempSensor tempSensor, IBarometer pressureSensor) {
        // Get our sensor instances.
        this.tempSensor = tempSensor;
        this.pressureSensor = pressureSensor;

        // Utility timer that is used to update internal state and notify observers.
        Timer updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(new TemperatureUpdateTask(), PERIOD, PERIOD);
    }

    /**
     * Returns the Kelvin temperature of the temperature sensor.
     * @return The Kelvin temperature of the temperature sensor.
     */
    public double getKelvinTemp(){
        return this.tempReading / 100.0;
    }

    /**
     * Returns the Celsius temperature of the temperature sensor.
     * @return The Celsius temperature of the temperature sensor.
     */
    public double getCelsiusTemp(){
        return tempSensor.getCelsius();
    }

    /**
     * Returns the Fahrenheit temperature of the temperature sensor.
     * @return The Fahrenheit temperature of the temperature sensor.
     */
    public double getFahrenheitTemp(){
        return tempSensor.getFahrenheit();
    }

    /**
     * Returns the inches of mercury reported by the pressure sensor.
     * @return The inches of mercury reported by the pressure sensor.
     */
    public double getInchesOfMercury(){
        return this.pressureReading;
    }

    /**
     * Returns the Millibars reported by the pressure sensor.
     * @return The Millibars reported by the pressure sensor.
     */
    public double getMillibars(){
        return this.pressureReading * MercuryToMillibars;
    }

    /**
     * A task that fires our updates with the temperature changed event.
     *
     * This task will:
     * 1) Update our readings from the temperature sensor.
     * 2) Fire off a temperature update event to subscribers notifying them to update for new information.
     *
     * @author Zachary "Bubba" Lichvar
     */
    private class TemperatureUpdateTask extends TimerTask{
        @Override
        public void run() {
            synchronized (this) {
                tempReading = tempSensor.rawReading();
                pressureReading = pressureSensor.rawReading();
                setChanged();
                notifyObservers();
            }
        }
    }
}
