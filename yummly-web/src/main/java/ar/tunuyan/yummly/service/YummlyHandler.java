package ar.tunuyan.yummly.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.tunuyan.eda.eventbus.Event;
import ar.tunuyan.eda.eventbus.handler.EventHandler;
import ar.tunuyan.yummly.model.Recipes;

@Service("recipes")
class YummlyHandler implements EventHandler<Integer> {

	RestTemplate restTemplate = new RestTemplate();

	public void handle(Event<Integer> ev) {
		String url = "http://api.yummly.com/v1/api/recipes";
		
		//_app_id=app-id&_app_key=app-key&your_search_parameters

		Recipes recipes = restTemplate.getForObject(url, Recipes.class);

	}

}