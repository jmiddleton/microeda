package ar.tunuyan.eda.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ar.tunuyan.eda.eventbus.EventBus;
import ar.tunuyan.eda.eventbus.EventCallback;
import ar.tunuyan.eda.executor.DispatcherException;

@Service
public class QuoteService {

	@Inject
	private EventBus eventBus;

	public <T extends Serializable> void getQuote(int value, EventCallback<T> callback) {
		try {
			eventBus.send("getQuote", value, callback);
		} catch (DispatcherException e) {
			e.printStackTrace();
			// TODO: improve the error handling
		}
	}

}
