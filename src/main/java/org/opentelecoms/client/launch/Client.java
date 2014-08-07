package org.opentelecoms.client.launch;

import java.net.URI;

/**
 * Clients that can be used to contact real-time communications URIs
 * (such as SIP and XMPP addresses) can implement this interface.
 * 
 * The launcher uses the ServiceLoader pattern to find all implementations of
 * this interface and tries each client until one of them handles the URI.
 */
public interface Client {
	
	/**
	 * Check if the client can handle a particular type of URI for a
	 * starting an RTC session.
	 * @param uri the target URI to be contacted
	 * @param st the real-time communicaiton session type
	 * @return true if the client is suitable for the URI and session type
	 */
	boolean handlesURI(URI uri, SessionType st);
	
	/**
	 * Try to launch the client to contact the URI
	 * @param uri the target URI to be contacted
	 * @return true if successful
	 */
	boolean launchURI(URI uri, SessionType st);

}
