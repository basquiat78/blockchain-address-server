package com.basquiat.address.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * AsyncConfig
 * created by basquiat
 *
 */
@Configuration
@EnableAsync
public class AsyncConfig {

	/**
	 * default async Thread Pool
	 * @return ThreadPoolTaskExecutor
	 */
	@Bean(name = "taskExecutor")
    public Executor defaultTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(10);
        return threadPoolTaskExecutor;
    }

}
