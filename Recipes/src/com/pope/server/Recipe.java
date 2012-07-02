package com.pope.server;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;
import com.pope.client.RecipeTO;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Recipe implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String name;
	@Persistent
	private String category;
	@Persistent
	private String cuisine;
	@Persistent
	private String occasion;
	@Persistent
	private String serves;
	@Persistent
	private Text ingredients;
	@Persistent
	private Text directions;
	@Persistent
	private Date created;

	public Recipe() {
		this.created = new Date();

	}

	public Recipe(String pName, String pCategory, String pCuisine, String pOccasion,
			String pServes, String pIngredients, String pDirections) {
		this();
		this.name = pName.substring(0, 1).toUpperCase() + pName.substring(1);
		this.category = pCategory;
		this.cuisine = pCuisine;
		this.occasion = pOccasion;
		this.serves = pServes;
		this.ingredients = new Text(pIngredients);
		this.directions = new Text(pDirections);
	}

	public Recipe(RecipeTO pRecipe) {
		this();
		this.id = pRecipe.getId();
		this.name = pRecipe.getName();
		this.category = pRecipe.getCategory();
		this.cuisine = pRecipe.getCuisine();
		this.occasion = pRecipe.getOccasion();
		this.serves = pRecipe.getServes();
		this.ingredients = new Text(pRecipe.getIngredients());
		this.directions = new Text(pRecipe.getDirections());
	}

	public String getName() {
		return name;
	}
	
	public void setName(String pName) {
		name = pName;
	}

	public String getCuisine() {
		return cuisine;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}

	public String getIngredients() {
		return ingredients.getValue();
	}

	public void setIngredients(String ingredients) {
		this.ingredients = new Text(ingredients);
	}

	public String getDirections() {
		return directions.getValue();
	}

	public void setDirections(String directions) {
		this.directions = new Text(directions);
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

}