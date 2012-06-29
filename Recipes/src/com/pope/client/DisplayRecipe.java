package com.pope.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DisplayRecipe extends FocusPanel implements ClickHandler {

	private RecipeServiceAsync recipeService = GWT.create(RecipeService.class);
	private Label title;
	private DisplayLabel occasionLabel;
	private DisplayLabel categoryLabel;
	private DisplayLabel cuisineLabel;
	private DisplayLabel servesLabel;
	private Label ingredientsData;
	private Label directionsData;
	private Label ingredientsLabel;
	private Label directionsLabel;
	private LoginInfo loginInfo;
	protected RecipeTO recipe;
	private static GroceryListDialogBox groceryList = new GroceryListDialogBox();
	private static ExportRecipesDialogBox exportRecipes = new ExportRecipesDialogBox();

	public DisplayRecipe(LoginInfo pLoginInfo) {
		System.out.println("I'm HERE!");
		loginInfo = pLoginInfo;
		init();
	}

	private void init() {

		VerticalPanel verPanel = new VerticalPanel();
		this.add(verPanel);
		this.setSize("100%", "100%");
		System.out.println("IN INIT");

		// if (loginInfo.isAdmin()) {
		addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				RecipeDialogBox recipeBox = new RecipeDialogBox(recipe);
				recipeBox.center();
				recipeBox.show();
				GWT.log("Got the event code " + event.getCharCode(), null);
				if (event.getCharCode() == 5) {
					GWT.log("Got the edit <Alt>+O command", null);
					// RecipeDialogBox recipeBox = new RecipeDialogBox(recipe);
					// recipeBox.center();
					// recipeBox.show();
				} else if (event.getCharCode() == 4) {
					GWT.log("Got a delete <Alt>+L command", null);
					recipeService.removeRecipe(recipe,
							new AsyncCallback<Void>() {
								public void onFailure(Throwable error) {
								}

								@Override
								public void onSuccess(Void result) {
									GWT.log("deleted recipe "
											+ recipe.getName(), null);
								}
							});
				} else {
					System.out.println("Got a " + event.getCharCode());
				}
			}
		});
		// }

		verPanel.add(new RecipeHeader());
		// verPanel.setSize("100%", "100%");
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		headerPanel.setSpacing(1);
		headerPanel.setStyleName("headerPanel");
		headerPanel.setWidth("100%");

		Button print = new Button("Print", new ClickHandler() {
			public void onClick(ClickEvent sender) {
				Window.print();
			}
		});
		print.setStyleName("Button");
		headerPanel.add(print);

		if (loginInfo.isAdmin()) {
			Button edit = new Button("Edit", new ClickHandler() {
				public void onClick(ClickEvent sender) {
					RecipeDialogBox recipeBox = new RecipeDialogBox(recipe);
					recipeBox.center();
					recipeBox.show();

				}
			});
			print.setStyleName("Button");
			headerPanel.add(edit);

			Button delete = new Button("Delete", new ClickHandler() {
				public void onClick(ClickEvent sender) {
					recipeService.removeRecipe(recipe,
							new AsyncCallback<Void>() {
								public void onFailure(Throwable error) {
								}

								@Override
								public void onSuccess(Void result) {
									GWT.log("deleted recipe "
											+ recipe.getName(), null);
								}
							});

				}
			});
			print.setStyleName("Button");
			headerPanel.add(delete);

			Button export = new Button("Export", new ClickHandler() {
				public void onClick(ClickEvent sender) {
					exportRecipes.loadRecipes();
					exportRecipes.show();
					// recipeService.getRecipesForExport(
					// new AsyncCallback<String[]>() {
					// public void onFailure(Throwable error) {
					// }
					//
					// @Override
					// public void onSuccess(String[] result) {
					// }
					// });

				}
			});
			print.setStyleName("Button");
			headerPanel.add(export);
		}

		verPanel.add(headerPanel);
		title = new Label();
		title.setStyleName("displayRecipeTitle");
		verPanel.add(title);
		verPanel.setCellHeight(headerPanel, "22px");

		HorizontalPanel optionsPanel = new HorizontalPanel();
		optionsPanel.setWidth("80%");
		optionsPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		categoryLabel = new DisplayLabel("Category: ");
		cuisineLabel = new DisplayLabel("Cusine: ");
		occasionLabel = new DisplayLabel("Occasion: ");
		servesLabel = new DisplayLabel("Serves: ");
		optionsPanel.add(categoryLabel);
		optionsPanel.add(cuisineLabel);
		optionsPanel.add(occasionLabel);
		optionsPanel.add(servesLabel);
		verPanel.add(optionsPanel);
		verPanel.setCellHeight(optionsPanel, "30px");
		verPanel.setCellHorizontalAlignment(optionsPanel,
				HasHorizontalAlignment.ALIGN_CENTER);

		VerticalPanel ingredientsPanel = new VerticalPanel();

		ingredientsLabel = new Label("Ingredients");
		directionsLabel = new Label("Directions");
		ingredientsLabel.setStyleName("displayIngDirLabel");
		directionsLabel.setStyleName("displayIngDirLabel");

		ingredientsData = new Label();
		directionsData = new Label();
		ingredientsData.setStyleName("displayIngDirData");
		directionsData.setStyleName("displayIngDirData");

		HorizontalPanel ingredientsLabelPanel = new HorizontalPanel();
		ingredientsLabelPanel.add(ingredientsLabel);

		Image grocery = new Image("static/images/grocerylist.gif");
		grocery.addClickHandler(this);
		ingredientsLabelPanel.add(grocery);

		ingredientsPanel.add(ingredientsLabelPanel);

		ingredientsPanel.add(ingredientsData);
		ingredientsPanel.add(directionsLabel);
		ingredientsPanel.add(directionsData);
		verPanel.add(ingredientsPanel);
		verPanel.setCellWidth(ingredientsPanel, "50%");
	}

	public void display(String pRecipe) {
		GWT.log("Display Recipe: " + pRecipe, null);
		recipeService.getRecipe(pRecipe, new AsyncCallback<RecipeTO>() {
			public void onFailure(Throwable error) {
			}

			@Override
			public void onSuccess(RecipeTO pRecipe) {
				recipe = pRecipe;
				title.setText(pRecipe.getName());
				occasionLabel.setData(pRecipe.getOccasion());
				cuisineLabel.setData(pRecipe.getCuisine());
				categoryLabel.setData(pRecipe.getCategory());
				servesLabel.setData(pRecipe.getServes());
				ingredientsData.setText(pRecipe.getIngredients());
				directionsData.setText(pRecipe.getDirections());
			}
		});
	}

	@Override
	public void onClick(ClickEvent event) {
		groceryList.add("\n-------  " + title.getText() + "  -------\n");
		groceryList.add(ingredientsData.getText());
		groceryList.show();

	}
}
