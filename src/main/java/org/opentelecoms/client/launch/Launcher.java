package org.opentelecoms.client.launch;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {
	
	Logger logger = LoggerFactory.getLogger(Launcher.class);

	Desktop desktop = null;
	
	private static ServiceLoader<Client> clientLoader
    	= ServiceLoader.load(Client.class);
	
	Client[] clients;
	
	protected Launcher() {
		if (Desktop.isDesktopSupported()) {
			logger.info("java.awt.Desktop supported");
			Desktop _desktop = Desktop.getDesktop();
			if(_desktop.isSupported(Action.BROWSE)) {
				logger.info("java.awt.Desktop.BROWSE supported");
				desktop = _desktop;
			} else {
				logger.warn(
					"java.awt.Desktop.BROWSE not supported on this platform");
			}
		} else {
			logger.warn("java.awt.Desktop not supported on this platform");
		}
	}

	public static Launcher getInstance() {
		return new Launcher();
	}

	public void launch(URI uri, SessionType st) throws LaunchFailureException {
		logger.debug("trying to initiate RTC session for target URI: " + uri +
			" and session type: " + st);
		if(uri == null) {
			throw new LaunchFailureException("URI may not be null");
		}
		if(st == null) {
			throw new LaunchFailureException("session type may not be null");
		}
		try {
			if(desktop != null) {
				logger.debug("trying to call using java.awt.Desktop.browse()");
				desktop.browse(uri);
				logger.debug("URI handled by java.awt.Desktop.browse()");
				return;
			}
		} catch(IOException ioe) {
			// There is no handler for the URI scheme or some other failure
			// occurred
			// Just fall through to the code after the try/catch block
			logger.debug("Exception from java.awt.Desktop.browse(): " +
				ioe.getMessage());
		}

		for(Client client : clientLoader) {
			String clientName = client.getClass().getName();
			logger.debug("checking RTC client: " + clientName);
			if(client.handlesURI(uri, st)) {
				if(client.launchURI(uri, st)) {
					// URI handled successfully
					logger.debug("URI handled by " + clientName);
					return;
				} else {
					logger.debug("failed to launch client");
				}
			} else {
				logger.debug("client does not handle this combination " +
					"of URI and session type");
			}
		}
		
		logger.warn("no method was able to handle the URI");
		throw new LaunchFailureException(
			"Could not launch a client for URI: " + uri.toString());
	}
}
