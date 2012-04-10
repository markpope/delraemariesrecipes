package com.pope.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class RecipeHeader extends HorizontalPanel {

	private static ClickHandler handler;
	private static LoginInfo loginInfo;
	private Image banner = new Image("static/images/delraemariesrecipes.gif");

	public RecipeHeader(ClickHandler pHandler, LoginInfo pLoginInfo) {
		super();
		this.setWidth("100%");
		banner.setHeight("110px");
		banner.setWidth("670px");
		banner.addClickHandler(pHandler);
		handler = pHandler;
		add(banner);
		loginInfo = pLoginInfo;
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		add(new Label(pLoginInfo.getNickname() + " | "));
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		add(new Anchor("Sign out", pLoginInfo.getLogoutUrl()));
	}

	public RecipeHeader() {
		this(handler, loginInfo);
	}
}
