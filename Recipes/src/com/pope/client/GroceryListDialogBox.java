package com.pope.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GroceryListDialogBox extends DialogBox {
	
	
	private TextArea list = new TextArea();
	

	public GroceryListDialogBox() {
		init();
	}

	private void init() {

		setPopupPosition(100, 100);
		setModal(true);
		setAnimationEnabled(true);
		ensureDebugId("cwDialogBox");
		setText("Groceery List");

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
		
		Button printButton = new Button("Print", new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.print();
			}
		});

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		buttonPanel.add(printButton);
		buttonPanel.add(closeButton);
		dialogContents.add(buttonPanel);
		
	}

	public void add(String pItems) {
		list.setText(list.getText() + "\n" + pItems);
	}
}
