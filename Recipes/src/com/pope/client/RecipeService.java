package com.pope.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("recipe")
public interface RecipeService extends RemoteService {
  public void addRecipe(RecipeTO pRecipe) throws NotLoggedInException;
  public void updateRecipe(RecipeTO pRecipe) throws NotLoggedInException;
  public void removeRecipe(RecipeTO pRecipe) throws NotLoggedInException;
  public RecipeTO getRecipe(String pName) throws NotLoggedInException;
  public RecipeTO[] getRecipes(int pBegRecipe, int pEndRecipe) throws NotLoggedInException;
  public RecipeTO[] getRecipesByCategory(int pBegRecipe, int pEndRecipe, String pCategory) throws NotLoggedInException;
  public RecipeTO[] getRecipesByCuisine(int pBegRecipe, int pEndRecipe, String pCuisine) throws NotLoggedInException;
  public RecipeTO[] getRecipesBySearch(int pBegRecipe, int pEndRecipe, String pSearch) throws NotLoggedInException;
  public String[] getRecipesForExport() throws NotLoggedInException;
}