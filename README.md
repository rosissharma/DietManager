# TERM-PROJECT: Diet Manager

A weight loss tracker which allows users an efficient means to track their daily calorie and weight goals. Users are
able to track their daily food intake and compare their daily caloric intake against a predetermined daily goal. Users are also able to track their weight and their weight goals in the application. 
Users can enter and save foods which they commonly consume, along with the nutrition information for these foods.

## Team

- Zachary "Bubba" Lichvar
- Brandon Connors
- Sachi Nutulapati
- Rosis Sharma
- Brennan Jackson 

## How to build the program
1. Use Intellij IDEA community edition to clone the the source code from this repository.

2. Go to File -> Project Structure

3. In the new window select Artifact -> the plus symbol -> Jar -> ...from module with dependencies.

4. For the Main class, select the folder icon then select the class of "MAIN".

5. You should be back at the Artifacts screen that you were at previously. Now give the name of "JAR".

6. Right click the file with the Artifact symbol next to it in the output tab, click rename. Name the archive "DietManager_TeamB.jar"

7. Click "Apply" and "Close".

8. Go to the Main Menu Bar > Build > Build Artifacts... > JAR > Build

## How to run the program
1. Build the file "DietManager-TeamB.jar"

2. Ensure that your food and log files are in the same directory as the .jar file. They must be named "foods.csv", "exercises.csv" and "log.csv" respectively. 

3. From a command prompt or terminal navigate to the location of the jar file and run:
"java -jar DietManager-TeamB.jar"

4. Use the CLI to navigate and use the application!

Optional: you may pass in the arguments:
- "--e2e" - Runs an end to end test
- "--dev-files" - Uses developer files (developer use only)
- "--text-ui" - Runs an optional TextUI along with the JavaFXUI

## Disclaimers
There are an obnoxious amount of popup confirmations for successes throughout the program. This is mainly the result of needing confirmation during development. The confirmations have been left in as to not disrupt working code so close to our deadline.

## Shortcomings
The TextUI is a proof of concept for a well designed MVC architecture, we have built in limited error handling to verify the users input into the CLI.
The user must enter in proper and valid dates as well as proper syntax and capitalization for foods and entries.  

The save button in the JavaFXUI is not working as intended. However, the program saves properly on shutdown so the user will not lose their data.

### Known bugs
When updating an Entry through the TextUI, the entry for the date will be properly created, however another "ghost-entry" may be creted where the day and month are swapped.

In the create entry JavaFXUI there are 2 "Exercise Entries" labels, the top one is supposed to read "Food Entries" rather than what it does now.

#### License
MIT License

See LICENSE for details.
