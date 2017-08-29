package com.fergusbentley.asproj.ai;

import java.util.ArrayList;
import java.util.List;

public class TaskQueue {

	private List<ActorTask> queue;
	
	public TaskQueue() {
		this.queue = new ArrayList<ActorTask>();
	}
	
	public ActorTask current() {
		if (this.queue.size() > 0)
			return this.queue.get(0);
		return ActorTask.done;
	}
	
	public void done() {
		ActorTask current = current();
		if (this.queue.size() > 0) this.queue.remove(0);
		if (current.next != null) {
			queue.add(0, current.next);
		}
	}
	
	public void add(ActorTask t) {
		this.queue.add(t);
	}

	public List<String> list() {
		List<String> list = new ArrayList<String>();
		for (ActorTask t : this.queue) {
			list.add(t.id);
		}
		return list;
	}
}
