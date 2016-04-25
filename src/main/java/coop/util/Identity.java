package coop.util;

public class Identity {
	private String userName;
	private char[] password;

	public Identity() {

	}

	public Identity(String userName, char[] password) {
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public char[] getPassword() {
		return password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}
}