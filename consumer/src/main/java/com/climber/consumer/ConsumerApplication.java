package com.climber.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;

import feign.Logger;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
	
	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * 异步调用断路器
	 * @return
	 */
	@Bean
	public HystrixCommandAspect hystrixCommandAspect(){
		return new HystrixCommandAspect();
	}
	
	/**
	 * 开启feign日志功能
	 * @return
	 */
	@Bean
	Logger.Level feignLoggerLevel() {
	    return Logger.Level.FULL;
	}
}
