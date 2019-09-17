package DietManager.Model.Food;

import DietManager.Model.ICSVable;

/**
 * This is an abstract calls that represents the properties of food.
 * It is inherited by all food objects such as Recipe and BasicFood.
 */
public abstract class Food implements ICSVable {

   //Variables to hold food name and amount of servings.
   String name = "INVALID FOOD";
   double servings = 0;
   double calories = 0;
   double fat = 0;
   double protein = 0;
   double carbs = 0;

   /**
    * Accessor for name.
    *
    * @return name of food
    */
   public String getName() {
      return this.name;
   }

   /**
    * Accessor for servings.
    *
    * @return servings of food
    */
   public double getServings() {
      return this.servings;
   }

   /**
    * Mutator for name.
    *
    * @param name The food name.
    */
   public void setName(String name) {
      if (name.length() > 0) {
         this.name = name;
      }
   }

   /**
    * Mutator for servings.
    *
    * @param servings The amount of servings.
    */
   public void setServings(double servings) {
      if (servings > 0) {
         this.servings = servings;
      }
   }

   /**
    * Accessor for calories defined by sub-class
    *
    * @return number of calories in food
    */
   public double getCalories() {
      return this.calories;
   }

   /**
    * Mutator for calories defined by sub-class
    *
    * @param calories The amount of protein.
    */
   public void setCalories(double calories) {
      if (calories > 0) {
         this.calories = calories;
      }
   }

   /**
    * Accessor for fat defined by sub-class
    *
    * @return Amount of fat in food
    */
   public double getFat() {
      return this.fat;
   }

   /**
    * Mutator for fat defined by sub-class
    *
    * @param fat The amount of fat.
    */
   public void setFat(double fat) {
      if (fat > 0) {
         this.fat = fat;
      }
   }

   /**
    * Accessor for carbs defined by sub-class
    *
    * @return number of carbs in food
    */
   public double getCarbs() {
      return this.carbs;
   }

   /**
    * Mutator for carbs defined by sub-class
    *
    * @param carbs The amount of carbs.
    */
   public void setCarbs(double carbs) {
      if (carbs > 0) {
         this.carbs = carbs;
      }
   }

   /**
    * Accessor for protein defined by sub-class
    *
    * @return Amount of protein in food
    */
   public double getProtein() {
      return this.protein;
   }

   /**
    * Mutator for protein defined by sub-class
    *
    * @param protein The amount of protein.
    */
   public void setProtein(double protein) {
      if (protein > 0) {
         this.protein = protein;
      }
   }
}
