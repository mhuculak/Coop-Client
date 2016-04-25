package coop.player;

import coop.actions.*;
// import org.codehaus.jackson.annotate.JsonIgnoreProperties;

// @JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerAction {
	private String name; //player name
	private PAction action;
//	private Action action;

	public PlayerAction(String name, Action action) {
		this.name = name;
		this.action = new PAction(action);
	}
	
	public String getName() {
		return name;
	}

	public PAction getAction() {
//	public Action getAction() {
		return action;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAction(PAction action) {
//	public void setAction(Action action) {
		this.action = action;
	}
	
}