package com.pope.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RecipeServiceAsync {
  public void addRecipe(RecipeTO pRecipe, AsyncCallback<Void> async);
  public void updateRecipe(RecipeTO pRecipe, AsyncCallback<Void> async);
  public void removeRecipe(RecipeTO pRecipe, AsyncCallback<Void> async);
  public void getRecipes(int pBegRecipe, int pEndRecipe, AsyncCallback<RecipeTO[]> async);
  public void getRecipesByCategory(int pBegRecipe, int pEndRecipe, String pCategory, AsyncCallback<RecipeTO[]> async);
  public void getRecipesByCuisine(int pBegRecipe, int pEndRecipe, String pCuisine, AsyncCallback<RecipeTO[]> async);
  public void getRecipesBySearch(int pBegRecipe, int pEndRecipe, String pSearch, AsyncCallback<RecipeTO[]> async);
  public void getRecipe(String pRecipe, AsyncCallback<RecipeTO> async);
  public void getRecipesForExport(AsyncCallback<String[]> async);
  public void importRecipes(String pXML, AsyncCallback async);
}