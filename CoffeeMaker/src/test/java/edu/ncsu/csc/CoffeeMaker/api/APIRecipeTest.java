package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    @Autowired
    private RecipeService service;

    @Autowired
    private MockMvc       mvc;

    @Autowired
    private WebApplicationContext context;
    /*
     Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }


    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        Ingredient coffee = new Ingredient("Coffee", 10);
        Ingredient milk = new Ingredient("Milk", 5);
        Ingredient sugar = new Ingredient("Sugar", 3);
        Ingredient chocolate = new Ingredient("Chocolate", 3);
        
        r.addIngredient(milk);
        r.addIngredient(coffee);
        r.addIngredient(sugar);
        r.addIngredient(chocolate);
        
        
        
        r.setPrice( 10 );
        r.setName( "Mocha" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }
    
    
    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        Ingredient coffee = new Ingredient("Coffee", 10);
        Ingredient milk = new Ingredient("Milk", 4);
        Ingredient sugar = new Ingredient("Sugar", 3);
        Ingredient chocolate = new Ingredient("Chocolate", 5);
        
        recipe.addIngredient(milk);
        recipe.addIngredient(coffee);
        recipe.addIngredient(sugar);
        recipe.addIngredient(chocolate);
        
        recipe.setIngredient(milk.getName(), 1);
        recipe.setIngredient(coffee.getName(), 10);
        recipe.setIngredient(sugar.getName(), 2);
        recipe.setIngredient(chocolate.getName(), 3);

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }
    
    @Test 
    @Transactional 
    public void testGetRecipe() throws Exception {
    	service.deleteAll(); 
    	
    	mvc.perform( get( "/api/v1/recipes/coffee" ) ).andExpect( status().isNotFound() );
    	
    	Recipe r = new Recipe(); 
    	r.setName("coffee"); 
    	r.setPrice(5);
    	Ingredient i = new Ingredient("water", 10);
    	r.addIngredient(i);
    	
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
        .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );        
    	
    	String coffee = mvc.perform( get( "/api/v1/recipes/coffee" ) ).andDo( print() ).andExpect( status().isOk() )
    	        .andReturn().getResponse().getContentAsString();
    	
    	Assertions.assertTrue(coffee.contains("coffee"));
    	
    	
    	
    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {
        service.deleteAll();

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        //Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final Recipe r1 = new Recipe();
        
        r1.setName( "A recipe" );
        Ingredient coffee = new Ingredient("Coffee", 10);
        Ingredient milk = new Ingredient("Milk", 4);
        Ingredient sugar = new Ingredient("Sugar", 3);
        Ingredient chocolate = new Ingredient("Chocolate", 5);
        
        r1.addIngredient(milk);
        r1.addIngredient(coffee);
        r1.addIngredient(sugar);
        r1.addIngredient(chocolate);
        service.save( r1 );
        
        final Recipe r2 = new Recipe();
        
        r2.setName( "Delicious Not-Coffee" );
        Ingredient coffee2 = new Ingredient("Coffee", 8);
        Ingredient milk2 = new Ingredient("Milk", 5);
        Ingredient sugar2 = new Ingredient("Sugar", 2);
        Ingredient chocolate2 = new Ingredient("Chocolate", 4);
        r2.addIngredient(milk2);
        r2.addIngredient(coffee2);
        r2.addIngredient(sugar2);
        r2.addIngredient(chocolate2);
        
        
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 2, service.findAll().size(), "There should two recipe in the CoffeeMaker" );
        
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isConflict() );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {
        service.deleteAll();

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        
        final Recipe r1 = new Recipe();
        
        r1.setName( "Delicious Not-Coffee" );
        Ingredient coffee = new Ingredient("Coffee", 10);
        Ingredient milk = new Ingredient("Milk", 4);
        Ingredient sugar = new Ingredient("Sugar", 3);
        Ingredient chocolate = new Ingredient("Chocolate", 5);
        
        r1.addIngredient(milk);
        r1.addIngredient(coffee);
        r1.addIngredient(sugar);
        r1.addIngredient(chocolate);
        service.save( r1 );
        
        final Recipe r2 = new Recipe();
        r2.setName("Coffee");
        Ingredient coffee1 = new Ingredient("Coffee", 10);
        Ingredient milk1 = new Ingredient("Milk", 4);
        Ingredient sugar1 = new Ingredient("Sugar", 3);
        Ingredient chocolate1 = new Ingredient("Chocolate", 5);
        r2.addIngredient(milk1);
        r2.addIngredient(coffee1);
        r2.addIngredient(sugar1);
        r2.addIngredient(chocolate1);
        
        r2.setIngredient(chocolate1.getName(), 8);
        service.save( r2 );
        
        final Recipe r3 = new Recipe();
        r3.setName("Coffee");
        Ingredient coffee2 = new Ingredient("Coffee", 10);
        Ingredient milk2 = new Ingredient("Milk", 4);
        Ingredient sugar2 = new Ingredient("Sugar", 3);
        Ingredient chocolate2 = new Ingredient("Chocolate", 5);
        r3.addIngredient(milk2);
        r3.addIngredient(coffee2);
        r3.addIngredient(sugar2);
        r3.addIngredient(chocolate2);
        r3.setIngredient(milk2.getName(), 6);
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = new Recipe();
        r4.setName("Latte");
        Ingredient coffee3 = new Ingredient("Coffee", 10);
        Ingredient milk3 = new Ingredient("Milk", 4);
        Ingredient sugar3 = new Ingredient("Sugar", 3);
        Ingredient chocolate3 = new Ingredient("Chocolate", 5);
        r4.addIngredient(milk3);
        r4.addIngredient(coffee3);
        r4.addIngredient(sugar3);
        r4.addIngredient(chocolate3);
        r4.setIngredient(coffee3.getName(), 2);

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }

    @Test 
    @Transactional
    public void testDeleteRecipe () throws Exception{
        service.deleteAll();

        mvc.perform( delete("/api/v1/recipes/coffee").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());  

        final Recipe r = new Recipe();
        r.setName("Coffee"); 
        r.setPrice(10); 
        Ingredient coffee = new Ingredient("Coffee", 10);
        Ingredient milk = new Ingredient("Milk", 4);
        Ingredient sugar = new Ingredient("Sugar", 3);
        Ingredient chocolate = new Ingredient("Chocolate", 5);

        r.addIngredient(coffee);
        r.addIngredient(milk);
        r.addIngredient(sugar);
        r.addIngredient(chocolate);

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
        .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );        
        
        Assertions.assertEquals(1, service.findAll().size());
        Assertions.assertEquals("Coffee", service.findByName("Coffee").getName());

        mvc.perform( delete("/api/v1/recipes/coffee").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());  


        Assertions.assertEquals(0, service.findAll().size());
    }

    @Test
    @Transactional
    public void testEditRecipe () throws Exception {
    	service.deleteAll();
        Recipe emptyRecipe = new Recipe(); 


        mvc.perform( put( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
            .content( TestUtils.asJsonString( emptyRecipe ) ) ).andExpect( status().isNotFound() );

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r = new Recipe();
        r.setName("Coffee"); 
        r.setPrice(10); 
        Ingredient coffee = new Ingredient("Coffee", 10);
        Ingredient milk = new Ingredient("Milk", 4);
        Ingredient sugar = new Ingredient("Sugar", 3);
        Ingredient chocolate = new Ingredient("Chocolate", 5);

        r.addIngredient(coffee);
        r.addIngredient(milk);
        r.addIngredient(sugar);
        r.addIngredient(chocolate);

        service.save(r);
        
        Assertions.assertEquals(1, service.findAll().size());
        Assertions.assertEquals("Coffee", service.findByName("Coffee").getName());


        final Recipe r2 = new Recipe();
        r2.setName("New");
        r2.setPrice(10);
        Ingredient coffee2 = new Ingredient("Coffee", 10);
        Ingredient milk2 = new Ingredient("Milk", 4);
        Ingredient sugar2 = new Ingredient("Sugar", 3);
        Ingredient chocolate2 = new Ingredient("Chocolate", 5);
        r2.addIngredient(chocolate2);
        r2.addIngredient(milk2);
        r2.addIngredient(sugar2);
        r2.addIngredient(coffee2);


        mvc.perform( put( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
            .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals(1, service.findAll().size());
        Assertions.assertEquals("New", service.findByName("New").getName());
        Assertions.assertNull(service.findByName("Coffee"));
    }    
}