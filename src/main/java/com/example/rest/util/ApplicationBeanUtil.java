package com.example.rest.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationBeanUtil implements ApplicationContextAware{

	
	private static  ApplicationContext applicationContext =null;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext=applicationContext;
		System.out.print(applicationContext);
	}
	
	
	public static ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	public  static Object getBean(String name){
		Object object =  applicationContext.getBean(name);
		return object;
	}
}
