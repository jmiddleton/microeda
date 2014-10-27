package hello;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.tunuyan.eda.eventbus.Event;
import ar.tunuyan.eda.eventbus.handler.EventHandler;

@Service("getQuote")
class RequestResponseReceiver implements EventHandler<Integer> {

	RestTemplate restTemplate = new RestTemplate();

	public void handle(Event<Integer> ev) {
		QuoteResource quoteResource = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", QuoteResource.class);
		String msg = "Quote " + ev.body() + ": " + quoteResource.getValue().getQuote();
		System.out.println(msg);

		ev.reply(msg);
	}

}