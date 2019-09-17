package DietManager.Controller;

import DietManager.Model.Food.BasicFood;
import DietManager.Model.Food.Food;
import DietManager.Model.Food.Recipe;
import DietManager.Model.FoodHandler;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * This is a controller that will crud against a food controller.
 */
public class TextFoodController {

    private FoodHandler foodHandler;
    private Scanner scan = new Scanner(System.in);

    /**
     * This will create a food controller to be used with the textUI.
     * @param foodHandler The handler that this controller will crud against.
     */
    public TextFoodController(FoodHandler foodHandler) {
        this.foodHandler = foodHandler;
    }

    /**
     * This will create a new menu for the user.
     */
    public void prompt(){
        System.out.println("\nSelect an option");
        System.out.println("1. View All Foods");
        System.out.println("2. Add Food");
        System.out.println("3. Update Food");
        System.out.println("4. Delete Food");
        System.out.println("0. Back");
        System.out.println("Select an Operation:");
        select(scan.nextLine());
    }

    /**
     * This will run call specific methods from the users input.
     * @param selection The users selection.
     */
    public void select(String selection){
        switch(selection){
            case "1":
                viewAll();
                prompt();
                break;
            case "2":
                create();
                break;
            case "3":
                update();
                break;
            case "4":
                delete();
                break;
            case "0":
                break;
            default:
                System.out.println("Invalid Selection. Try Again.");
                prompt();
                break;
        }
    }

    /**
     * This will create a new food and will add it to the food handler.
     */
    public void create(){
        String foodName = null;
        System.out.println("Is this a food or recipe (b/r)");
        String recipeOorFood = scan.nextLine();
        if(recipeOorFood.equals("b")){
            double calories = 0;
            double fat = 0;
            double protein = 0;
            double carbs = 0;
            System.out.println("\nPlease Enter the Name of a Food:");
            foodName = scan.nextLine();
            System.out.println("Please Enter the Total Calories:");
            calories = Double.parseDouble(scan.nextLine());
            System.out.println("Please Enter the Total Fat:");
            fat = Double.parseDouble(scan.nextLine());
            System.out.println("Please Enter the Total Protein:");
            protein = Double.parseDouble(scan.nextLine());
            System.out.println("Please Enter the Total Carbohydrates:");
            carbs = Double.parseDouble(scan.nextLine());
            if(foodHandler.add(processBasicFood(foodName,calories,fat,carbs,protein))){
                System.out.println("Food Successfully Created!");
            }else{
                System.out.println("There Was an Error Creating Your Food. Your Food Was Not Created.");
            }
        } else if(recipeOorFood.equals("r")){
            StringBuilder recipe = new StringBuilder("r,");
            System.out.println("What is the name of the recipe to be added?");
            recipe.append(scan.nextLine()).append(",");

            System.out.println("Please add 1 food item name that makes up this recipe:");
            recipe.append(scan.nextLine()).append(",");

            System.out.println("How many servings?");
            recipe.append(scan.nextLine());

            System.out.println("Do you have another item to add?(y/n)");
            String response = scan.nextLine();

            while(response.equals("y")){
                System.out.println("Please add the next item");
                recipe.append(",").append(scan.nextLine()).append(",");

                System.out.println("How many servings?");
                recipe.append(scan.nextLine());

                System.out.println("Do you have another item to add?(y/n)");
                response = scan.nextLine();
            }

            try{
                foodHandler.add(recipe.toString());
                System.out.println("Your new recipe has been added\n");
            } catch(InvalidParameterException ex){
                System.out.println("There was an error adding your recipe.\n");
            }

        } else{
            create();
        }

    }

    /**
     * This will print the details for a specific food.
     */
    public void view(){
        System.out.println("\nPlease Enter the Name of a Food:");
        String name = scan.nextLine();
        Food foods = foodHandler.get(name);
        System.out.println("Name: " + foods.getName());
        System.out.println("Calories: " + foods.getCalories());
        System.out.println("Fat: " + foods.getFat());
        System.out.println("Protein: " + foods.getProtein());
        System.out.println("Carbohydrates: " + foods.getCarbs());
    }

    /**
     * Used to update a food item.
     */
    public void update(){
        String response = null;

        viewAll();
        System.out.println("Please select the food you want to update:");
        response = scan.nextLine();
        ArrayList<Food> foods = new ArrayList<>(foodHandler.getAll());

        int index = Integer.parseInt(response) - 1;
        Food foodToDelete = foods.get(index);

        System.out.println("Food to be updated: " + foodToDelete.getName() + " Calories: " + foodToDelete.getCalories()
                + " Fat: " + foodToDelete.getFat()+ " Carbs: " + foodToDelete.getCarbs()+ " Protein: " + foodToDelete.getProtein());

        try{
            foodHandler.delete(foodToDelete);
        } catch(Exception ex){
            System.out.println();
        }

        create();

    }

    /**
     * This will delete a specific food
     */
    public void delete(){
        ArrayList<Food> foods = new ArrayList<>(foodHandler.getAll());
        viewAll();
        System.out.println("\nPlease Select a Food to Delete:");
        if(foodHandler.delete(foods.get(Integer.parseInt(scan.nextLine())-1))){
            System.out.println("Food Successfully Deleted!");
        }else{
            System.out.println("There Was an Error Deleting Your Food. Your Food Was Not Deleted.");
        }
    }

    /**
     * This will return a pretty print of all the foods that are in the handler.
     * This is used for view.
     */
    private void viewAll(){
        ArrayList<Food> foods = new ArrayList<>(foodHandler.getAll());
        for(int i = 0; i < foods.size(); i++){
            Food food = foods.get(i);
            System.out.println(i + 1 + ". " + food.getName() + " Calories: " + food.getCalories()
                    + " Fat: " + food.getFat()+ " Carbs: " + food.getCarbs()+ " Protein: " + food.getProtein());
        }
    }

    /**
     * This is used as a helper method to create a food.
     * @param foodName The name of the food.
     * @param calories The calories of the food.
     * @param fat The fat of the food.
     * @param protein The protein of the food.
     * @param carbs The carbs of the food.
     * @return The food with the given parameters
     */
    private Food processBasicFood(String foodName,double calories,double fat,double protein,double carbs){
        return new BasicFood(foodName,calories,fat,carbs,protein);
    }
}
