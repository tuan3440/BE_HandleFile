package com.mycompany.myapp.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setContext(applicationContext);
    }

    public static synchronized void setContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static synchronized ApplicationContext getContext() {
        return context;
    }
}
