package org.opentelecoms.client.launch;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ServiceLoader;

public class Launcher {
	
	Desktop desktop = null;
	
	private static ServiceLoader<Client> clientLoader
    	= ServiceLoader.load(Client.class);
	
	Client[] clients;
	
	protected Launcher() {
		if (Desktop.isDesktopSupported()) {
			Desktop _desktop = Desktop.getDesktop();
			if(_desktop.isSupported(Action.BROWSE)) {
				desktop = _desktop;
			}
		}
	}

	public static Launcher getInstance() {
		return new Launcher();
	}

	public void launch(URI uri, SessionType st) throws LaunchFailureException {
		try {
			if(desktop != null) {
				desktop.browse(uri);
				return;
			}
		} catch(IOException ioe) {
			// There is no handler for the URI scheme or some other failure
			// occurred
			// Just fall through to the code after the try/catch block
		}

		for(Client client : clientLoader) {
			if(client.handlesURI(uri, st)) {
				if(client.launchURI(uri, st)) {
					// URI handled successfully
					return;
				}
			}
		}
		
		throw new LaunchFailureException(
			"Could not launch a client for URI: " + uri.toString());
	}
}
