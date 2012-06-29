package com.pope.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainView extends VerticalPanel implements ClickHandler {

	private final DialogBox recipeDialogBox = new RecipeDialogBox(this);
	private FlexTable recipesFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private Label lastUpdatedLabel = new Label();
	private LoginInfo loginInfo = null;
	private Anchor signOutLink = new Anchor("Sign Out");
	private RecipeServiceAsync recipeService = GWT.create(RecipeService.class);
	private String navTag = "recipes";
	private int displayRange = 0;
	private int recipesReturned = 0;
	private static final int DISPALY_RANGE = 4;
	private Hyperlink previous;
	private Label range;
	private Hyperlink next;

	public MainView(LoginInfo pLoginInfo) {
		loginInfo = pLoginInfo;
		try {
			init();
		} catch (Exception e) {
			GWT.log(e.getMessage(), e);
		}
	}

	private void init() {
		// Set up sign out hyperlink.
		RecipeHeader header = new RecipeHeader(this, loginInfo);
		add(header);
		setCellHeight(header, "110px");
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setStyleName("headerPanel");
		headerPanel.setWidth("100%");

		HorizontalPanel recipeMgmtPanel = new HorizontalPanel();
		recipeMgmtPanel.setStyleName("recipeMgmtPanel");
		Button newRecipe = new Button("New Recipe", new ClickHandler() {
			public void onClick(ClickEvent sender) {
				recipeDialogBox.clear();
				recipeDialogBox.center();
				recipeDialogBox.show();
			}
		});
		newRecipe.setStyleName("Button");
		recipeMgmtPanel.add(newRecipe);
		final TextBox searchText = new TextBox();
		Button search = new Button("Search", new ClickHandler() {
			public void onClick(ClickEvent sender) {
				loadRecipes("search=" + searchText.getText() + "?view=0");
			}
		});
		search.setStyleName("Button");
		recipeMgmtPanel.add(search);
		searchText.setStyleName("Button");
		recipeMgmtPanel.add(searchText);

		AbsolutePanel recipeNavPanel = new AbsolutePanel();
		recipeNavPanel.setStyleName(".gwt-DockPanel");
		recipeNavPanel.setSize("220px", "25px");
		previous = new Hyperlink("< Previous", "recipes?view=" + displayRange);
		previous.setStyleName("hyperlink");
		previous.setVisible(false);
		range = new Label("" + (displayRange + 1) + "-" + (displayRange + DISPALY_RANGE) + " recipes");
		range.setStyleName("hyperlink");
		next = new Hyperlink("Next >", "recipes?view=" + (displayRange + DISPALY_RANGE));
		next.setStyleName("hyperlink");
		recipeNavPanel.setStyleName("recipeNavPanel");
		recipeNavPanel.add(previous, 0, 0);
		recipeNavPanel.add(range, 80, 0);
		recipeNavPanel.add(next, 150, 0);

		headerPanel.add(recipeMgmtPanel);
		headerPanel.add(recipeNavPanel);
		headerPanel.setCellHorizontalAlignment(recipeNavPanel, HorizontalPanel.ALIGN_RIGHT);
		recipesFlexTable.getRowFormatter().addStyleName(0, "recipeTableHeader");
		recipesFlexTable.setSize("100%", "0%");
		recipesFlexTable.setText(0, 0, "Recipe");
		recipesFlexTable.setText(0, 1, "Category");
		recipesFlexTable.setText(0, 2, "Cuisine");
		
		
		// Assemble Main panel.
		this.setSize("100%", "100%");
		this.add(headerPanel);
		this.setCellHeight(headerPanel, "22px");
		this.add(recipesFlexTable);
		this.add(addPanel);
		this.add(lastUpdatedLabel);

	}

	void loadRecipes() {
		loadRecipes(null);
	}

	/**
	 * Recipe hyperlink is recipe=Fried Chicken Category hyperlink is
	 * category=Poultry?view=0 Cuisine hyperlink is cuisine=American?view=0
	 * newer hyper link is category=Poultry?view=4 or recipes?view=4
	 * 
	 * 
	 * @param pToken
	 */
	void loadRecipes(String pToken) {
		GWT.log("MainView loadRecipes(" + pToken + ")", null);
		try {
			if (pToken == null) {
				recipeService.getRecipes(displayRange, (displayRange + DISPALY_RANGE), new LoadRecipesAsyncCallback());
			} else {
				displayRange = Integer.parseInt(pToken.substring(pToken.lastIndexOf('=') + 1));
				if (pToken.startsWith("recipes")) {
					recipeService.getRecipes(displayRange, (displayRange + DISPALY_RANGE),
							new LoadRecipesAsyncCallback());
					navTag = "recipes";
				} else {
					String category = pToken.substring(pToken.indexOf('=') + 1, pToken.indexOf('?'));
					if (pToken.startsWith("category")) {
						recipeService.getRecipesByCategory(displayRange, (displayRange + DISPALY_RANGE), category,
								new LoadRecipesAsyncCallback());
						navTag = "category=" + category;
					} else if (pToken.startsWith("cuisine")) {
						recipeService.getRecipesByCuisine(displayRange, (displayRange + DISPALY_RANGE), category,
								new LoadRecipesAsyncCallback());
						navTag = "cuisine=" + category;
					} else if (pToken.startsWith("search")) {
						recipeService.getRecipesBySearch(displayRange, (displayRange + DISPALY_RANGE), category,
								new LoadRecipesAsyncCallback());
						navTag = "search=" + category;
					}
				}
			}

		} catch (Exception e) {
			GWT.log(e.getMessage(), e);
		}

	}

	private class LoadRecipesAsyncCallback implements AsyncCallback<RecipeTO[]> {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess(RecipeTO[] recipes) {
			clearRecipeTable();
			int rowCount = 1;
			for (RecipeTO recipe : recipes) {
				recipesFlexTable.setWidget(rowCount, 0, new Hyperlink(recipe.getName(), "recipe=" + recipe.getName()));
				recipesFlexTable.setWidget(rowCount, 1, new Hyperlink(recipe.getCategory(), "category="
						+ recipe.getCategory() + "?view=0"));
				recipesFlexTable.setWidget(rowCount, 2, new Hyperlink(recipe.getCuisine(), "cuisine="
						+ recipe.getCuisine() + "?view=0"));
				rowCount = rowCount + 1;
			}
			recipesReturned = recipes.length;

			if (displayRange == 0) {
				previous.setVisible(false);
			} else {
				previous.setTargetHistoryToken(navTag + "?view=" + (displayRange - DISPALY_RANGE));
				previous.setVisible(true);
			}
			if (recipesReturned < DISPALY_RANGE) {
				next.setVisible(false);
			} else {
				next.setTargetHistoryToken(navTag + "?view=" + (displayRange + DISPALY_RANGE));
				next.setVisible(true);
			}
			range.setText("" + (displayRange + 1) + "-"
					+ (displayRange + ((recipesReturned == 0) ? DISPALY_RANGE : recipesReturned)) + " recipes");
		}
	}

	private void clearRecipeTable() {
		try {
			int rows = recipesFlexTable.getRowCount();
			for (int row = 1; row < rows; row++) {
				recipesFlexTable.removeRow(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		displayRange = 0;
		navTag = "recipes";
		loadRecipes();
		RootPanel.get("stockList").remove(0);
		RootPanel.get("stockList").add(this);

	}

}
