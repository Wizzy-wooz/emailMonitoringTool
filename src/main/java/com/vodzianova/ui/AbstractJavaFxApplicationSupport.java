package com.vodzianova.ui;

import javafx.application.Application;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author Elena Vodzianova 11/08/2016
 */
public abstract class AbstractJavaFxApplicationSupport extends Application {

    private static Logger logger = getLogger(AbstractJavaFxApplicationSupport.class);

    private static String[] savedArgs;

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(getClass(), savedArgs);
        context.getAutowireCapableBeanFactory().autowireBean(this);
        logger.debug("Application has been initiated...");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        context.close();
        logger.debug("Application was stopped.");
    }

    protected static void launchApp(Class<? extends AbstractJavaFxApplicationSupport> clazz, String[] args) {
        AbstractJavaFxApplicationSupport.savedArgs = args;
        Application.launch(clazz, args);
        logger.debug("Application is launched.");
    }
}
