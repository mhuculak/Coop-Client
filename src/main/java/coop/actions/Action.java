package coop.actions;

// import org.codehaus.jackson.annotate.JsonIgnore;
// import org.fasterxml.jackson.annotate.JsonIgnore;

import java.util.*;

enum ActionValue { read, write, password, phone, speak, speakPhone, answer, hangUp, climb, boost, 
				   open, close, lock, unlock, pick, drop, give, lift, buy, sell, playAs, newGame, quit, exit};
						 

public class Action {

	List<ActionObject> objects;
	String action;
//	@JsonIgnore
	ActionValue actionValue;
	String type;

	public Action(String action) {
		this.action = action;
	}

	public Action(PAction pa) {
		this.action = pa.getAction();
		this.type = pa.getType();
		this.objects = pa.getObjects();
	}

	public ActionValue getActionValue() {
		if (actionValue == null) {
			System.out.println("get ActionValue for \"" + action + "\"");			
			if (action.equals("speak on phone")) {
				actionValue = ActionValue.speakPhone;
			}
			else if (action.equals("hang up")) {
				actionValue = ActionValue.hangUp;
			}
			else if (action.equals("Play As")) {
				actionValue = ActionValue.playAs;
			}
			else if (action.equals("New Game")) {
				actionValue = ActionValue.newGame;
			}
			else {
//					System.out.println("ERROR: unknown action " + action);
				actionValue = ActionValue.valueOf(action);				
			}

		}
		return actionValue;
	}

	public String getInputText() {
		String inputText = "";
		switch (getActionValue()) {
			case write:
			case speak:
			case speakPhone:
				inputText = "Message: ";
				break;
			case password:
				inputText = "Enter Password: ";
				break;
			case phone:
				inputText = "Phone Number: ";
				break;
			default:
				break;			
		}
		return inputText;
	}

	public void addInput(String value) {
		switch (getActionValue()) {
			case write:
			case password:
				objects.get(1).setName(value);
				break;
			case phone:
			case speak:
			case speakPhone:
				objects.get(0).setName(value);
				break;
			default:
				break;
		}
	}

	public boolean needsInput() {
		boolean needsInput = false;
		switch (getActionValue()) {
			case write:
			case password:
			case phone:
			case speak:
			case speakPhone:
				needsInput = true;
				break;
			default:
				needsInput = false;
		}
		return needsInput;
	}

	public boolean needsMap() {
		boolean needsMap = false;
		switch  (getActionValue()) {
			case password:
			case climb:
			case boost:
			case open:
			case close:
			case lock:
			case unlock:
			case give:
			case lift:
				needsMap = true;
				break;
			default:
				needsMap = false;
		}
		return needsMap;
	}

	private void addObj(ActionObject ao) {
		if (objects == null) {
			objects = new ArrayList<ActionObject>();			
		}
		objects.add(ao);
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (action != null) {
			sb.append(action);
		}

		if (type != null) {
			sb.append(" type: " + type);
		}
		
		if (objects != null) {
			sb.append(" objects: ");
			for (ActionObject ao : objects) {
				sb.append(ao.toString()+",");
			}
		}
		return sb.toString();
	}

}