package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import javax.transaction.Transactional;

import org.aspectj.lang.annotation.Before;
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
public class APITest {  
	
	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST
	 * API
	 */
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;
	
	 @Autowired
	 private RecipeService    service;

	 @Autowired
	 private InventoryService iService;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup () {
	    mvc = MockMvcBuilders.webAppContextSetup( context ).build();
	}
	
    @Test
    @Transactional
    public void aPICoffeeTest () throws UnsupportedEncodingException, Exception {
    	
    	String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
    	        .andReturn().getResponse().getContentAsString();

    	/* Figure out if the recipe we want is present */
    	if ( !recipe.contains( "Mocha" ) ) {
    	    final Recipe r = new Recipe();
    	    
    	    final Inventory ivt = iService.getInventory();
            Ingredient coffee = new Ingredient("Coffee", 3);
            Ingredient milk = new Ingredient("Milk", 4);
            Ingredient sugar = new Ingredient("Sugar", 8);
            Ingredient chocolate = new Ingredient("Chocolate", 10);
            ivt.addIngredient(coffee);
            ivt.addIngredient(milk);
            ivt.addIngredient(sugar);
            ivt.addIngredient(chocolate);
            iService.save( ivt );

            Ingredient water = new Ingredient("Water", 6);
            Ingredient milk2 = new Ingredient("Milk", 2);
            Ingredient tea = new Ingredient("Tea", 4);
    	    r.setPrice( 10 );
    	    r.setName( "Mocha" );
    	    r.addIngredient(tea);
    	    r.addIngredient(milk2);
    	    r.addIngredient(water);
    	    

    	    mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
    	            .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
    	    
    	    String recipe2 = mvc.perform( get( "/api/v1/recipes" ) ).andExpect( status().isOk() )
        	        .andReturn().getResponse().getContentAsString();
    	    
    	    Assertions.assertTrue(recipe2.contains("Mocha")); 
    	    
    	}
    
    }
    
    @Test 
    @Transactional 
    public void aPIInventoryTest() throws UnsupportedEncodingException, Exception{

    	String inventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
    	        .andReturn().getResponse().getContentAsString();
    	
    	if (inventory.contains(":0")) {
    		
    		final Inventory ivt = iService.getInventory();
            Ingredient coffee = new Ingredient("Coffee", 50);
            Ingredient milk = new Ingredient("Milk", 50);
            Ingredient sugar = new Ingredient("Sugar", 50);
            Ingredient chocolate = new Ingredient("Chocolate", 50);
            ivt.addIngredient(coffee);
            ivt.addIngredient(milk);
            ivt.addIngredient(sugar);
            ivt.addIngredient(chocolate);
            iService.save( ivt );
    		
    		mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
    	            .content( TestUtils.asJsonString( ivt ) ) ).andExpect( status().isOk() );
    		
    		String inventory2 = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
        	        .andReturn().getResponse().getContentAsString();
        	
        	Assertions.assertEquals(ivt.checkIngredient("Coffee"), 50); 
        	Assertions.assertTrue(inventory2.contains("50")); 
    	}
    }
    
    @Test 
    @Transactional 
    public void aPICoffeeMakeringTest() throws UnsupportedEncodingException, Exception{
	    
	    /* Creating Recipe */
    	String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
    	        .andReturn().getResponse().getContentAsString();

    	/* Figure out if the recipe we want is present */
    	if ( !recipe.contains( "Mocha" ) ) {
    	    final Recipe r = new Recipe();
    	    
    	    final Inventory ivt = iService.getInventory();
            Ingredient coffee = new Ingredient("Coffee", 3);
            Ingredient milk = new Ingredient("Milk", 4);
            Ingredient sugar = new Ingredient("Sugar", 8);
            Ingredient chocolate = new Ingredient("Chocolate", 5);
            ivt.addIngredient(coffee);
            ivt.addIngredient(milk);
            ivt.addIngredient(sugar);
            ivt.addIngredient(chocolate);
            iService.save( ivt );
    	    
            r.addIngredient(chocolate);
            r.addIngredient(coffee);
            r.addIngredient(sugar);
            r.addIngredient(milk);
            
    	    r.setPrice( 100 );
    	    r.setName( "Mocha" );

    	    mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
    	            .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );   	    
    	}
	    
	    /* Adding inventory */
    	String inventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
    	        .andReturn().getResponse().getContentAsString();
    	
    	if (inventory.contains(":0")) {
    		final Inventory ivt = iService.getInventory();
            Ingredient coffee = new Ingredient("Coffee", 50);
            Ingredient milk = new Ingredient("Milk", 50);
            Ingredient sugar = new Ingredient("Sugar", 50);
            Ingredient chocolate = new Ingredient("Chocolate", 50);
            ivt.addIngredient(coffee);
            ivt.addIngredient(milk);
            ivt.addIngredient(sugar);
            ivt.addIngredient(chocolate);
            iService.save( ivt );
    		
    		mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
    	            .content( TestUtils.asJsonString( ivt ) ) ).andExpect( status().isOk() );
    		
    	}
	    
    	mvc.perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
	            .content( TestUtils.asJsonString( 100 ) ) ).andExpect( status().isOk() );
    	
    }

	@Test 
    @Transactional 
    public void aPIDeleteRecipeTest() throws UnsupportedEncodingException, Exception{
		String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
		.andReturn().getResponse().getContentAsString();

		Assertions.assertFalse(recipe.contains("Mocha")); 

		/* Figure out if the recipe we want is present */
		if( !recipe.contains("Mocha")){ 
			final Recipe r = new Recipe();
			final Inventory ivt = iService.getInventory();
            Ingredient coffee = new Ingredient("Coffee", 3);
            Ingredient milk = new Ingredient("Milk", 4);
            Ingredient sugar = new Ingredient("Sugar", 8);
            Ingredient chocolate = new Ingredient("Chocolate", 5);
            Ingredient coffee2 = new Ingredient("Coffee", 2);
            Ingredient milk2 = new Ingredient("Milk", 3);
            Ingredient sugar2 = new Ingredient("Sugar", 1);
            Ingredient chocolate2 = new Ingredient("Chocolate", 1);
            ivt.addIngredient(coffee);
            ivt.addIngredient(milk);
            ivt.addIngredient(sugar);
            ivt.addIngredient(chocolate);
            iService.save( ivt );
    	    
            r.addIngredient(chocolate);
            r.addIngredient(coffee);
            r.addIngredient(sugar);
            r.addIngredient(milk);
            
    	    r.setPrice( 10 );
    	    r.setName( "Mocha" );

			final Recipe r2 = new Recipe();
			
			 r2.addIngredient(chocolate2);
	         r2.addIngredient(coffee2);
	         r2.addIngredient(sugar2);
	         r2.addIngredient(milk2);
	         r2.setIngredient(chocolate2.getName(), 1);
	         r2.setIngredient(coffee2.getName(), 2);
	         r2.setIngredient(sugar2.getName(), 1);
	         r2.setIngredient(milk2.getName(), 3);
			r2.setPrice( 2 );
			r2.setName( "Matcha" );

			mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );  
					
			mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );  
		}

		String recipe2 = mvc.perform( get( "/api/v1/recipes" ) ).andExpect( status().isOk() )
				.andReturn().getResponse().getContentAsString();

		Assertions.assertTrue(recipe2.contains("Mocha"));
		Assertions.assertTrue(recipe2.contains("Matcha")); 


		/* Deleting recipe */
		mvc.perform(delete("/api/v1/recipes/Mocha").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		// mvc.perform( delete("/api/v1/recipes/Mocha")).andExpect(status().isOk());
		String recipe3 = mvc.perform( get( "/api/v1/recipes" ) ).andExpect( status().isOk() )
				.andReturn().getResponse().getContentAsString();
		Assertions.assertFalse(recipe3.contains("Mocha"));
		Assertions.assertTrue(recipe3.contains("Matcha"));
	}

}
