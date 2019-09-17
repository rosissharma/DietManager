package DietManager.Model.Util;

import DietManager.Model.Entry.Entry;
import DietManager.Model.Food.Food;
import DietManager.Model.ICSVable;

import java.io.*;
import java.util.*;

/**
 * This class handles low-level interactions with the CSV log files.
 * It will read and write to both food.csv and log.csv
 * Implements ICSVable interface
 */
public class IOHandler {

    // File IO utilities.
    private BufferedWriter bw;
    private BufferedReader br;

    /**
     * Writes an entry to the given file.
     *
     * @param foods A Collection of food items to be written to file.
     */
    public void writeToFile(Collection<ICSVable> foods, File file) throws IOException {
        try {
            this.bw = new BufferedWriter(new FileWriter(file));
            for (ICSVable food : foods) {
                bw.write(food.getCSV());
                bw.newLine();
            }
            this.bw.flush();
        } catch (IOException e) {
            throw new IOException("Could not write required file.");
        } finally {
            this.bw.close();
        }
    }

    /**
     * Reads in the entries from a selected csv file to an arraylist of string arrays
     *
     * @return An array list of strings containing the entries from given csv file.
     */
    public ArrayList<String> readFromFile(File file) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        try {
            this.br = new BufferedReader(new FileReader(file));
            String buffer;
            while((buffer = this.br.readLine()) != null){
                list.add(buffer.trim());
            }
        } catch (IOException e) {
            throw new IOException("Could not write to required file.");
        } finally {
            this.br.close();
        }
        return list;
    }
}
