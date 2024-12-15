package edu.ncsu.csc.CoffeeMaker.api;

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
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class APIInventoryTest {
	
    @Autowired
    private InventoryService service;

    @Autowired
    private MockMvc       mvc;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach 
    public void setup() throws UnsupportedEncodingException, Exception {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();


        final Inventory ivt = service.getInventory();
        Assertions.assertEquals(0, ivt.getIngredients().size());
        Ingredient espresso = new Ingredient("Espresso", 100);
        Ingredient cream = new Ingredient("Cream", 50);
        Ingredient syrup = new Ingredient("Syrup", 30);
        Ingredient water = new Ingredient("Water", 75);
        
        ivt.addIngredient(espresso);
        ivt.addIngredient(cream);
        ivt.addIngredient(syrup);
        ivt.addIngredient(water);
        service.save(ivt);
//        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
//        .content( TestUtils.asJsonString( ivt ) ) ).andExpect( status().isOk() );
    }
    
    @Test
    @Transactional
    public void testGetInventory() throws Exception {
    	
    	mvc.perform( get( "/api/v1/inventory" )).andExpect( status().isOk() )
    			.andDo( print() )
                .andExpect( jsonPath( "$.ingredients[0].name" ).value( "Espresso" ) )
                .andExpect( jsonPath( "$.ingredients[0].amount" ).value( 100 ) )
                .andExpect( jsonPath( "$.ingredients[2].name" ).value( "Syrup" ) )
                .andExpect( jsonPath( "$.ingredients[2].amount" ).value( 30 ) )
                .andExpect( jsonPath( "$.ingredients[3].name" ).value( "Water" ) )
                .andExpect( jsonPath( "$.ingredients[3].amount" ).value( 75 ) );
    	
    	
    }
    
    
    @Test
    @Transactional
    public void ensureInventory() throws UnsupportedEncodingException, Exception {
        service.deleteAll();
        
        final Inventory i = new Inventory();
        Ingredient coffee = new Ingredient("Coffee", 10);
        Ingredient milk = new Ingredient("Milk", 5);
        Ingredient sugar = new Ingredient("Sugar", 3);
        Ingredient chocolate = new Ingredient("Chocolate", 3);
        i.addIngredient(coffee);
        i.addIngredient(chocolate);
        i.addIngredient(sugar);
        i.addIngredient(milk);

        service.save(i); 
    }

//    @Test
//    @Transactional
//    public void testInventoryAPI() throws UnsupportedEncodingException, Exception {
//        String inventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
//    	        .andReturn().getResponse().getContentAsString();
//    	
//    	Assertions.assertFalse(inventory.contains(":0")); 
//
//        Inventory newInventory = new Inventory(); 
//        Ingredient coffee = new Ingredient("Coffee", 2);
//        Ingredient milk = new Ingredient("Milk", 2);
//        Ingredient sugar = new Ingredient("Sugar", 2);
//        Ingredient chocolate = new Ingredient("Chocolate", 2);
//        
//        newInventory.addIngredient(coffee);
//        newInventory.addIngredient(chocolate);
//        newInventory.addIngredient(sugar);
//        newInventory.addIngredient(milk);
//
//        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
//        .content( TestUtils.asJsonString( newInventory ) ) ).andExpect( status().isOk() );
//        
//        String inventory2 = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
//    	        .andReturn().getResponse().getContentAsString();
//        System.out.println(); 
//        System.out.println(inventory2); 
//
//        Assertions.assertTrue(inventory2.contains(":12"));
//    }
}


