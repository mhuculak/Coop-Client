package coop.player;

import coop.actions.Action;
import coop.actions.PAction;

import coop.map.Position;

// import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.*;


// @JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerOptions {
	private String message;
	private Position position;	
	private List<PAction> actions;

	public PlayerOptions() {

	}

	public PlayerOptions(String message, String action) {
		this.message = message;
		actions = new ArrayList<PAction>();
		actions.add(new PAction(action));
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public String getMessage() {
		return message;
	}

	public List<Action> getActions() {
//		return actions;
		List<Action> acts = new ArrayList<Action>();
		for (PAction a : actions) {
			acts.add(new Action(a));
		}
		return acts;
	}

	public void setActions(List<PAction> actions) {
		this.actions = actions;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}