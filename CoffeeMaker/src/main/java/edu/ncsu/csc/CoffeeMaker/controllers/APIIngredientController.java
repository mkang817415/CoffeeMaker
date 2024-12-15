package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * API controller for arbitrary Ingredient objects
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {
	
	/**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
    @Autowired
    private IngredientService ingredientService;
    
    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService inventoryService;
	
    /**
     * Endpoint for getting an ingredient by its name
     * @param name of ingredient
     * @return responseentity
     */
	@GetMapping ( BASE_PATH + "/ingredients/{name}" )
	public ResponseEntity getIngredient ( @PathVariable final String name ) {
		

	    final Ingredient ingr = ingredientService.findByName( name );

	    if ( null == ingr ) {
	        return new ResponseEntity( HttpStatus.NOT_FOUND );
	    }

	    return new ResponseEntity( ingr, HttpStatus.OK );
	}
	
	/**
     * Endpoint for getting all ingredients
     * @return responseentity
     */
	@GetMapping (BASE_PATH + "/ingredients")
	public List<Ingredient> getIngredients () {
		return ingredientService.findAll();
	}
	
	/**
     * Endpoint for adding an ingredient to the inventory
     * Expects an ingredient object and will either create
     * a new ingredient in the inventory if it does not already exist
     * or add to the existing ingredient of the same name
     * @param ingredient to add
     * @return responseentity
     */
	@PostMapping (BASE_PATH + "/ingredients")
	public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {
		
		final Inventory inventory = inventoryService.getInventory();
		
		if ( null == inventory.getIngredient(ingredient.getName()) ) {
			if ( inventory.getIngredients().size() < 9 ) {
				ingredientService.save(ingredient);
				inventory.addIngredient(ingredient);
				inventoryService.save(inventory);
	            return new ResponseEntity( successResponse( ingredient.getName() + " successfully created" ), HttpStatus.OK );
			}
			else {
				return new ResponseEntity(
	                    errorResponse( "Insufficient space in ingredient book for ingredient " + ingredient.getName() ),
	                    HttpStatus.INSUFFICIENT_STORAGE );
			}
		}
		else {
			ingredientService.save(ingredient);
			inventory.addIngredient(ingredient);
			inventoryService.save(inventory);
            return new ResponseEntity( successResponse( ingredient.getName() + " successfully created" ), HttpStatus.OK );
		}
		
	}
	
}
