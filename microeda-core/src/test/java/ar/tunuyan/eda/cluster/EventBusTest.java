package ar.tunuyan.eda.cluster;

import static org.junit.Assert.fail;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.tunuyan.eda.eventbus.EventBus;
import ar.tunuyan.eda.eventbus.EventCallback;
import ar.tunuyan.eda.eventbus.ReplyException;
import ar.tunuyan.eda.executor.DispatcherException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(name = "root", classes = ApplicationTestConfig.class) })
@ActiveProfiles("dev")
public class EventBusTest {

	@Inject
	private EventBus bus;

	// @Test
	public void testRemoteMicroservice() {
		try {
			bus.send("getViewHandler", "8003603459045713");

		} catch (DispatcherException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testCallableMicroservice() {
		try {
			for (int i = 0; i < 10; i++) {
				final int index = i;
				
				bus.send("getViewHandler", "800" + index, new EventCallback<String>() {

					@Override
					public void onResponse(String response) {
						System.out.println(response);
						Assert.assertNotNull(response);
						Assert.assertEquals("{800" + index + "}", response);
					}

					@Override
					public void onFailure(ReplyException re) {
						System.out.println("{800" + index + "} Client should restart the TX >>>>>>>> " + re.getMessage());
					}
				});
			}

			System.out.println("end.");
		} catch (DispatcherException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	// @Test
	public void testPublishMicroservice() {
		try {
			for (int i = 0; i < 10; i++) {
				bus.publish("getViewHandler", "8003603459045713");
			}
		} catch (DispatcherException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}

}
