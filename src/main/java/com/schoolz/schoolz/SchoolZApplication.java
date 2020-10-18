package com.schoolz.schoolz;


import com.schoolz.schoolz.app.AbstractJavaFxApplicationSupport;
import com.schoolz.schoolz.screen.MainScreen;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


@SpringBootApplication
@EntityScan("com.schoolz.schoolz.entity")
@EnableJpaRepositories("com.schoolz.schoolz.dao")
public class SchoolZApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(SchoolZApplication.class, args);
		AbstractJavaFxApplicationSupport.launchApp(MainScreen.class, applicationContext, args);
	}

	@Bean("threadPoolTaskExecutor")
	public TaskExecutor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(1000);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("Async-");
		return executor;
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource resource =
				new ResourceBundleMessageSource();
		resource.setBasename("messages");
		resource.setDefaultEncoding("utf-8");
		return resource;
	}

}
