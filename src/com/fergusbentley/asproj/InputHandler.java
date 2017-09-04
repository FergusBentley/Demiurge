package com.fergusbentley.asproj;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class InputHandler {

	private static Map<Integer, Callable<Boolean>> binds = new HashMap<Integer, Callable<Boolean>>();
	
	public static void addBind(int key, Callable<Boolean> callback) {
		binds.put(key, callback);
	}
	
	public static void handleKeyPress(int key) {
		Callable<Boolean> callback = binds.get(key);
		if (callback != null) {
			try {
				callback.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
