package com.pope.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DelraeMarie implements EntryPoint, ValueChangeHandler {

	private MainView mainView = null;
	private static DisplayRecipe displayRecipe = null;
	LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access the DelraeMarie's Receipe Application.");
	private Anchor signInLink = new Anchor("Sign In");

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		try {

			// Check login status using login service.
			LoginServiceAsync loginService = GWT.create(LoginService.class);
			loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
				public void onFailure(Throwable error) {
				}

				public void onSuccess(LoginInfo result) {
					loginInfo = result;
					if (loginInfo.isLoggedIn()) {
						if (mainView == null) {
							mainView = new MainView(loginInfo);
							if (displayRecipe == null) {
								displayRecipe = new DisplayRecipe(loginInfo, mainView);
							}
							mainView.loadRecipes(null);
						}
						RootPanel.get("stockList").add(mainView);
					} else {
						loadLogin();
					}
				}
			});
			History.addValueChangeHandler(this);
			History.fireCurrentHistoryState();
		} catch (Exception e) {
			GWT.log(e.getMessage(), e);
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent pEvent) {
		if (mainView != null) {
			String historyToken = pEvent.getValue().toString();
			if ((historyToken != null) && (historyToken.startsWith("recipe="))) {
				displayRecipe.display(historyToken.substring(historyToken.indexOf("=") + 1));
				RootPanel.get("stockList").remove(0);
				RootPanel.get("stockList").add(displayRecipe);
			} else {
				mainView.loadRecipes(historyToken);
				RootPanel.get("stockList").remove(0);
				RootPanel.get("stockList").add(mainView);
			}
		}
	}

	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("stockList").add(loginPanel);
	}

}
