package com.pope.client;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;




public class RecipeTO implements Serializable {
	
	private Long id;
	private String name;
	private String category;
	private String cuisine;
	private String occasion;
	private String serves;
	private String ingredients;
	private String directions;
	private Date created;

	public RecipeTO() {

	}

	public RecipeTO(@NotNull String pName, String pCategory, String pCuisine,
			String pOccasion, String pServes, String pIngredients, String pDirections) {
		this();
		this.name = pName.substring(0, 1).toUpperCase() + pName.substring(1);
		this.category = pCategory;
		this.cuisine = pCuisine;
		this.occasion = pOccasion;
		this.serves = pServes;
		this.ingredients = pIngredients;
		this.directions = pDirections;
	}

	public RecipeTO(Long pId, @NotNull String pName, String pCategory, String pCuisine,
			String pOccasion, String pServes, String pIngredients, String pDirections) {
		this();
		this.id = pId;
		this.name = pName.substring(0, 1).toUpperCase() + pName.substring(1);
		this.category = pCategory;
		this.cuisine = pCuisine;
		this.occasion = pOccasion;
		this.serves = pServes;
		this.ingredients = pIngredients;
		this.directions = pDirections;
	}


	public String getName() {
		return name;
	}

	public String getCuisine() {
		return cuisine;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getDirections() {
		return directions;
	}

	public void setDirections(String directions) {
		this.directions = directions;
	}

	public String getOccasion() {
		return occasion;
	}

	public void setOccasion(String occasion) {
		this.occasion = occasion;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getCreated() {
		return created;
	}

	public String getServes() {
		return serves;
	}

	public void setServes(String serves) {
		this.serves = serves;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

}