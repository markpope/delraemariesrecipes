package com.pope.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class DisplayLabel extends Composite {

	Label dataText = new Label();
	Label labelText = new Label();

	public DisplayLabel(String pLabel) {
		HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(6);
		initWidget(panel);

		labelText.addStyleName("displayLabelLabel");
		labelText.setText(pLabel);
		panel.add(labelText);

		dataText.addStyleName("displayLabelData");
		panel.add(dataText);
	}

	public void setData(String pData) {
		dataText.setText(pData);
	}

}
