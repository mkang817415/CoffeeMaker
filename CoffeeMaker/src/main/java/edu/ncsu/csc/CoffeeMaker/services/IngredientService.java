package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

/**
 * The ingredient service class - go between ingredient repository
 * and ingredient controller
 */
@Component 
@Transactional
public class IngredientService extends Service {

	/**
	 * The repository of ingredients,
	 * autowired by Spring
	 */
    @Autowired
    private IngredientRepository ingredientRepository;

    /**
     * Override the JpaRepository
     */
    @Override
    protected JpaRepository<Ingredient, Long> getRepository () {
        return ingredientRepository;
    }
    
    /**
     * Find an ingredient with the provided name
     * 
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public Ingredient findByName ( final String name ) {
        return ingredientRepository.findByName( name );
    }


	
}
