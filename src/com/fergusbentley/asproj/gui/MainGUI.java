package com.fergusbentley.asproj.gui;

import java.util.Map;
import java.util.concurrent.Callable;

import com.fergusbentley.asproj.Config;
import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.Main;
import com.fergusbentley.asproj.Resources;
import com.fergusbentley.asproj.ai.HumanBehaviour;
import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.entity.living.ActorHuman;

public class MainGUI extends GUIGrid {

	
	
	public MainGUI(Main a) {
		super(a, Config.GRID_COLS, Config.GRID_ROWS);		
	}
	
	
	public void construct() {
		
		GUIStyle blackout = new GUIStyle(app, "styles/blackout.json");
		GUIStyle panel = new GUIStyle(app, "styles/panel.json");
		GUIStyle black_panel = new GUIStyle(app, "styles/black_panel.json");
		GUIStyle trans_button = new GUIStyle(app, "styles/trans_button.json");
		GUIStyle trans_button_gold = new GUIStyle(app, "styles/trans_button_gold.json");
		
		
		GUIGrid loadScreen = new GUIGrid(app, 0, 0, 50, 32, 1, 1, 10, blackout)
				.addChild("loadingText", new GUIText(app, "Loading Assets...", 0, 0, 1, 1));
		addChild("loadScreen", loadScreen);
		
		
		GUIGrid tray = new GUIGrid(app, 41, 31, 9, 1, 9, 1)
				.addChild("maximiseSidebar", new GUIButton(app, 0, 0, 1, 1, Resources.get("button_question"), trans_button_gold)
						.assign(new Callable<Boolean>() {
							public Boolean call() {
						        getChild("sidebar").visible();
						        getChild("toolTray").asGrid().getChild("maximiseSidebar").hidden();
						        return true;
							}
						}))
				.addChild("zoomIcon", new GUIIcon(app, 1, 0, 1, 1, "img/zoom.png"))
				.addChild("zoomText", new GUIText(app, "100%", 2, 0, 3, 1))
				.addChild("fpsIcon", new GUIIcon(app, 5, 0, 1, 1, "img/fps.png"))
				.addChild("fpsText", new GUIText(app, "60fps", 6, 0, 3, 1));
		addChild("toolTray", tray);
		
		
		GUIGrid sidebar = new GUIGrid(app, 41, 5, 8, 20, 8, 20, 1, panel)
				.addChild("entityView", new GUIEntityRender(app, 1, 1, 6, 4, null, black_panel))
				.addChild("entityInfo", new GUIList(app, 1, 6, 6, 13, black_panel))
				.addChild("minimiseSidebar", new GUIButton(app, 7, 0, 1, 1, Resources.get("button_no"), trans_button)
						.assign(new Callable<Boolean>() {
						   public Boolean call() {
						        getChild("sidebar").hidden();
						        getChild("toolTray").asGrid().getChild("maximiseSidebar").visible();
						        return true;
						   }
						})).hidden().asGrid();
		addChild("sidebar", sidebar);
	}
	
	
	public void update(GameState game) {
		
		if (game.getState() == 0) {
			getChild("loadScreen").getChild("loadingText").asText().text = "Generating World";
		}
		
		if (game.getState() == 1) {
			int n = (int) Math.floor((game.time() % 1200) / 300f);
			int p = 3 - n;
			String dots = new String(new char[n]).replace("\0", ".");
			String spaces = new String(new char[p]).replace("\0", " ");
			getChild("loadScreen").getChild("loadingText").asText().text = "Generating World" + dots + spaces;
		}
		
		if (game.getState() == 2) {
			getChild("loadScreen").hidden();
			updateSidebar(game);
		}
		
		getChild("toolTray").getChild("zoomText").asText().text = (int) Math.ceil((game.getZoom() * 10)) * 10 + "%";
		getChild("toolTray").getChild("fpsText").asText().text = (int) Math.round(app.frameRate) + "fps";
	}

	
	private void updateSidebar(GameState game) {
		Entity selected = game.getSelectedEntity();
		if (selected != null) {
			this.getChild("sidebar").getChild("entityView").asEntityRender().setEntity(selected);
			GUIList infoList = this.getChild("sidebar").getChild("entityInfo").asList();
			infoList.clear();
			infoList.addLine(selected.x + ", " + selected.y);
			if (selected instanceof ActorHuman) {
				ActorHuman human = (ActorHuman) selected;
				HumanBehaviour hb = ((HumanBehaviour)human.behaviour);
				Map<String, Float> stats = hb.stats;
				infoList.addLine("H: " + stats.get("health").intValue());
				infoList.addLine("E: " + stats.get("exposure").intValue());
				infoList.addLine("F: " + stats.get("hunger").intValue());
				infoList.addLine("W: " + stats.get("thirst").intValue());
				infoList.addLine("I: " + hb.indoors.toString());
				infoList.addLine("Tasks:");
				if (hb.queue.list().size() > 0)
					for (String t : hb.queue.list()) {
						infoList.addLine("- " + t);
					}
				else infoList.addLine("- none");
			}
		}
	}
	
}
