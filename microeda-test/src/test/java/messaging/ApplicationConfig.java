package messaging;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ar.tunuyan.eda.executor.Dispatcher;
import ar.tunuyan.eda.executor.cluster.DistributedDispatcherImpl;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@Configuration
@ComponentScan(basePackages = { "hello" })
public class ApplicationConfig {

	private static HazelcastInstance hazelcastInstance;

	@PreDestroy
	public void destroy() {
		Hazelcast.shutdownAll();
	}

	@Bean
	public Dispatcher microServiceDispatcher() {
		initClientMode();
		return createDistributedDispatcher();
	}

	private Dispatcher createDistributedDispatcher() {
		DistributedDispatcherImpl hzDispatcher = new DistributedDispatcherImpl(hazelcastInstance);
		hzDispatcher.init();
		return hzDispatcher;
	}

	private void initClientMode() {
		System.setProperty("hazelcast.operation.call.timeout.millis", "5000");

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setExecutorPoolSize(5);

		hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
	}

	/**
	 * @return the unique instance.
	 */
	@Bean
	public static HazelcastInstance getHazelcastInstance() {
		return hazelcastInstance;
	}
}
