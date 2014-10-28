package ar.tunuyan.eda.web;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import ar.tunuyan.eda.eventbus.EventCallback;
import ar.tunuyan.eda.eventbus.ReplyException;
import ar.tunuyan.eda.service.QuoteService;

import com.codahale.metrics.annotation.Timed;

@RestController
public class QuoteResource {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private Map<String, DeferredResult<String>> deferredRequests = new ConcurrentHashMap<String, DeferredResult<String>>();

	@Inject
	private QuoteService quoteService;

	@RequestMapping(produces = "application/json", method = RequestMethod.GET, consumes = "application/json", value = "/api/quote/{value}")
	@ResponseBody
	@Timed
	public DeferredResult<String> getQuote(@PathVariable Integer value) {

		final String requestId = value + "_key";

		if (logger.isTraceEnabled()) {
			logger.trace("Request received: " + requestId);
		}

		final DeferredResult<String> result = new DeferredResult<String>(20000);
		this.deferredRequests.put(requestId, result);

		EventCallback<String> eventCallback = new EventCallback<String>() {
			@Override
			public void onResponse(String response) {
				if (deferredRequests.containsKey(requestId)) {
					DeferredResult<String> deferredResult = deferredRequests.remove(requestId);
					deferredResult.setResult(response);
				}
			}

			@Override
			public void onFailure(ReplyException re) {
				logger.trace("Errorrrrr.... " + re.getMessage());
				DeferredResult<String> deferredResult = deferredRequests.remove(requestId);
				deferredResult.setErrorResult(re.getMessage());
			}
		};
		quoteService.getQuote(value, eventCallback);

		result.onCompletion(new Runnable() {
			public void run() {
				deferredRequests.remove(requestId);
			}
		});

		return result;
	}

	@ExceptionHandler
	@ResponseBody
	public String handleException(IllegalStateException ex) {
		return "Handled exception: " + ex.getMessage();
	}
}
