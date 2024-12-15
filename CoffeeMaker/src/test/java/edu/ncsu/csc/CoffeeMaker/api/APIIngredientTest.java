package edu.ncsu.csc.CoffeeMaker.api;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIngredientTest {
	
    @Autowired
    private IngredientService service;
    
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MockMvc mvc;
    
    @Autowired
	private WebApplicationContext context;


    @BeforeEach 
    public void setup() throws UnsupportedEncodingException, Exception {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        
    }
    @Test 
    @Transactional
    public void testGetIngredient() throws Exception {

    	service.deleteAll();
    	
        mvc.perform( get( "/api/v1/ingredients/coffee" ) ).andExpect( status().isNotFound() );

        Ingredient coffee = new Ingredient("Coffee", 10);
        service.save(coffee);
        
        String ingredient = mvc.perform( get( "/api/v1/ingredients/Coffee" ) ).andExpect( status().isOk() )
        .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(ingredient.contains("Coffee"));

    }
    
    @Test 
    @Transactional 
    public void testGetIngredients() throws Exception {
   
    	mvc.perform( get( "/api/v1/ingredients/coffee" ) ).andExpect( status().isNotFound() );

    	
        
        Ingredient coffee = new Ingredient("Coffee", 10);
        service.save(coffee);
        
        String ingredient = mvc.perform( get( "/api/v1/ingredients" ) ).andExpect( status().isOk() )
        .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue(ingredient.contains("Coffee"));

    }
    
    @Test 
    @Transactional
    public void testCreateIngredient() throws Exception {
        
    	service.deleteAll();
        Inventory inventory = inventoryService.getInventory();
    	Assertions.assertTrue(0 == inventory.getIngredients().size());
    	
        Ingredient water = new Ingredient("Water", 50);
        mvc.perform( post("/api/v1/ingredients").contentType( MediaType.APPLICATION_JSON )
        		.content( TestUtils.asJsonString( water ) ) ).andExpect( status().isOk() );

        inventory = inventoryService.getInventory();
    	Assertions.assertTrue(1 == inventory.getIngredients().size());
    	Assertions.assertTrue(50 == inventory.getIngredient("Water").getAmount());
    	
        Ingredient water2 = new Ingredient("Water", 25);
        mvc.perform( post("/api/v1/ingredients").contentType( MediaType.APPLICATION_JSON )
        		.content( TestUtils.asJsonString( water2 ) ) ).andExpect( status().isOk() );
        inventory = inventoryService.getInventory();
    	Assertions.assertTrue(1 == inventory.getIngredients().size());
    	Assertions.assertTrue(75 == inventory.getIngredient("Water").getAmount());
    }

        
}
