package ar.tunuyan.eda.local;

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

import ar.tunuyan.eda.eventbus.Event;
import ar.tunuyan.eda.eventbus.EventBus;
import ar.tunuyan.eda.eventbus.EventCallback;
import ar.tunuyan.eda.eventbus.ReplyException;
import ar.tunuyan.eda.eventbus.handler.EventHandler;
import ar.tunuyan.eda.executor.DispatcherException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration(name = "root", classes = LocalApplicationTestConfig.class) })
@ActiveProfiles("dev")
public class LocalEventBusTest {

	@Inject
	private EventBus bus;

	@Before
	public void setup() {
		bus.registerMicroService("test.address", new EventHandler<String>() {

			@Override
			public void handle(Event<String> message) {
				if ("message 2".equals(message.body())) {
					throw new IllegalArgumentException("error");
				}
				System.out.println("[" + Thread.currentThread().getName() + "] Received message: " + message.body());
				message.reply("response " + message.body());
			}
		});
	}

	@Test
	public void testLocalMicroservice() {
		try {

			for (int i = 0; i < 3; i++) {
				final int index = i;
				bus.send("test.address", "message " + index, new EventCallback<String>() {

					@Override
					public void onResponse(String response) {
						System.out.println(response);
						Assert.assertEquals("response message " + index, response);
					}

					@Override
					public void onFailure(ReplyException re) {
						System.out.println("Processing the failure: " + re.getMessage());
					}
				});
			}

		} catch (DispatcherException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
