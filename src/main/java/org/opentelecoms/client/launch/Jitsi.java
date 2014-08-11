package org.opentelecoms.client.launch;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Jitsi implements Client {
	
	// Jitsi installation relative to %ProgramFiles% on Windows
	private static final String PROGRAM_FILES_JITSI = "jitsi";
	private static final String JITSI_EXE = "Jitsi.exe";
	
	private static final String LATEST_JNLP_LOCATION
	    = "https://jitsi.org/webstart/latest/client.jnlp";
	
	private static final String STABLE_JNLP_LOCATION
    	= "https://jitsi.org/webstart/stable/client.jnlp";
	
	private static final String DEFAULT_JNLP_LOCATION = LATEST_JNLP_LOCATION;
	
	private String[] uriSchemes = { "sip", "xmpp" };
	
	Set<String> _schemes = new HashSet<String>();
	private static String jnlpLocation = DEFAULT_JNLP_LOCATION;
	
	public Jitsi() {
		for(String scheme : uriSchemes) {
			_schemes.add(scheme.toLowerCase());
		}
	}
	
	public static void setJnlpLocation(String jnlpLocation) {
		Jitsi.jnlpLocation = jnlpLocation;
	}
	
	private String getJnlpLocation() {
		return jnlpLocation;
	}

	@Override
	public boolean handlesURI(URI uri, SessionType st) {
		String scheme = uri.getScheme().toLowerCase();
		return _schemes.contains(scheme);
	}
	
    private File findJitsiBinary() {
    	String os = System.getProperty("os.name");
    	File jitsiBinary = null;
    	if(os.startsWith("Windows")) {
    		// Assume it is in default location, may not be in PATH
			Vector<File> locations = new Vector<File>();
			String val = System.getenv("ProgramFiles");
			if(val != null) {
				locations.add(new File(val, PROGRAM_FILES_JITSI));
			}
			val = System.getenv("ProgramFiles(x86)");
			if(val != null) {
				locations.add(new File(val, PROGRAM_FILES_JITSI));
			}
			for(File location : locations) {
				jitsiBinary = new File(location, JITSI_EXE);
				if(jitsiBinary.exists()) {
					continue;
				}
			}
		} else if(File.separator.equals("/")) {  // UNIX-like filesystem
    		// Assume it is in PATH
    		jitsiBinary = new File("jitsi");
    	}
    	return jitsiBinary;
    }
    
    private boolean runProc(ProcessBuilder pb) {
    	File jitsiBinary = findJitsiBinary();
    	if(jitsiBinary == null) {
    		return false;
    	}
    	try {
			Process p = pb.start();
			try {
				// FIXME - sleep for shorter periods and check
				// periodically if the process finished
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// something went wrong while sleeping
			}
			int result = p.exitValue();
			if(result != 0) {
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IllegalThreadStateException e) {
			// It looks like the process did not exit within the
			// timeout, assume it started OK
			return true;
		}
    }
    
    private boolean runJitsi(URI uri) {
    	File jitsiBinary = findJitsiBinary();
    	if(jitsiBinary == null) {
    		return false;
    	}
    	ProcessBuilder pb = new ProcessBuilder(jitsiBinary.getPath(), uri.toString());
    	return runProc(pb);
    }
    
    private boolean runJitsiWebStart(URI uri) {
    	File jitsiBinary = findJitsiBinary();
    	if(jitsiBinary == null) {
    		return false;
    	}
    	ProcessBuilder pb = new ProcessBuilder("javaws", "-open", uri.toString(), getJnlpLocation());
    	return runProc(pb);
    }

	@Override
	public boolean launchURI(URI uri, SessionType st) {
		// Try running Jitsi directly
		if(runJitsi(uri)) {
			return true;
		}

		// Try and make the call using WebStart / JNLP
		if(runJitsiWebStart(uri)) {
			return true;
		}

		// We were not able to initiate the call using Jitsi
		return false;
	}

}
