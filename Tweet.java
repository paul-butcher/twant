
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//Used to encode the status message for posting to Twitter.
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

import org.apache.tools.ant.Task;
import org.apache.commons.codec.binary.Base64;

public class Tweet  extends Task {
	private String statusText; 
	private String username = ""; 
	private String password = "";
	private String credentials ="";
	
	public void execute() {
		try {
			HttpURLConnection http = getURLAsConnection();
			addAuthorisationHeader(http);
			http.setRequestMethod("POST");
	        http.connect();
	        System.out.println(http.getResponseCode() + ": " + http.getResponseMessage());
	        http.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addText(String text) {
		statusText = text;
	}
	
	
	public void setUser(String user) {
		username = user;
	}

	public void setPass(String pass) {
		password = pass;
	}

	public void setCredentialsfile(String path) throws FileNotFoundException, IOException {
		FileReader f = new FileReader(path);
		BufferedReader r = new BufferedReader(f);
		credentials = r.readLine();
	}
	
	public String getURL() {
		
		try {
			return "http://twitter.com/statuses/update.xml?status=" + URLEncoder.encode(statusText, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected String getEncodedCredentials() {
		return new String(new Base64().encode( credentials.isEmpty()? (username + ":" + password).getBytes(): credentials.getBytes() ));
	}

	private HttpURLConnection getURLAsConnection() throws MalformedURLException, IOException {
		String urlString = getURL();
		if(urlString == null) {
			return null;
		} else {
			URL u = new URL(urlString);
			return (HttpURLConnection) u.openConnection();
		}
	}
	
	private void addAuthorisationHeader(HttpURLConnection http) {
	    String encodedCredential = getEncodedCredentials();
	    System.out.println(encodedCredential);
	    http.setRequestProperty("Authorization", "Basic " +  encodedCredential);
	}
}
