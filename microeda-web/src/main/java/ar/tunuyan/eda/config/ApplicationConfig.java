package ar.tunuyan.eda.config;

import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import ar.tunuyan.eda.executor.DefaultDispatcherImpl;
import ar.tunuyan.eda.executor.Dispatcher;
import ar.tunuyan.eda.executor.cluster.DistributedDispatcherImpl;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.ExecutorConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;

@Configuration
@PropertySource({ "classpath:reactor.properties" })
@ImportResource({ "classpath*:META-INF/spring/app-config.xml" })
@ComponentScan(basePackages = { "ar.tunuyan.eda" })
public class ApplicationConfig {

	private static HazelcastInstance hazelcastInstance;

	@Inject
	private Environment env;

	@PreDestroy
	public void destroy() {
		Hazelcast.shutdownAll();
	}

	@Bean
	public Dispatcher microServiceDispatcher() {
		String startCluster = env.getProperty("micro.reactor.cluster");
		if ("cluster".equals(startCluster)) {
			try {
				initClientMode();
			} catch (IllegalStateException he) {
				System.out.println(he.getMessage());
				initServerMode();
			}
			return createDistributedDispatcher();
		}
		return createLocalDispatcher();
	}

	private Dispatcher createLocalDispatcher() {
		DefaultDispatcherImpl dispatcher = new DefaultDispatcherImpl();
		dispatcher.setExecutorService(Executors.newFixedThreadPool(10));
		return dispatcher;
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

		// System.setProperty("hazelcast.local.localAddress", "127.0.0.1");
		config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
		// //config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
		config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);

		config.getMapConfigs().put("default", initializeDefaultMapConfig());
		config.getMapConfigs().put("session-cluster", initializeClusteredSession());

		ExecutorConfig exconfig = config.getExecutorConfig("default");
		exconfig.setPoolSize(100);
		exconfig.setStatisticsEnabled(true);

		config.getManagementCenterConfig().setEnabled(true);
		config.getManagementCenterConfig().setUrl("http://localhost:9080/mancenter-3.3");

		hazelcastInstance = HazelcastInstanceFactory.newHazelcastInstance(config);

	}

	/**
	 * @return the unique instance.
	 */
	@Bean
	public static HazelcastInstance getHazelcastInstance() {
		return hazelcastInstance;
	}

	private MapConfig initializeDefaultMapConfig() {
		MapConfig mapConfig = new MapConfig();

		/*
		 * Number of backups. If 1 is set as the backup-count for example, then
		 * all entries of the map will be copied to another JVM for fail-safety.
		 * Valid numbers are 0 (no backup), 1, 2, 3.
		 */
		mapConfig.setBackupCount(0);

		/*
		 * Valid values are: NONE (no eviction), LRU (Least Recently Used), LFU
		 * (Least Frequently Used). NONE is the default.
		 */
		mapConfig.setEvictionPolicy(MapConfig.EvictionPolicy.LRU);

		/*
		 * Maximum size of the map. When max size is reached, map is evicted
		 * based on the policy defined. Any integer between 0 and
		 * Integer.MAX_VALUE. 0 means Integer.MAX_VALUE. Default is 0.
		 */
		mapConfig.setMaxSizeConfig(new MaxSizeConfig(0, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE));

		/*
		 * When max. size is reached, specified percentage of the map will be
		 * evicted. Any integer between 0 and 100. If 25 is set for example, 25%
		 * of the entries will get evicted.
		 */
		mapConfig.setEvictionPercentage(25);

		return mapConfig;
	}

	private MapConfig initializeClusteredSession() {
		MapConfig mapConfig = new MapConfig();

		mapConfig.setBackupCount(env.getProperty("cache.hazelcast.backupCount", Integer.class, 1));
		mapConfig.setTimeToLiveSeconds(env.getProperty("cache.timeToLiveSeconds", Integer.class, 3600));
		return mapConfig;
	}

}
