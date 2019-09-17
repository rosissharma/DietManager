How to build the program

    1. Use Intellij IDEA community edition to clone the the source code from this repository.

    2. Go to File -> Project Structure

    3. In the new window select Artifact -> the plus symbol -> Jar -> ...from module with dependencies.

    4. For the Main class, select the folder icon then select the class of "MAIN".

    5. You should be back at the Artifacts screen that you were at previously. Now give the name of "JAR".

    6. Right click the file with the Artifact symbol next to it in the output tab, click rename. Name the archive "DietManager_TeamB.jar"

    7. Click "Apply" and "Close".

    8. Go to the Main Menu Bar > Build > Build Artifacts... > JAR > Build

How to run the program

    1. Download the file "DietManager-TeamB.jar" and associated .csv files. Place them in the same directory.

    2. Ensure that your food and log files are in the same directory as the .jar file. They must be named "foods.csv", "exercises.csv" and "log.csv" respectively.

    3. From a command prompt or terminal navigate to the location of the jar file and run: "java -jar DietManager-TeamB.jar"

    Use the JavaFXUI or TextUI to navigate and use the application!

Optional: you may pass in the arguments:

    "--e2e" - Runs an end to end test
    "--dev-files" - Uses developer files (developer use only)
    "--text-ui" - Runs an optional TextUI along with the JavaFXUI

Using the program:

- The program is very straightforward and easy to use. The tables provided insight to the users foods, logs, and exercises along with a graph of nutritional information.
- The log dates may be clicked in the log table to reveal more information about the daily entries. 
- The menu bar at the top of the program can be used to perform CRUD operations on Exercises, Foods, and Entries.
- The usuer can also save from the menu bar.
- On the program exit, a save will also be performed.
