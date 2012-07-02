package com.pope.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImportRecipesDialogBox extends DialogBox {
	
	private RecipeServiceAsync recipeService = GWT.create(RecipeService.class);
	private TextArea list = new TextArea();

	public ImportRecipesDialogBox() {
		init();
	}

	private void init() {

		setPopupPosition(0, 0);
		setModal(true);
		setAnimationEnabled(true);
		ensureDebugId("cwDialogBox");
		setText("Import Recipes as XML");

		list.setWidth("800px");
		list.setHeight("500px");
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		setWidget(dialogContents);
		dialogContents.add(list);

		// Add a close button at the bottom of the dialog
		Button closeButton = new Button("Close", new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		Button importButton = new Button("Import", new ClickHandler() {
			public void onClick(ClickEvent event) {
				recipeService.importRecipes(list.getText(), new ImportRecipesAsyncCallback());
			}
		});

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		buttonPanel.add(importButton);
		buttonPanel.add(closeButton);
		dialogContents.add(buttonPanel);
		
	}

	private void add(String pItems) {
		list.setText(list.getText() + "\n" + pItems);
	}
	
	private class ImportRecipesAsyncCallback implements AsyncCallback {

		@Override
		public void onFailure(Throwable caught) {
			add(caught.getMessage());
		}

		@Override
		public void onSuccess(Object result) {
			
		}
	}
}
