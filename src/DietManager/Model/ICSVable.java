package DietManager.Model;


/**
 * ICSVable is implemented by objects that are indented to be written to a file and have their data represented as CSV data.
 */
public interface ICSVable {

	/**
	 * Returns a CSV formatted representation of the data contained in the object.
	 *
	 * @return A CSV formatted representation of the data contained in the object.
	 */
	String getCSV();
}
