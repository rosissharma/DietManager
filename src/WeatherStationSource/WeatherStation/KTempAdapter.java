 package WeatherStation;

/**
 * Interface to be implemented by TempSensor.
 *
 * Initial Author
 *      @author Sachi Nutulapati
 *
 * Other Contributors
 *      @author Brandon Connors
 *
 */
 
public class KTempAdapter implements ITempSensor {
   
   private TempSensor kts;
   private double K2C_CONVERT = -27315;

   public KTempAdapter(TempSensor kts) {
      this.kts = kts;
   }

   /**
    * Simulate a new reading based on the last reading and whether the
    * temperature is trending up or down. We assume that the temperature
    * has a 80% chance of continuing on its current trend and 20%
    * chance of changing direction. Also, we will not allow changes
    * outside of the specific min. and max. temperatures.
    *
    * @return The raw reading of the temperature sensor.
    */
   public double rawReading() {
    return kts.rawReading();
   }

   /**
    * Returns the Celsius temperature of the temperature sensor.
    * @return The Celsius temperature of the temperature sensor.
    */
   public double getCelsius() {
      return (rawReading() + K2C_CONVERT) / 100.0;
   }

   /**
    * Returns the Fahrenheit temperature of the temperature sensor.
    * @return The Fahrenheit temperature of the temperature sensor.
    */
   public double getFahrenheit(){
      return ( (getCelsius() *(9/5.0)) +32);
   }
}

