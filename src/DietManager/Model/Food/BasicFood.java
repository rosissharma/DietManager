package DietManager.Model.Food;

import java.security.InvalidParameterException;

/**
 * This class is a representation of a concrete food object.
 * This object is used in a composite pattern with recipe objects.
 */
public class BasicFood extends Food {

   /**
    * Constructs a BasicFood object with the required parameters.
    * @param name The name of the food that is being instantiated.
    * @param calories The calories that the food has.
    * @param fat The fat that the food has.
    * @param carbs The carbs that the food has.
    * @param protein The protein that the food has.
    * @throws InvalidParameterException If any of the parameters are invalid, this will be thrown.
    */
   public BasicFood(String name, double calories, double fat, double carbs, double protein) throws InvalidParameterException {

      if ((name != null) && (name.length() > 0)) {
         this.name = name;
      } else {
         throw new InvalidParameterException("Invalid food name.");
      }

      if (calories >= 0) {
         this.calories = calories;
      } else {
         throw new InvalidParameterException("Invalid calorie value.");
      }

      if (fat >= 0) {
         this.fat = fat;
      } else {
         throw new InvalidParameterException("Invalid fat value.");
      }

      if (carbs >= 0) {
         this.carbs = carbs;
      } else {
         throw new InvalidParameterException("Invalid carbohydrates value." + this.name);
      }

      if (protein >= 0) {
         this.protein = protein;
      } else {
         throw new InvalidParameterException("Invalid protein value.");
      }
   }

   /**
    * Returns a string that is a CSV representations of the object.
    *
    * @return A string that is a CSV representations of the object.
    */
   @Override
   public String getCSV() {
      StringBuilder sb = new StringBuilder("b")
              .append(",")
              .append(this.name)
              .append(",")
              .append(this.calories)
              .append(",")
              .append(this.carbs)
              .append(",")
              .append(this.protein)
              .append(",")
              .append(this.fat);
      return sb.toString();
   }
}
