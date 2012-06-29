package com.pope.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RecipeDialogBox extends DialogBox {

	private RecipeServiceAsync recipeService = GWT.create(RecipeService.class);
	private TextBox nameTextBox;
	private TextArea directions;
	private TextArea ingredients;
	private static MainView mainView;
	private ListBox category = new ListBox();
	private ListBox cuisine = new ListBox();
	private ListBox occasion = new ListBox();
	private ListBox serves = new ListBox();
	private boolean recipeExists = false;

	public RecipeDialogBox() {
		init();
	}

	public RecipeDialogBox(MainView pMainView) {
		this();
		mainView = pMainView;
	}

	public RecipeDialogBox(RecipeTO recipe) {
		recipeExists = true;
		init();
		edit(recipe);
	}

	private void init() {

		setModal(true);
		setAnimationEnabled(true);
		ensureDebugId("cwDialogBox");
		setText("New Recipe");

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		setWidget(dialogContents);

		HorizontalPanel namePanel = new HorizontalPanel();
		namePanel.setSpacing(6);
		namePanel.add(new Label("Name:"));
		nameTextBox = new TextBox();
		nameTextBox.setWidth("600px");
		if (recipeExists) {
			nameTextBox.setEnabled(false);
		}
		namePanel.add(nameTextBox);

		HorizontalPanel categoriesPanel = new HorizontalPanel();
		categoriesPanel.setSpacing(6);
		categoriesPanel.add(new Label("Category"));

		category.addItem("Appetizer");
		category.addItem("Beverage");
		category.addItem("Breakfast");
		category.addItem("Bread");
		category.addItem("Condiment");
		category.addItem("Beef");
		category.addItem("Dessert");
		category.addItem("Fish");
		category.addItem("Grain");
		category.addItem("Legume");
		category.addItem("Other");
		category.addItem("Pasta");
		category.addItem("Pork");
		category.addItem("Poultry");
		category.addItem("Sauce");
		category.addItem("Salad");
		category.addItem("Seafood");
		category.addItem("Soup");
		category.addItem("Vegetable");
		categoriesPanel.add(category);

		categoriesPanel.add(new Label("Cuisine"));

		cuisine.addItem("American");
		cuisine.addItem("Asian");
		cuisine.addItem("Cuban");
		cuisine.addItem("French");
		cuisine.addItem("German");
		cuisine.addItem("Greek");
		cuisine.addItem("Indian");
		cuisine.addItem("Italian");
		cuisine.addItem("Mexican");
		cuisine.addItem("Middle East");
		cuisine.addItem("Russian");
		cuisine.addItem("Southern");
		cuisine.addItem("Other");
		categoriesPanel.add(cuisine);

		categoriesPanel.add(new Label("Occasion"));

		occasion.addItem("Party");
		occasion.addItem("Easter");
		occasion.addItem("Spring");
		occasion.addItem("Summer");
		occasion.addItem("Thanksgiving");
		occasion.addItem("Fall");
		occasion.addItem("Christmas");
		occasion.addItem("Winter");
		occasion.addItem("Other");
		categoriesPanel.add(occasion);

		categoriesPanel.add(new Label("Serves"));

		serves.addItem("1");
		serves.addItem("2");
		serves.addItem("3");
		serves.addItem("4");
		serves.addItem("5");
		serves.addItem("6");
		serves.addItem("7");
		serves.addItem("8");
		serves.addItem("9");
		serves.addItem("10");
		serves.addItem("12");
		serves.addItem("15");
		serves.setItemSelected(1, true);
		categoriesPanel.add(serves);

		ingredients = new TextArea();
		ingredients.setTitle("Ingredients");
		ingredients.setHeight("200px");
		ingredients.setWidth("600px");

		Label ingredientsLabel = new Label("Ingredients");

		directions = new TextArea();
		directions.setTitle("Directions");
		directions.setHeight("200px");
		directions.setWidth("600px");

		Label directionsLabel = new Label("Directions");

		FlexTable grid = new FlexTable();
		grid.setWidget(0, 0, ingredientsLabel);
		grid.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.setWidget(0, 1, ingredients);
		dialogContents.add(namePanel);
		grid.setWidget(1, 0, directionsLabel);
		grid.setWidget(1, 1, directions);
		dialogContents.setCellHeight(namePanel, "50px");
		dialogContents.add(categoriesPanel);
		dialogContents.setCellHeight(namePanel, "50px");
		dialogContents.add(grid);

		// Add a close button at the bottom of the dialog
		Button closeButton = new Button("Close", new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		Button createButton = new Button("Create Recipe", new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log("Creating Recipe: " + nameTextBox.getText(), null);
				addRecipe(new RecipeTO(nameTextBox.getText(), category.getValue(category.getSelectedIndex()), cuisine
						.getValue(cuisine.getSelectedIndex()), occasion.getValue(occasion.getSelectedIndex()), serves
						.getValue(serves.getSelectedIndex()), ingredients.getText(), directions.getText()));
				hide();
			}
		});

		Button saveButton = new Button("Save Recipe", new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log("Updating Recipe: " + nameTextBox.getText(), null);
				updateRecipe(new RecipeTO(nameTextBox.getText(), category.getValue(category.getSelectedIndex()),
						cuisine.getValue(cuisine.getSelectedIndex()), occasion.getValue(occasion.getSelectedIndex()),
						serves.getValue(serves.getSelectedIndex()), ingredients.getText(), directions.getText()));
				hide();
				mainView.loadRecipes();
				RootPanel.get("stockList").remove(0);
				RootPanel.get("stockList").add(mainView);
				History.newItem("hoho");
			}
		});

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		// Display update or create button if recipe exists and is being edited
		if (recipeExists) {
			buttonPanel.add(saveButton);
		} else {
			buttonPanel.add(createButton);
		}
		buttonPanel.add(closeButton);
		dialogContents.add(buttonPanel);

		// Return the dialog box
	}

	public void clear() {
		nameTextBox.setText("");
		directions.setText("");
		ingredients.setText("");
	}

	void addRecipe(final RecipeTO pRecipe) {
		recipeService.addRecipe(pRecipe, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
			}

			@Override
			public void onSuccess(Void result) {
				mainView.loadRecipes();
			}
		});
	}

	void updateRecipe(final RecipeTO pRecipe) {
		recipeService.updateRecipe(pRecipe, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
			}

			@Override
			public void onSuccess(Void result) {
				mainView.loadRecipes();
			}
		});
	}

	public void edit(RecipeTO pRecipe) {
		nameTextBox.setText(pRecipe.getName());
		nameTextBox.setReadOnly(false);
		for (int i = 0; i < category.getItemCount(); i++) {
			if (pRecipe.getCategory().equals(category.getItemText(i))) {
				category.setSelectedIndex(i);
			}
		}
		for (int i = 0; i < cuisine.getItemCount(); i++) {
			if (pRecipe.getCuisine().equals(cuisine.getItemText(i))) {
				cuisine.setSelectedIndex(i);
			}
		}
		for (int i = 0; i < occasion.getItemCount(); i++) {
			if (pRecipe.getOccasion().equals(occasion.getItemText(i))) {
				occasion.setSelectedIndex(i);
			}
		}
		for (int i = 0; i < serves.getItemCount(); i++) {
			if (pRecipe.getServes().equals(serves.getItemText(i))) {
				serves.setSelectedIndex(i);
			}
		}
		ingredients.setText(pRecipe.getIngredients());
		directions.setText(pRecipe.getDirections());

	}
}
