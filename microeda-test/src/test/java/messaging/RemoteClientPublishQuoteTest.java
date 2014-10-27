package messaging;

import static org.junit.Assert.fail;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.tunuyan.eda.eventbus.EventCallback;
import ar.tunuyan.eda.eventbus.ReplyException;

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

	@Test
	public void testRequestReply() {
		try {
			for (int i = 0; i < 10; i++) {
				final int index = i;

				publisher.sendQuote(index, new EventCallback<String>() {
					@Override
					public void onResponse(String response) {
						System.out.println(response);
						Assert.assertNotNull(response);
						Assert.assertTrue(response.startsWith("Quote " + index + ":"));
					}

					@Override
					public void onFailure(ReplyException re) {
						// TODO
						System.out.println(">>>>>>>> Error: " + re.getMessage());
					}
				});
			}

			System.out.println("end.");
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
