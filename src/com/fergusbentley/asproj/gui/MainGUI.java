package com.fergusbentley.asproj.gui;

import java.util.Map;
import java.util.concurrent.Callable;

import com.fergusbentley.asproj.Config;
import com.fergusbentley.asproj.GameState;
import com.fergusbentley.asproj.Main;
import com.fergusbentley.asproj.Resources;
import com.fergusbentley.asproj.ai.BehaviourHuman;
import com.fergusbentley.asproj.entity.Entity;
import com.fergusbentley.asproj.entity.living.ActorHuman;

import processing.core.PConstants;

public class MainGUI extends GUIGrid implements PConstants {

	private boolean pauseButtonActive;
	
	public MainGUI(Main a) {
		super(a, Config.GRID_COLS, Config.GRID_ROWS);		
	}
	
	
	public void construct(GameState game) {
		
		GUIStyle blackout = new GUIStyle(app, "styles/blackout.json");
		GUIStyle panel = new GUIStyle(app, "styles/panel.json");
		GUIStyle black_panel = new GUIStyle(app, "styles/black_panel.json");
		GUIStyle light_panel = new GUIStyle(app, "styles/light_panel.json");
		GUIStyle trans_button = new GUIStyle(app, "styles/trans_button.json");
		GUIStyle trans_button_gold = new GUIStyle(app, "styles/trans_button_gold.json");
		GUIStyle fade = new GUIStyle(app, "styles/fade.json");
		GUIStyle unfocused = new GUIStyle(app, "styles/unfocused.json");
		
		
		addChild("mainMenu", new GUIGrid(app, 0, 0, 50, 32, 50, 32, blackout)
				.addChild("mainTitle", new GUIText(app, "Demiurge", 20, 10, 10, 2))
				.addChild("mainGenerate", new GUIButton(app, 20, 14, 10, 2, "Generate New World", light_panel)
						.assign(new Callable<Boolean>() {
						    public Boolean call() {
						    	game.setState(GameState.START_GENERATION);
						    	return true;
						    }
						})
					)
				.addChild("mainLoad", new GUIButton(app, 20, 17, 10, 2, "Load Existing World", light_panel))
				.addChild("pauseExit", new GUIButton(app, 20, 20, 10, 2, "Quit to Desktop", light_panel)
						.assign(new Callable<Boolean>() {
						    public Boolean call() {
						    	app.exit();
						        return true;
						    }
						}))
				.zIndex(1000)
				.asGrid());
		
		addChild("loadScreen", new GUIGrid(app, 0, 0, 50, 32, 1, 1, blackout)
				.zIndex(100).asGrid()
				.addChild("loadingText", new GUIText(app, "Loading Assets...", 0, 0, 1, 1)));
		
		addChild("powersTray", new GUIGrid(app, 1, 16, 4, 15, 6, 24, panel)
				.addChild("powersTitle", new GUIText(app, "Powers", 0, 1, 6, 2))
				.addChild("powerFire", new GUIButton(app, 1, 4, 4, 4, Resources.get("icon/button_fire"), light_panel))
				.addChild("powerRain", new GUIButton(app, 1, 9, 4, 4, Resources.get("icon/button_rain"), light_panel))
				.addChild("powerTree", new GUIButton(app, 1, 14, 4, 4, Resources.get("icon/button_tree"), light_panel))
				.addChild("powerNoTree", new GUIButton(app, 1, 19, 4, 4, Resources.get("icon/button_notree"), light_panel)));
		
		addChild("toolTray", new GUIGrid(app, 41, 31, 9, 1, 9, 1)
				.addChild("maximiseSidebar", new GUIButton(app, 0, 0, 1, 1, Resources.get("icon/button_question"), trans_button_gold)
						.assign(new Callable<Boolean>() {
							public Boolean call() {
								getChild("sidebar").visible();
						        getChild("toolTray").asGrid().getChild("maximiseSidebar").hidden();
						        return true;
							}
						}))
				.addChild("zoomIcon", new GUIIcon(app, 1, 0, 1, 1, "icon/zoom"))
				.addChild("zoomText", new GUIText(app, "100%", 2, 0, 3, 1))
				.addChild("fpsIcon", new GUIIcon(app, 5, 0, 1, 1, "icon/fps"))
				.addChild("fpsText", new GUIText(app, "60fps", 6, 0, 3, 1)));
		
		addChild("sidebar", new GUIGrid(app, 41, 5, 8, 20, 8, 20, panel)
				.addChild("entityView", new GUIEntityRender(app, 1, 1, 6, 4, null, black_panel))
				.addChild("entityInfo", new GUIList(app, 1, 6, 6, 13, black_panel))
				.addChild("minimiseSidebar", new GUIButton(app, 7, 0, 1, 1, Resources.get("icon/button_no"), trans_button)
						.assign(new Callable<Boolean>() {
							public Boolean call() {
								getChild("sidebar").hidden();
								getChild("toolTray").asGrid().getChild("maximiseSidebar").visible();
								return true;
							}
						}))
				.hidden()
				.asGrid());
		
		addChild("pauseMenu", new GUIGrid(app, 0, 0, 50, 32, 50, 32, fade)
				.addChild("pausePanel", new GUIPanel(app, 19, 8, 12, 17))
				.addChild("pauseTitle", new GUIText(app, "Game Paused", 20, 9, 10, 2, black_panel))
				.addChild("pauseResume", new GUIButton(app, 20, 13, 10, 2, "Resume", light_panel)
						.assign(new Callable<Boolean>() {
						    public Boolean call() {
						    	if (pauseButtonActive) {
						        	pauseButtonActive = false;
							        getChild("pauseMenu").hidden();
							        getChild("pauseButton").visible();
							        getChild("resumeButton").hidden();
						        }
						        return true;
						    }
						})
						.bind(TAB)
					)
				.addChild("pauseSave", new GUIButton(app, 20, 16, 10, 2, "Save", light_panel))
				.addChild("pauseMenuExit", new GUIButton(app, 20, 19, 10, 2, "Quit to Main Menu", light_panel)
						.assign(new Callable<Boolean>() {
						    public Boolean call() {
						    	getChild("exitMenuWarning").visible();
						        return true;
						    }
						}))
				.addChild("pauseDesktopExit", new GUIButton(app, 20, 22, 10, 2, "Quit to Desktop", light_panel)
						.assign(new Callable<Boolean>() {
						    public Boolean call() {
						    	getChild("exitDesktopWarning").visible();
						        return true;
						    }
						}))
				.hidden()
				.zIndex(80)
				.asGrid());
		
		addChild("pauseButton", new GUIButton(app, 1, 1, 1, 1, Resources.get("icon/button_pause"))
				.assign(new Callable<Boolean>() {
				    public Boolean call() {
				        if (!pauseButtonActive) {
				        	pauseButtonActive = true;
					        getChild("pauseMenu").visible();
					        getChild("pauseButton").hidden();
					        getChild("resumeButton").visible();
				        }
				        return true;
				    }
				})
				.zIndex(90));
		
		addChild("resumeButton", new GUIButton(app, 1, 1, 1, 1, Resources.get("icon/button_play"))
				.assign(new Callable<Boolean>() {
				    public Boolean call() {
				    	if (pauseButtonActive) {
				        	pauseButtonActive = false;
					        getChild("pauseMenu").hidden();
					        getChild("pauseButton").visible();
					        getChild("resumeButton").hidden();
				        }
				        return true;
				    }
				})
				.hidden()
				.zIndex(90));
		
		addChild("exitDesktopWarning", new GUIGrid(app, 0, 0, 50, 32, 50, 32, fade)
				.addChild("exitPanel", new GUIPanel(app, 13, 12, 24, 7))
				.addChild("exitText1", new GUIText(app, "Are you sure you want to exit the game?", 15, 13, 20, 1))
				.addChild("exitText2", new GUIText(app, "Any unsaved progress will be lost.", 15, 14, 20, 1, unfocused))
				.addChild("exitConfirm", new GUIButton(app, 15, 16, 10, 2, "Yes", light_panel)
						.assign(new Callable<Boolean>() {
							public Boolean call() {
								app.exit();
								return true;
							}
						})
						.bind(ENTER))
				.addChild("exitDeny", new GUIButton(app, 26, 16, 10, 2, "No", light_panel)
						.assign(new Callable<Boolean>() {
							public Boolean call() {
								getChild("exitDesktopWarning").hidden();
								return true;
							}
						})
						.bind(TAB))
				.zIndex(85)
				.hidden());
		
		addChild("exitMenuWarning", new GUIGrid(app, 0, 0, 50, 32, 50, 32, fade)
				.addChild("exitPanel", new GUIPanel(app, 13, 12, 24, 7))
				.addChild("exitText1", new GUIText(app, "Are you sure you want to quit to the main menu?", 15, 13, 20, 1))
				.addChild("exitText2", new GUIText(app, "Any unsaved progress will be lost.", 15, 14, 20, 1, unfocused))
				.addChild("exitConfirm", new GUIButton(app, 15, 16, 10, 2, "Yes", light_panel)
						.assign(new Callable<Boolean>() {
							public Boolean call() {
								game.setState(GameState.MAIN_MENU);
								return true;
							}
						})
						.bind(ENTER))
				.addChild("exitDeny", new GUIButton(app, 26, 16, 10, 2, "No", light_panel)
						.assign(new Callable<Boolean>() {
							public Boolean call() {
								getChild("exitMenuWarning").hidden();
								return true;
							}
						})
						.bind(TAB))
				.zIndex(85)
				.hidden());
	}
	
	
	public void update(GameState game) {
		
		if (game.getState() == GameState.MAIN_MENU) {
			getChild("mainMenu").visible = true;
		}
		
		else if (game.getState() == GameState.START_GENERATION) {
			getChild("loadScreen").getChild("loadingText").asText().text = "Generating World";
		}
		
		else if (game.getState() == GameState.GENERATION_SCREEN) {
			int n = (int) Math.floor((game.time() % 1200) / 300f);
			int p = 3 - n;
			String dots = new String(new char[n]).replace("\0", ".");
			String spaces = new String(new char[p]).replace("\0", " ");
			getChild("loadScreen").getChild("loadingText").asText().text = "Generating World" + dots + spaces;
		}
		
		else if (game.getState() == GameState.IN_GAME) {
			getChild("loadScreen").hidden();
			updateSidebar(game);
		}
		
		game.setPaused(pauseButtonActive);
		
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
				BehaviourHuman hb = ((BehaviourHuman)human.behaviour);
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
