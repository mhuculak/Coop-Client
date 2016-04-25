package coop.actions;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.*;


// @JsonIgnoreProperties(ignoreUnknown = true) // typical spring crap that doesnt work
public class PAction {
	public List<ActionObject> objects;
	public String action;
	public String type;

	public PAction() {

	}

	public PAction(String action) {
		this.action = action;
	}

	public PAction(Action a) {
		this.action = a.getAction();
		this.type = a.getType();
		this.objects = a.getObjects();
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setObjects(List<ActionObject> objects) {
		this.objects = objects;
	}

	public String getAction() {
		return action;
	}

	public String getType() {
		return type;
	}

	public List<ActionObject> getObjects() {
		return objects;
	}
}