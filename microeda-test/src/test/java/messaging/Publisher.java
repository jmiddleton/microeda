package messaging;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.tunuyan.eda.eventbus.EventBus;
import ar.tunuyan.eda.executor.DispatcherException;

@Service
public class Publisher {

	@Autowired
	EventBus bus;

	public void publishQuotes(int numberOfQuotes) throws InterruptedException {
		long start = System.currentTimeMillis();

		AtomicInteger counter = new AtomicInteger(1);

		for (int i = 0; i < numberOfQuotes; i++) {
			try {
				bus.send("quotes", counter.getAndIncrement());
			} catch (DispatcherException e) {
				e.printStackTrace();
				break;
			}
		}

		long elapsed = System.currentTimeMillis() - start;

		System.out.println("Elapsed time: " + elapsed + "ms");
		System.out.println("Average time per quote: " + elapsed / numberOfQuotes + "ms");
	}

}
