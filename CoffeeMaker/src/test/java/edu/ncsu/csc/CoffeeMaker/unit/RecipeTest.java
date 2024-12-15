package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.crypto.spec.RC2ParameterSpec;
import javax.validation.ConstraintViolationException;

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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient("Buttercup", 100 ) );
        r1.addIngredient( new Ingredient("Coco", 100 ) );
        r1.addIngredient( new Ingredient("Milk", 100 ) );
        r1.addIngredient( new Ingredient("Choco", 100 ) );

        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient("Buttercup", 100 ) );
        r2.addIngredient( new Ingredient("Coco", 100 ) );
        r2.addIngredient( new Ingredient("Milk", 100 ) );
        r2.addIngredient( new Ingredient("Choco", 100 ) );

        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe();
        r1.setName(name);

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe(); 
        r1.setName( name );


        service.save( r1 );

        Assertions.assertNotNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe(); 
        r1.setName( name );


        service.save( r1 );

        Assertions.assertNotNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );


    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        service.save( r1 );
        final Recipe r2 = new Recipe();
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe(); 
        service.save( r1 );
        final Recipe r2 = new Recipe(); 
        service.save( r2 );
        final Recipe r3 = new Recipe(); 
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe(); 
        service.save( r1 );
        final Recipe r2 = new Recipe(); 
        service.save( r2 );
        final Recipe r3 = new Recipe(); 
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe(); 
        r1.setName( "Coffee" );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }
    
    
    @Test
    @Transactional
    public void testEditRecipe () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe(); 
        r1.setName( "Coffee" );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }
    
    @Test
    public void testEquals() {
    	
    	
    	Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final Recipe r1 = new Recipe(); 

        final Recipe r2 = new Recipe();
        r2.setName("Coffee");
        
    	Assertions.assertFalse(r1 ==null);
    	Object obj = new Object();
    	Assertions.assertFalse(r1.equals(obj));
        Assertions.assertFalse(r1.equals(r2)); 

        service.save(r2); 
        final Recipe retrieved = service.findByName( "Coffee" );
        Assertions.assertTrue(r2.equals(retrieved));
    	
    }
    
    @Test
    public void testToString() {
    	
    	Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final Recipe r1 = new Recipe(); 
        r1.setName("Coffee");

        service.save(r1);
        final Recipe retrieved = service.findByName("Coffee");
    	Assertions.assertEquals("Coffee", retrieved.toString());
        
    }
    
    @Test
    public void testHashCode() {
    	
    	Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final Recipe r1 = new Recipe(); 
        r1.setName("Coffee");
        service.save(r1);
        final Recipe retrieved = service.findByName("Coffee");
    	Assertions.assertEquals(2023803915, retrieved.hashCode());
    }

}
