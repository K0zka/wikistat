/**
 * Created by kocka on 5/7/14.
 */

import org.junit.Test
import org.junit.Assert

public class AppTest {
	Test fun sayHello() {
		Assert.assertEquals("Hello World", App().sayHello("World"))
	}
}