package edu.ncsu.csc.CoffeeMaker.unit;

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

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( new Ingredient("Buttercup", 100 ) );
        ivt.addIngredient( new Ingredient("Coco", 100 ) );
        ivt.addIngredient( new Ingredient("Milk", 100 ) );
        ivt.addIngredient( new Ingredient("Choco", 100 ) );

        inventoryService.save( ivt );
    }
    
    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient("Buttercup", 5 ) );
        recipe.addIngredient( new Ingredient("Coco", 2 ) );
        recipe.addIngredient( new Ingredient("Milk", 1 ) );
        recipe.addIngredient( new Ingredient("Choco", 2 ) );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 95, (int) i.checkIngredient("Buttercup") );
        Assertions.assertEquals( 98, (int) i.checkIngredient("Coco") );
        Assertions.assertEquals( 99, (int) i.checkIngredient("Milk") );
        Assertions.assertEquals( 98, (int) i.checkIngredient("Choco") );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();


        ivt.addIngredient( new Ingredient("Buttercup", 100 ) );
        ivt.addIngredient( new Ingredient("Coco", 50 ) );
        ivt.addIngredient( new Ingredient("Milk", 20 ) );
        ivt.addIngredient( new Ingredient("Choco", 10 ) );


        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 200, (int) ivt.checkIngredient("Buttercup"),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 150, (int) ivt.checkIngredient("Coco"),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 120, (int) ivt.checkIngredient("Milk"),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 110, (int) ivt.checkIngredient("Choco"),
                "Adding to the inventory should result in correctly-updated values chocolate" );

    }


    @Test 
    @Transactional
    public void useInventoryTest(){
        final Inventory ivt = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "CocoButter" );
        recipe.addIngredient( new Ingredient("Buttercup", 100 ) );
        recipe.addIngredient( new Ingredient("Coco", 50 ) );
        recipe.addIngredient( new Ingredient("Milk", 20 ) );
        recipe.addIngredient( new Ingredient("Choco", 10 ) );     
        recipe.setPrice( 5 );

        ivt.useIngredients( recipe );   

        Assertions.assertEquals( 0, (int) ivt.checkIngredient("Buttercup") );
        Assertions.assertEquals( 50, (int) ivt.checkIngredient("Coco") );
        Assertions.assertEquals( 80, (int) ivt.checkIngredient("Milk") );
        Assertions.assertEquals( 90, (int) ivt.checkIngredient("Choco") );
    }

    @Test
    @Transactional
    public void testEnoughIngredients () {
    	final Inventory ivt = inventoryService.getInventory();
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient("Buttercup", 5 ) );
        recipe.addIngredient( new Ingredient("Coco", 2 ) );
        recipe.addIngredient( new Ingredient("Milk", 1 ) );
        recipe.addIngredient( new Ingredient("Choco", 2 ) );

        Assertions.assertTrue(ivt.enoughIngredients(recipe));
        
        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Delicious Not-Coffee" );
        recipe2.addIngredient( new Ingredient("Buttercup", 100 ) );
        recipe2.addIngredient( new Ingredient("Coco", 200 ) );
        recipe2.addIngredient( new Ingredient("Milk", 300 ) );
        recipe2.addIngredient( new Ingredient("Choco", 100 ) );
        recipe2.setPrice( 5 );

        Assertions.assertFalse(ivt.enoughIngredients(recipe2));

    }
    
    @Test
    @Transactional
    public void testGetSetId () {
    	final Inventory ivt = inventoryService.getInventory();
    	ivt.setId(9L);
    	try {
    		Assertions.assertEquals(9L, ivt.getId());
    	} catch (final IllegalArgumentException iae) {
    		Assertions.fail("The get or set id does not work.");
    	}
    }
    
    @Test
    @Transactional
    public void testCheckIngredients() {
    	final Inventory ivt = inventoryService.getInventory();
    	
    	try {
    		Assertions.assertEquals(100, ivt.checkIngredient("Buttercup"));
    	} catch (final IllegalArgumentException iae) {
    		Assertions.fail("The check ingredients does not work.");
    	}
    	try {
    		Assertions.assertEquals(100, ivt.checkIngredient("Coco"));
    	} catch (final IllegalArgumentException iae) {
    		Assertions.fail("The check ingredients does not work.");
    	}
    	try {
    		Assertions.assertEquals(100, ivt.checkIngredient("Milk"));
    	} catch (final IllegalArgumentException iae) {
    		Assertions.fail("The check ingredients does not work.");
    	}
    	try {
    		Assertions.assertEquals(100, ivt.checkIngredient("Choco"));
    	} catch (final IllegalArgumentException iae) {
    		Assertions.fail("The check ingredients does not work.");
    	}
    }
    

}