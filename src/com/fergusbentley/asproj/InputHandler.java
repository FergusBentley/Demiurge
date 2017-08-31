package com.fergusbentley.asproj;

import java.util.HashMap;
import java.util.Map;

import com.fergusbentley.asproj.gui.GUIButton;

public class InputHandler {

	private static Map<String, GUIButton> buttonBinds = new HashMap<String, GUIButton>();
	
	public static void addBind(char key, GUIButton element) {
		buttonBinds.put("" + key, element);
	}
	
	public static void handleKeyPress(char key) {
		GUIButton button = buttonBinds.get("" + key);
		if (button != null) {
			button.click();
		}
	}
	
}
