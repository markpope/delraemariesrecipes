package com.pope.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
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
	private HTML ingredientsData;
	private HTML directionsData;
	private Label ingredientsLabel;
	private Label directionsLabel;
	private LoginInfo loginInfo;
	protected RecipeTO recipe;
	private MainView mainView;
	private static GroceryListDialogBox groceryList = new GroceryListDialogBox();
	private static ExportRecipesDialogBox exportRecipes = new ExportRecipesDialogBox();
	private static ImportRecipesDialogBox importRecipes = new ImportRecipesDialogBox();

	public DisplayRecipe(LoginInfo pLoginInfo, MainView pMainView) {
		loginInfo = pLoginInfo;
		mainView = pMainView;
		init();
	}

	private void init() {

		VerticalPanel verPanel = new VerticalPanel();
		this.add(verPanel);
		this.setSize("100%", "100%");

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
					recipeService.removeRecipe(recipe, new AsyncCallback<Void>() {
						public void onFailure(Throwable error) {
						}

						@Override
						public void onSuccess(Void result) {
							GWT.log("deleted recipe " + recipe.getName(), null);
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
		if (loginInfo.isAdmin()) {
			headerPanel.setWidth("200");
		} else {
			headerPanel.setWidth("100%");
		}
		headerPanel.setStyleName("headerPanel");

		Button print = new Button("Print", new ClickHandler() {
			public void onClick(ClickEvent sender) {
				Window.print();
			}
		});
		print.setStyleName("Button");
		headerPanel.add(print);

		if (loginInfo.isAdmin()) {
			Button editButton = new Button("Edit", new ClickHandler() {
				public void onClick(ClickEvent sender) {
					RecipeDialogBox recipeBox = new RecipeDialogBox(recipe);
					recipeBox.center();
					recipeBox.show();

				}
			});
			print.setStyleName("Button");
			headerPanel.add(editButton);

			Button deleteButton = new Button("Delete", new ClickHandler() {
				public void onClick(ClickEvent sender) {
					recipeService.removeRecipe(recipe, new AsyncCallback<Void>() {
						public void onFailure(Throwable error) {
						}

						@Override
						public void onSuccess(Void result) {
							GWT.log("deleted recipe " + recipe.getName(), null);
						}
					});
					mainView.onClick(null);
				}
			});
			print.setStyleName("Button");
			headerPanel.add(deleteButton);

			Button exportButton = new Button("Export", new ClickHandler() {
				public void onClick(ClickEvent sender) {
					exportRecipes.loadRecipes();
					exportRecipes.show();
				}
			});
			print.setStyleName("Button");
			headerPanel.add(exportButton);

			print.setStyleName("Button");
			headerPanel.add(deleteButton);

			Button importButton = new Button("Import", new ClickHandler() {
				public void onClick(ClickEvent sender) {
					importRecipes.show();
				}
			});
			print.setStyleName("Button");
			headerPanel.add(importButton);
		}

		verPanel.add(headerPanel);
		title = new Label();
		title.setStyleName("displayRecipeTitle");
		verPanel.add(title);
		verPanel.setCellHeight(headerPanel, "22px");

		HorizontalPanel optionsPanel = new HorizontalPanel();
		optionsPanel.setWidth("80%");
		optionsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
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
		verPanel.setCellHorizontalAlignment(optionsPanel, HasHorizontalAlignment.ALIGN_CENTER);

		VerticalPanel ingredientsPanel = new VerticalPanel();

		ingredientsLabel = new Label("Ingredients");
		directionsLabel = new Label("Directions");
		ingredientsLabel.setStyleName("displayIngDirLabel");
		directionsLabel.setStyleName("displayIngDirLabel");

		ingredientsData = new HTML();
		directionsData = new HTML();
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
				ingredientsData.setHTML("<div id='ingredients'>" + pRecipe.getIngredients().replace("\n", "<br/>") + "</div>");
				directionsData.setHTML("<div id='ingredients'>" + pRecipe.getDirections().replace("\n", "<p>") + "</div>");
			}
		});
	}

	@Override
	public void onClick(ClickEvent event) {
		groceryList.add("\n-------  " + title.getText() + "  -------\n");
		groceryList.add(recipe.getIngredients());
		groceryList.show();

	}
}
