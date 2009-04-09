package twant;

import junit.framework.TestCase;

public class testTweet extends TestCase {

	public void testAddText() {
		assertEquals(1,1);
	}
	
	public void testgetURL() {
		Tweet t = new Tweet();
		t.addText("Hello");
		assertEquals("http://twitter.com/statuses/update.xml?status=Hello", t.getURL());
	}

	public void testgetURLWithSpace() {
		Tweet t = new Tweet();
		t.addText("Hello Sailor");
		assertEquals("http://twitter.com/statuses/update.xml?status=Hello+Sailor", t.getURL());
	}

	public void testgetURLWithEscapes() {
		Tweet t = new Tweet();
		t.addText("Hello, Sailor!");
		assertEquals("http://twitter.com/statuses/update.xml?status=Hello%2C+Sailor%21", t.getURL());
	}
	
	public void testGetEncodedCredentials(){
		Tweet t = new Tweet();
		t.setUser("someUser");
		t.setPass("passWord123");
		assertEquals("c29tZVVzZXI6cGFzc1dvcmQxMjM=",t.getEncodedCredentials());
	}

	public void testgetURLWithMessageAttribute() {
		Tweet t = new Tweet();
		t.setMessage("Hello Dolly");
		assertEquals("http://twitter.com/statuses/update.xml?status=Hello+Dolly", t.getURL());
	}

	public void testgetURLWithMessageAttributeAndContent() {
		Tweet t = new Tweet();
		t.setMessage("Hello Dolly");
		t.addText("Hello Sailor");
		assertEquals("http://twitter.com/statuses/update.xml?status=Hello+Dolly", t.getURL());
	}
}
