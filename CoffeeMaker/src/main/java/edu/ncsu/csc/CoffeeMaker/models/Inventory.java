package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long    id;


    /** Ingredients inside the inventory */
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )    
    private List<Ingredient> ingredients; 

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        ingredients = new ArrayList<Ingredient>(); 
    }

    /**
     * Use this to create inventory with specified ingredients.
     *
     * @param ingredientList
     *            list of ingredients
     */
    public Inventory ( final List<Ingredient> ingredientList ) {
        ingredients = ingredientList;
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Check the number of unit of specified ingredient name within the inventory. 
     * 
     * @param ingredientName
     *         name of the ingredient
     * @return amount
     */
    public Integer checkIngredient(String ingredientName){
        int amount = 0; 
        for (Ingredient ingredient: ingredients){

            if (ingredient.getName().equals(ingredientName)){
                amount = ingredient.getAmount();
                return amount; 
            }
        }
        return null; 
    }


    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
    	
        List<Ingredient> recipeIngredients = r.getIngredients();
        int recipeIngredientsFound = 0;

        for (Ingredient recipeIngredient: recipeIngredients){
        	String name = recipeIngredient.getName();
            for (Ingredient inventoryIngredient: ingredients){
            	if (name.equals(inventoryIngredient.getName())) {
            		if (inventoryIngredient.getAmount() < recipeIngredient.getAmount()) return false;
            		recipeIngredientsFound++;
            	}
            }
        }
        if (recipeIngredientsFound < recipeIngredients.size()) return false;
        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
    	
        if ( enoughIngredients( r ) ) {
            for (Ingredient invIngredient: ingredients) {
            	String name = invIngredient.getName();
                for (Ingredient recipeIngredient: r.getIngredients()) 
                	if (name.equals(recipeIngredient.getName())) 
                		invIngredient.setAmount(invIngredient.getAmount() - recipeIngredient.getAmount());
            }
            return true;
        }
        else return false;
    }

    /**
     * Adds ingredient units to the inventory
     *
     * @param ingredient to add
     */
    public void addIngredient ( final Ingredient ingredient ) {
    	
    	Ingredient invIngredient = getIngredient(ingredient.getName());
    	
    	if (invIngredient == null) ingredients.add(ingredient);
    	
    	else invIngredient.setAmount(invIngredient.getAmount() + ingredient.getAmount());
    	
    }

    /**
     * Returns the list of ingredients in the inventory
     * @return ingredients
     *          ingredients in the inventory 
     */
    public List<Ingredient> getIngredients(){
        return ingredients; 
    }
    
    /**
     * Set the ingredient list
     * @param ingredients to set
     */
    public void setIngredients(List<Ingredient> ingredients) {
    	this.ingredients = ingredients;
    }
    
    /**
     * Returns an ingredient from ingredients, searches for a name of the ingredient
     * Returns null if the ingredient cannot be found
     * 
     * @param name of ingredient
     * @return ingredient
     */
    public Ingredient getIngredient(String name) {
    	for (Ingredient ingredient : ingredients) {
    		if (ingredient.getName().equals(name)) return ingredient;
    	}
    	return null;
    }
    
    /**
     * sets an ingredient amount from a name and amount
     *
     * @param name
     *            The name of the ingredient.
     * @param amount
     * 			  The amount of the ingredient to set.
     */
    public void setIngredient(String name, Integer amount) {

    	for (Ingredient ingredient : ingredients) {
    		if (name.equals(ingredient.getName())){
    			ingredient.setAmount(amount);
    			return;
    		}
    	}
    }
    /** 
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for (Ingredient invIngredient: ingredients){
            buf.append(invIngredient.getName() + ": " + String.valueOf(invIngredient.getAmount()) + "\n"); 
        }
        return buf.toString();
    }
    

}
