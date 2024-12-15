package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;


/**
 * Class to represent an Ingredient present in the Coffee Maker
 */
@Entity
public class Ingredient extends DomainObject {
	
	/** Ingredient ID */
	@Id
	@GeneratedValue
	private Long id; 
	
	/** The name of ingredient */
	private String name;
	
	/** The amount of this ingredient present in the inventory or required for a recipe */
	@Min ( 0 )
	private Integer amount; 


	
	/**
	 * Make a new Ingredient
	 * 
	 * @param name of ingredient
	 * @param amount of ingredient
	 */
	public Ingredient(String name, Integer amount) {
		super();
		this.name = name;
		this.amount = amount;
	}
	
	/**
	 * Make a new Ingredient - no params
	 */
	public Ingredient() {
		
	}
	
	/**
	 * Get the recipe ID
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Get the amount of this ingredient object
	 * @return amount
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * set the amount of this ingredient object
	 * @param amount to set
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * set the id for this ingredient object
	 * @param id to set
	 */
	@SuppressWarnings("unused")
	private void setId(final Long id) {
		this.id = id;
	}

	/**
	 * get the name of this ingredient object
	 * @return name as a string
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of this ingredient object
	 * @param name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * to string method
	 */
	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", name=" + name + ", amount=" + amount + "]";
	}


}
