/*	
 * Tweet.java, an ANT task that posts updates to tweet.
 * 
 * Copyright (c) 2009 Paul Butcher
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Paul Butcher nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY Paul Butcher ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Paul Butcher BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package twant;
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

/**
 * The Tweet class provides functionality to allow ANT to post updates to twitter, 
 * 	after the same fashion as {@literal <echo></echo>}.
 * Usage:
 * 	Define the task thus:
 * 	{@code <taskdef name="tweet" classname="Tweet" classpath="Twant.jar"></taskdef>}
 *  The task may used with a username and password thus:
 *  {@code <tweet user="myUser" pass="myPass">hello world</tweet>}
 *  or (in order to keep passwords out of source control repositories), thus:
 *  {@code <tweet credentialsFile="myTwitterDetails">hello world</tweet>}
 */
public class Tweet  extends Task {
	private String statusText = ""; 
	private String username = ""; 
	private String password = "";
	private String credentials ="";

	/**
	 * Override of org.apache.tools.ant.Task::execute, posts the message defined by this instance to Twitter.
	 * Echos the HTTP response code and message.
	 */
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
	
	/**
	 * Specifies the text of the tweet to be posted.
	 * @param text a String representing the content of the tweet to be posted.
	 */
	public void addText(String text) {
		if(statusText.isEmpty()){
			statusText = text;
		}
	}
	
	/**
	 * Sets the username to be used for the tweet.
	 * @param user the Twitter username to be used. 
	 */
	public void setUser(String user) {
		username = user;
	}

	/**
	 * Sets the password to be used for the tweet.
	 * @param pass the password for the Twitter user specified by {@link setUser}. 
	 */
	public void setPass(String pass) {
		password = pass;
	}

	/**
	 * Sets a credentials file to be used in place of setUser and setPassword.
	 * The credentials file should have a single line, specifying the username and password
	 * separated by a colon, thus:
	 *	username:password
	 * @param path The path to the file
	 * @throws FileNotFoundException if the file specified by {@link path} could not be found.
	 * @throws IOException If a line could not be read from the file.
	 */
	public void setCredentialsfile(String path) throws FileNotFoundException, IOException {
		FileReader f = new FileReader(path);
		BufferedReader r = new BufferedReader(f);
		credentials = r.readLine();
	}
	
	/**
	 * Sets the value of the message to be used to update the authenticated user's Twitter status.
	 * setMessage takes precedence over {@link addText}
	 * @param message a String corresponding to the message. 
	 */
	public void setMessage(String message) {
		statusText = message; 
	}
	/**
	 * Returns the URL to use to update the authenticated user's Twitter status with the text
	 * provided in addText.
	 * @return String The URL 
	 */
	protected String getURL() {
		try {
			return "http://twitter.com/statuses/update.xml?status=" + URLEncoder.encode(statusText, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns the credentials encoded as to be used in a Basic authorisation header.
	 * @return A String representation of the encoded credentials.
	 */
	protected String getEncodedCredentials() {
		return new String(new Base64().encode( credentials.isEmpty()? (username + ":" + password).getBytes(): credentials.getBytes() ));
	}

	/**
	 * Returns a connection to the appropriate URL as a java.net.HttpURLConnection object 
	 * @return HttpURLConnection object representing the url used to post the update. 
	 */
	private HttpURLConnection getURLAsConnection() throws MalformedURLException, IOException {
		String urlString = getURL();
		if(urlString == null) {
			return null;
		} else {
			URL u = new URL(urlString);
			return (HttpURLConnection) u.openConnection();
		}
	}
	
	/**
	 * Adds authorisation details to the given HttpURLConnection object.
	 * @param http HttpURLConnection object representing the url used to post the update. 
	 */
	private void addAuthorisationHeader(HttpURLConnection http) {
	    String encodedCredential = getEncodedCredentials();
	    http.setRequestProperty("Authorization", "Basic " +  encodedCredential);
	}
}
