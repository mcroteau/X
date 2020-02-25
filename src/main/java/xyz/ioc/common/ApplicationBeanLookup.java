package xyz.ioc.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationBeanLookup implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	public void setApplicationContext(ApplicationContext appContext) throws BeansException{
		context = appContext;
	}
	
	public static ApplicationContext getApplicationContext(){
		return context;
	}

}
