package coop.action;

public class TestObject {
	private String name;
	private String value;

	public TestObject() {

	}
	
	public TestObject(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return "name = " + name + " value = " + value;
	}
}