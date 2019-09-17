package GUIs;

import WeatherStation.WeatherStation;

import java.awt.Font ;
import java.awt.GridLayout ;
import java.util.*;

import javax.swing.JFrame ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;

/**
 * Swing UI class used for displaying the information from the
 * associated weather station object.
 * This is an extension of JFrame, the outermost container in
 * a Swing application.
 *
 * Initial Author
 *      @author Michael J. Lutz
 *
 * Other Contributers
 *      @author Zachary "Bubba" Lichvar
 */
public class SwingUI extends JFrame implements Observer {
    // Put weather station information here.
    private JLabel celsiusField, kelvinField, millibarsField,
            inchesOfMercuryField, fahrenheitField;

    // A Font object contains information on the font to be used to render text.
    private static Font labelFont = new Font(Font.SERIF, Font.PLAIN, 36) ;

    // The format of all of our numbers.
    private final String NUMBER_FORMAT= "%6.2f";

    // Our Weather Station instance.
    private WeatherStation weatherStation;

    /**
     * Create and populate the SwingUI JFrame with panels and labels to show the temperatures.
     *
     * @param weatherStation A instance of an object implementing the TemperatureGenerator interface
     *                       that we will subscribe to for updates.
     *
     * @since 1.0
     */
    public SwingUI(Observable weatherStation) {
        super("Weather Station");

        this.weatherStation = (WeatherStation)weatherStation;

        // The weather station object we are subscribing to.
        this.weatherStation.addObserver(this);

        // Remove ourselves from the subscribing to updates on shutdown.
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                SwingUI.this.weatherStation.deleteObserver(SwingUI.this);
            }
        });

        // WeatherStation frame is a grid of 1 row by an indefinite number of columns.
        this.setLayout(new GridLayout(1,0)) ;

        /*
         * There are two panels, one each for Kelvin and Celsius, added to the
         * frame. Each Panel is a 2 row by 1 column grid, with the temperature
         * name in the first row and the temperature itself in the second row.
         */
        JPanel panel;

        // Set up Kelvin display.
        panel = new JPanel(new GridLayout(2,1));
        this.add(panel);
        createLabel("Kelvin", panel);
        kelvinField = createLabel("", panel);

        // Set up Celsius display.
        panel = new JPanel(new GridLayout(2,1));
        this.add(panel);
        createLabel("Celsius", panel);
        celsiusField = createLabel("", panel);

        // Set up Fahrenheit display.
        panel = new JPanel(new GridLayout(2,1));
        this.add(panel);
        createLabel("Fahrenheit", panel);
        fahrenheitField = createLabel("", panel);

        // Set up InchesOfMercury display.
        panel = new JPanel(new GridLayout(2,1));
        this.add(panel);
        createLabel("Inches", panel);
        inchesOfMercuryField = createLabel("", panel);

        // Set up Millibars display.
        panel = new JPanel(new GridLayout(2,1));
        this.add(panel);
        createLabel("Millibars", panel);
        millibarsField = createLabel("", panel);

        // Set up the frame's default close operation pack its elements, and make the frame visible.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Create a Label with the initial value of title, place it in
     * the specified panel, and return a reference to the Label
     * in case the caller wants to remember it.
     * @param title The text to put on the label.
     * @param panel The panel to add the label to.
     * @since 1.0
     */
    private JLabel createLabel(String title, JPanel panel) {
        JLabel label = new JLabel(" "+title+" ");

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(labelFont);
        panel.add(label);

        return label;
    }

    /**
     * Set the label holding the Kelvin temperature.
     * @param temperature The temperature to set the kelvin field to.
     * @since 1.0
     */
    private void setKelvinJLabel(double temperature) {
        kelvinField.setText(String.format(NUMBER_FORMAT, temperature));
    }

    /**
     * Set the label holding the Celsius temperature.
     * @param temperature The temperature to set the celsius field to.
     * @since 1.0
     */
    private void setCelsiusJLabel(double temperature) {
        celsiusField.setText(String.format(NUMBER_FORMAT, temperature));
    }

    /**
     * Set the label holding the Celsius temperature.
     * @param temperature The temperature to set the celsius field to.
     * @since 1.3
     */
    private void setFahrenheitLabel(double temperature) {
        fahrenheitField.setText(String.format(NUMBER_FORMAT, temperature));
    }

    /**
     * Set the label holding the Inches of Mercury.
     * @param pressure The inches of mercury to set the inchesOfMercury field to.
     * @since 1.3
     */
    private void setInchesOfMercuryLabel(double pressure) {
        inchesOfMercuryField.setText(String.format(NUMBER_FORMAT, pressure));
    }

    /**
     * Set the label holding the millibars of pressure.
     * @param pressure The millibars to set the millibars field to.
     * @since 1.3
     */
    private void setMillibarsLabel(double pressure) {
        millibarsField.setText(String.format(NUMBER_FORMAT, pressure));
    }

    /**
     * This is the method used to update the GUI based on when the observable notifies us of its changed state.
     * @param observable The Observable that this class is observing.
     * @param unused Unused.
     * @since 1.2
     */
    public void update(Observable observable, Object unused){
        if (this.weatherStation.equals(observable)) {
            this.setCelsiusJLabel(this.weatherStation.getCelsiusTemp());
            this.setKelvinJLabel(this.weatherStation.getKelvinTemp());
            this.setFahrenheitLabel(this.weatherStation.getFahrenheitTemp());
            this.setInchesOfMercuryLabel(this.weatherStation.getInchesOfMercury());
            this.setMillibarsLabel(this.weatherStation.getMillibars());
        }
    }
}
