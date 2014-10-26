package messaging;

import static org.junit.Assert.fail;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(name = "root", classes = ApplicationTestConfig.class) })
@ActiveProfiles("dev")
public class RemoteClientPublishQuoteTest {

	@Inject
	private Publisher publisher;

	@Before
	public void setup() {

	}

	@Test
	public void testPublish() {
		try {
			publisher.publishQuotes(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
