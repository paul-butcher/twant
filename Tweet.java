
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.tools.ant.Task;

public class Tweet  extends Task {
	private String message; 
	public void execute() {

	}
	
	public void addText(String text) {
		message = text;
	}
	
	public String getURL() {
		try {
			return "http://twitter.com/statuses/update.xml?status=" + URLEncoder.encode(message, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
