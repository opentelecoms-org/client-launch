package org.opentelecoms.client.launch;

public class LaunchFailureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LaunchFailureException(Exception ex) {
		super(ex);
	}

	public LaunchFailureException(String message) {
		super(message);
	}

}
