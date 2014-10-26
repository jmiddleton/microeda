package ar.tunuyan.eda.cluster;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ar.tunuyan.eda.executor.Dispatcher;
import ar.tunuyan.eda.executor.cluster.DistributedDispatcherImpl;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.ExecutorConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;

@Configuration
@ComponentScan(basePackages = { "ar.tunuyan.eda.executor", "ar.tunuyan.eda.eventbus" })
public class ApplicationTestConfig {

	private static HazelcastInstance hazelcastInstance;

	@PreDestroy
	public void destroy() {
		Hazelcast.shutdownAll();
	}

	@Bean
	public Dispatcher microServiceDispatcher() {
		try {
			initClientMode();
		} catch (IllegalStateException he) {
			System.out.println(he.getMessage());
			initServerMode();
		}
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

	private void initServerMode() {
		final Config config = new Config();
		config.setInstanceName("tunuyan.eda");
		config.getNetworkConfig().setPort(5701);
		config.getNetworkConfig().setPortAutoIncrement(true);

		config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
		config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
		// config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

		ExecutorConfig exconfig = config.getExecutorConfig("default");
		exconfig.setPoolSize(5);
		exconfig.setStatisticsEnabled(true);

		hazelcastInstance = HazelcastInstanceFactory.newHazelcastInstance(config);
	}

	/**
	 * @return the unique instance.
	 */
	@Bean
	public static HazelcastInstance getHazelcastInstance() {
		return hazelcastInstance;
	}
}