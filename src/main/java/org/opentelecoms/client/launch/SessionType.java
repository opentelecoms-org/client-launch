package org.opentelecoms.client.launch;

public enum SessionType {
	ANY (null),
	ANY_CALL (null),
	VOICE_CALL (null),
	VIDEO_CALL (null),
	CHAT ("message");
	
	private String xmppParameter;

	SessionType(String xmppFragment) {
		this.xmppParameter = xmppFragment;
	}
	
	public String getParameter(String scheme) {
		if(scheme.equals("sip")) {
			return null;
		} else if(scheme.equals("xmpp")) {
			return xmppParameter;
		}
		return null;
	}

}
