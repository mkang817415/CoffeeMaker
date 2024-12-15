package edu.ncsu.csc.CoffeeMaker.datageneration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipeWithIngredients {
    
    @Autowired
    private RecipeService recipeService;
    
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }
    
    
    @Test
    public void createRecipe () {
        final Recipe r1 = new Recipe();
        r1.setName( "Delicious Coffee" );
        r1.setPrice( 50 );
        r1.addIngredient( new Ingredient("Milk", 5 ) );
        r1.addIngredient( new Ingredient( "Coco", 3 ) );
        r1.addIngredient( new Ingredient( "Butter", 2 ) );
        
        recipeService.save( r1 );
        
        printRecipes();
    }
    
    private void printRecipes () {
        for ( DomainObject r : recipeService.findAll() ) {
            System.out.println( r );
        }
    }

}