package ar.tunuyan.eda.executor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class SpringContextHelper implements ApplicationContextAware {
	private static ApplicationContext context = null;
	
	public synchronized void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if(context == null) {
			context = applicationContext;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}
	
	public static <T> T getBean(String name, Class<T> type) {
		return (T) context.getBean(name, type);
	}
}
