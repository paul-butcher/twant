
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
}
