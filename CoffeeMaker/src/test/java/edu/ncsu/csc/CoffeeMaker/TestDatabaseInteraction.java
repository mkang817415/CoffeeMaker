package edu.ncsu.csc.CoffeeMaker;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {

    @Autowired
    private RecipeService recipeService;

    @Test
    @Transactional
    public void testRecipes () {
        recipeService.deleteAll();

        final Recipe r = new Recipe();

        r.setName( "Special Drink" );

        r.addIngredient( new Ingredient("Milk", 5 ) );
        r.addIngredient( new Ingredient( "Coco", 3 ) );
        r.addIngredient( new Ingredient( "Butter", 2 ) );

        r.setPrice( 2 );

        recipeService.save( r );

        final List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();

        Assertions.assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        /*
         * Note we are _not_ using the `Recipe.equals(Object)` method here
         * because it only checks the name!
         */
        Assertions.assertEquals( r.getName(), dbRecipe.getName() );
        Assertions.assertEquals( r.getPrice(), dbRecipe.getPrice() );
        Assertions.assertEquals( r.getIngredient("Milk"), dbRecipe.getIngredient("Milk"));
        Assertions.assertEquals( r.getIngredient("Coco"), dbRecipe.getIngredient("Coco") );
        Assertions.assertEquals( r.getIngredient("Butter"), dbRecipe.getIngredient("Butter"));
        Assertions.assertEquals( r.getPrice(), dbRecipe.getPrice() );

        final Recipe dbRecipeByName = recipeService.findByName( "Special Drink" );

        Assertions.assertEquals( r.getIngredient("Milk"), dbRecipe.getIngredient("Milk"));

        dbRecipe.setPrice( 15 );
        recipeService.save( dbRecipe );

        Assertions.assertEquals( 1, recipeService.count() );

        Assertions.assertEquals( 15, (int) ( (Recipe) recipeService.findAll().get( 0 ) ).getPrice() );

    }

    @Test
    @Transactional
    public void deleteAllRecipeTest(){
        recipeService.deleteAll(); 
        final Recipe r1 = new Recipe();
        r1.setName( "Shake" );
        r1.addIngredient( new Ingredient("Milk", 5 ) );
        r1.addIngredient( new Ingredient( "Coco", 3 ) );
        r1.addIngredient( new Ingredient( "Butter", 2 ) );
        r1.setPrice( 10);
        recipeService.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "FrogJuice" );
        r2.addIngredient( new Ingredient("Buttercup", 10 ) );
        r2.addIngredient( new Ingredient( "Popcorn", 2 ) );
        r2.addIngredient( new Ingredient( "Seamoss", 4) );
        r2.setPrice( 1);
        recipeService.save( r2 );

        final List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();
        Assertions.assertEquals(2, dbRecipes.size());
        Assertions.assertEquals(r1, dbRecipes.get(0));
        Assertions.assertEquals(r2, dbRecipes.get(1)); 
        
        /* Deleting Recipe from  */
        recipeService.deleteAll();
        final List<Recipe> dbRecipes2 = (List<Recipe>) recipeService.findAll();
        Assertions.assertEquals(0, dbRecipes2.size());
    }

    @Test
    @Transactional
    public void deleteRecipeTest(){
        recipeService.deleteAll(); 
        final Recipe r1 = new Recipe();
        r1.setName( "Shake" );
        r1.addIngredient( new Ingredient("Buttercup", 10 ) );
        r1.addIngredient( new Ingredient( "Popcorn", 2 ) );
        r1.addIngredient( new Ingredient( "Seamoss", 4) );
        r1.setPrice( 10);
        recipeService.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "FrogJuice" );
        r2.addIngredient( new Ingredient("Milk", 5 ) );
        r2.addIngredient( new Ingredient( "Coco", 3 ) );
        r2.addIngredient( new Ingredient( "Butter", 2 ) );
        r2.setPrice( 1);
        recipeService.save( r2 );

        final List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();
        Assertions.assertEquals(2, dbRecipes.size());
        Assertions.assertEquals(r1, dbRecipes.get(0));
        
        /* Deleting Recipe from  */
        recipeService.delete(r2);
        final List<Recipe> dbRecipes2 = (List<Recipe>) recipeService.findAll();
        Assertions.assertEquals(1, dbRecipes2.size());
        Assertions.assertEquals(r1, dbRecipes2.get(0)); 
    }
}
