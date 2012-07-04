package com.pope.server.recipes.service.rest.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.pope.server.jaxb.Recipe;
import com.pope.server.jaxb.Recipes;

@Path("/recipes/")
public class RecipeService {

	@GET
	@Produces("plain/text")
	@Path("/recipe")
	public Recipes getRecipes() {
		// dummy code
		Recipes recipes = new Recipes();
		Recipe recipe = new Recipe();
		recipe.setCategory("Seafood");
		recipe.setCuisine("American");
		recipe.setOccasion("Summer");
		recipe.setServes("4");
		recipe.setIngredients("Shrimp and lobster");
		recipe.setDirections("Boil and server.");
		recipes.getRecipes().add(recipe);
		return recipes;
	}
}