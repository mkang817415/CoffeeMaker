package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long    id;

    /** Recipe name */
    private String  name;

    /** Recipe price */
    @Min ( 0 )
    private Integer price;

    /** Ingredient List */
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )    
    private List<Ingredient> ingredients;
    	
    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe() {
    	ingredients = new ArrayList<Ingredient>();
        this.name = "";
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
    	for (Ingredient ingredient : ingredients) {
    		if (ingredient.getAmount() != 0) return false;
    	}
        return true;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }
    
    /**
     * Add a new ingredient to the list of ingredients
     * 
     * @param ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
    	ingredients.add(ingredient); 
    }
    
    /**
     * Remove an ingredient from the list of ingredients
     * 
     * @param name of ingredient to remove
     * @return removed ingredient
     */
    public Ingredient removeIngredient(String name) {
    	for (int i = 0; i < ingredients.size(); i++) {
    		if (name.equals(ingredients.get(i).getName())) return ingredients.remove(i);
    	}
    	return null;
    }

    /**
     * gets an ingredient from a name, returns if ingredient isn't found
     *
     * @param name
     *            The name of the requested ingredients.
     * @return ingredient
     * 			  The ingredient in the recipe.
     */

    public Ingredient getIngredient(String name) {
    	for(Ingredient ingredient : ingredients) {
    		if (name.equals(ingredient.getName())){
    			return ingredient;
    		}
    	}
    	return null;
    }

    /** 
     * gets the ingredient list from the recipe
     * 
     * @return ingredients
     */
    public List<Ingredient> getIngredients() {
    	return ingredients;
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
     * Set the ingredient list to a new list of ingredients
     * 
     * @param ingredients to set
     */
    public void setIngredients(List<Ingredient> ingredients) {
    	this.ingredients = ingredients;
    }
    
    
    
    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        return name;
    }
    
    /**
     * Returns the hash code of the recipe.
     *
     * @return hash code
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    /**
     * Returns a string representation of the recipe.
     *
     * @return string representation
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}