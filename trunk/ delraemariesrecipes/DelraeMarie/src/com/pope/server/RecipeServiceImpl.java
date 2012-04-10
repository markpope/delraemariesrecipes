package com.pope.server;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pope.client.NotLoggedInException;
import com.pope.client.RecipeService;
import com.pope.client.RecipeTO;

public class RecipeServiceImpl extends RemoteServiceServlet implements RecipeService {

	private static final Logger LOG = Logger.getLogger(RecipeServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	public void addRecipe(RecipeTO pRecipe) throws NotLoggedInException {
		checkLoggedIn();
		byte[] bytes = pRecipe.getIngredients().getBytes();
		System.out.println(bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			System.out.print(new Byte(bytes[i]).intValue() + ", ");
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(convert(pRecipe));
		} finally {
			pm.close();
		}
	}

	@Override
	public void updateRecipe(RecipeTO pRecipe) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Recipe.class);
			q.setFilter("name == pName");
			q.declareParameters("String pName");
			List<Recipe> recipes = (List<Recipe>) q.execute(pRecipe.getName());
			Recipe recipe = recipes.get(0);
			recipe.setCategory(pRecipe.getCategory());
			recipe.setCuisine(pRecipe.getCuisine());
			recipe.setDirections(pRecipe.getDirections());
			recipe.setIngredients(pRecipe.getIngredients());
			recipe.setOccasion(pRecipe.getOccasion());
			recipe.setServes(pRecipe.getServes());
		} finally {
			pm.close();
		}

	}

	public void removeRecipe(RecipeTO pRecipe) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			long deleteCount = 0;
			Query q = pm.newQuery(Recipe.class);
			q.setFilter("name == pName");
			q.declareParameters("String pName");
			List<Recipe> qRecipes = (List<Recipe>) q.execute(pRecipe.getName());
			for (Recipe recipe : qRecipes) {
				GWT.log("Deleting " + recipe.getName(), null);
				pm.deletePersistent(recipe);
			}
		} finally {
			pm.close();
		}
	}

	public RecipeTO getRecipe(String pName) throws NotLoggedInException {
		System.out.println("getRecipe(" + pName + ")");
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Recipe.class);
			q.setFilter("name == pName");
			q.declareParameters("String pName");
			List<Recipe> recipes = (List<Recipe>) q.execute(pName);
			return (RecipeTO) convert(recipes.get(0));
		} finally {
			pm.close();
		}
	}

	/**
	 * Convienence method
	 * @param pRecipe
	 * @return
	 */
	private Recipe convert(RecipeTO pRecipe) {
		return new Recipe(pRecipe);
	}

	/**
	 * Convienence method
	 * @param pRecipe
	 * @return
	 */
	private RecipeTO convert(Recipe pRecipe) {
		return new RecipeTO(pRecipe.getId(), pRecipe.getName(), pRecipe.getCategory(), pRecipe.getCuisine(), pRecipe
				.getOccasion(), pRecipe.getServes(), pRecipe.getIngredients(), pRecipe.getDirections());
	}

	public RecipeTO[] getRecipes(int pBegRecipe, int pEndRecipe) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<RecipeTO> recipes = new ArrayList<RecipeTO>();
		try {
			Query q = pm.newQuery(Recipe.class);
			q.setOrdering("name");
			q.setRange(pBegRecipe, pEndRecipe);
			List<Recipe> qRecipes = (List<Recipe>) q.execute();
			for (Recipe recipe : qRecipes) {
				recipes.add(convert(recipe));
			}
		} finally {
			pm.close();
		}
		return (RecipeTO[]) recipes.toArray(new RecipeTO[0]);
	}

	public RecipeTO[] getRecipesByCategory(int pBegRecipe, int pEndRecipe, String pCategory)
			throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<RecipeTO> recipes = new ArrayList<RecipeTO>();
		try {
			Query q = pm.newQuery(Recipe.class);
			q.setFilter("category == pCategory");
			q.declareParameters("String pCategory");
			q.setOrdering("name");
			q.setRange(pBegRecipe, pEndRecipe);
			List<Recipe> qRecipes = (List<Recipe>) q.execute(pCategory);
			for (Recipe recipe : qRecipes) {
				recipes.add(convert(recipe));
			}
		} finally {
			pm.close();
		}
		return (RecipeTO[]) recipes.toArray(new RecipeTO[0]);
	}

	public RecipeTO[] getRecipesByCuisine(int pBegRecipe, int pEndRecipe, String pCuisine) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<RecipeTO> recipes = new ArrayList<RecipeTO>();
		try {
			Query q = pm.newQuery(Recipe.class);
			q.setFilter("cuisine == pCuisine");
			q.declareParameters("String pCuisine");
			q.setOrdering("name");
			q.setRange(pBegRecipe, pEndRecipe);
			List<Recipe> qRecipes = (List<Recipe>) q.execute(pCuisine);
			for (Recipe recipe : qRecipes) {
				recipes.add(convert(recipe));
			}
		} finally {
			pm.close();
		}
		return (RecipeTO[]) recipes.toArray(new RecipeTO[0]);
	}

	public RecipeTO[] getRecipesBySearch(int pBegRecipe, int pEndRecipe, String pSearch) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<RecipeTO> recipes = new ArrayList<RecipeTO>();
		try {
			Query q = pm.newQuery(Recipe.class);
			q.setOrdering("name");
			List<Recipe> qRecipes = (List<Recipe>) q.execute(pSearch);
			for (Recipe recipe : qRecipes) {
				StringTokenizer st = new StringTokenizer(pSearch);
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					if (recipe.getIngredients().contains(token) || recipe.getDirections().contains(token)) {
						recipes.add(convert(recipe));
						break;
					}
				}
			}
		} finally {
			pm.close();
		}
		return (RecipeTO[]) recipes.toArray(new RecipeTO[0]);
	}

	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}

	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}