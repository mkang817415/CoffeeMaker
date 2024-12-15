
//package edu.ncsu.csc.CoffeeMaker.datageneration;
//
//import javax.transaction.Transactional;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import edu.ncsu.csc.CoffeeMaker.TestConfig;
//import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
//import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
//
//@ExtendWith ( SpringExtension.class )
//@EnableAutoConfiguration
//@SpringBootTest ( classes = TestConfig.class )
//public class GenerateIngredients {
//
//    @Autowired
//    private IngredientService ingredientService;
//
//    @Test
//    @Transactional
//    public void testCreateIngredients() {
//        ingredientService.deleteAll();
//    
//        final Ingredient i1 = new Ingredient( "Milk", 5 );
//
//        ingredientService.save( i1 );
//
//        final Ingredient i2 = new Ingredient( "Chocolate", 3 );
//
//        ingredientService.save( i2 );
//        
//        Assertions.assertEquals( 2, ingredientService.count() );
//
//        
//    }
//}
