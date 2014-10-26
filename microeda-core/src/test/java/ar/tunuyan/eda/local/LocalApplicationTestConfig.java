package ar.tunuyan.eda.local;

import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ar.tunuyan.eda.executor.Dispatcher;
import ar.tunuyan.eda.executor.local.LocalDispatcherImpl;

@Configuration
@ComponentScan(basePackages = { "ar.tunuyan.eda.executor", "ar.tunuyan.eda.eventbus" })
public class LocalApplicationTestConfig {

	@Bean
	public Dispatcher createLocalDispatcher() {
		LocalDispatcherImpl dispatcher = new LocalDispatcherImpl();
		dispatcher.setExecutorService(Executors.newFixedThreadPool(10));
		return dispatcher;
	}
}