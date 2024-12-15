package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICoffeeTest {

    @Autowired
    private MockMvc          mvc;

    @Autowired
    private RecipeService    service;

    @Autowired
    private InventoryService iService;
    
    @Autowired
	private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
    	
    	mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        final Inventory ivt = iService.getInventory();
        Ingredient coffee = new Ingredient("Coffee", 15);
        Ingredient milk = new Ingredient("Milk", 15);
        Ingredient sugar = new Ingredient("Sugar", 15);
        Ingredient chocolate = new Ingredient("Chocolate", 15);
        ivt.addIngredient(coffee);
        ivt.addIngredient(milk);
        ivt.addIngredient(sugar);
        ivt.addIngredient(chocolate);
        iService.save( ivt );

        final Recipe recipe = new Recipe();
        recipe.setName("Latte");
        recipe.setPrice( 50 );
        Ingredient coffee2 = new Ingredient("Coffee", 5);
        Ingredient milk2 = new Ingredient("Milk", 1);
        Ingredient sugar2 = new Ingredient("Sugar", 2);
        Ingredient chocolate2 = new Ingredient("Chocolate", 3);
        recipe.addIngredient(coffee2);
        recipe.addIngredient(milk2);
        recipe.addIngredient(sugar2);
        recipe.addIngredient(chocolate2);
        service.save( recipe );
    }

    @Test
    @Transactional
    public void testPurchaseBeverage1 () throws Exception {

        final String name = "Latte";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 60 ) ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.message" ).value( 10 ) );
        
        final Inventory inventory = iService.getInventory();
        Assertions.assertEquals(10, inventory.getIngredient("Coffee").getAmount());
        Assertions.assertEquals(14, inventory.getIngredient("Milk").getAmount());
        Assertions.assertEquals(13, inventory.getIngredient("Sugar").getAmount());
        Assertions.assertEquals(12, inventory.getIngredient("Chocolate").getAmount());




    }

    @Test
    @Transactional
    public void testPurchaseBeverage2 () throws Exception {
        /* Insufficient amount paid */

        final String name = "Latte";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 40 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough money paid" ) );

    }
    
    @Test
    @Transactional
    public void testNotEnoughInventory () throws Exception {
        /* Insufficient inventory */

        final String name = "Latte";
        final Inventory inventory = iService.getInventory();
        inventory.setIngredient("Coffee", 1);
        iService.save(inventory);
        

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 50 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" ) );

    }
    
    @Test
    @Transactional
    public void testNotEnoughInventory2 () throws Exception {
        /* Insufficient inventory */

        final String name = "Latte";
        Inventory inventory = iService.getInventory();
        List<Ingredient> ingredients = inventory.getIngredients();
        ingredients.remove(0);
        inventory.setIngredients(ingredients);
        iService.save(inventory);
        inventory = iService.getInventory();

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 51 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" ) );

    }

//    @Test
//    @Transactional
//    public void testPurchaseBeverage3 () throws Exception {
//        /* Insufficient inventory */
//
//        final Inventory ivt = iService.getInventory();
//        ivt.s( 0 );
//        iService.save( ivt );
//
//        final String name = "Coffee";
//
//        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
//                .content( TestUtils.asJsonString( 50 ) ) ).andExpect( status().is4xxClientError() )
//                .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" ) );
//
//    }
//    
    @Test
    @Transactional
    public void testPurchaseNullBeverage () throws Exception {
        /* Null Recipe */

        final Inventory ivt = iService.getInventory();
        iService.save( ivt );

        final String name = null;

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 50 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "No recipe selected" ) );

    }
    
    @Test
    @Transactional
    public void testPurchaseNonExistentBeverage () throws Exception {
        /* Non-Existent Recipe */

        final Inventory ivt = iService.getInventory();
//        ivt.setCoffee( 0 );
        iService.save( ivt );
        final String name = "Non-Existent Recipe";
        try {
        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 50 ) ) ).andExpect( status().is4xxClientError() );
        } catch (final IllegalArgumentException iae) {
        	//expected
        }

    }

}
