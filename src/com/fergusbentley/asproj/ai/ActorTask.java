package com.fergusbentley.asproj.ai;

public class ActorTask {

	public static final ActorTask done = new ActorTask("done");
	public static final ActorTask interact = new ActorTask("interact");
	public static final ActorTask navigate = new ActorTask("navigate");
	public static final ActorTask navigateInteract = new ActorTask("navigate", interact);
	public static final ActorTask seekShelter = new ActorTask("seekShelter", navigateInteract);
	
	public static final ActorTask gatherResources = new ActorTask("gatherResources");
	public static final ActorTask build = new ActorTask("build");
	
	public static final ActorTask exitAbode = new ActorTask("exitAbode");
	
	public static final ActorTask explore = new ActorTask("explore");
	
	
	public String id;
	public ActorTask next;
	
	// Single following task
	private ActorTask(String id, ActorTask next) {
		this.id = id;
		this.next = next;
	}
	
	// Terminal task
	private ActorTask(String id) {
		this.id = id;
		this.next = null;
	}
	
}
