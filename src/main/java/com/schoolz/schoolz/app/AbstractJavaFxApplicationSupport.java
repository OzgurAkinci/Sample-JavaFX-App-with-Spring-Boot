package com.schoolz.schoolz.app;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public abstract class AbstractJavaFxApplicationSupport extends Application {

    static ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        super.init();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        applicationContext.close();
    }

    public static void launchApp(Class<? extends AbstractJavaFxApplicationSupport> appClass, ConfigurableApplicationContext context, String[] args) {
        applicationContext = context;
        I18N.setLocale(new Locale("en", "US"));
        Application.launch(appClass, args);
    }

}
