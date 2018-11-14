package com.climber.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.climber.commons.Book;
import com.climber.feign.service.imp.HelloServiceFallback;

/**
 * fallback对hystrix进行降级处理，处理的方法对应为HelloServiceFallback 同时在application.properties里面配置feign.hystrix.enabled=true
 * @author Derlin
 *
 */
@FeignClient(value = "hello-service", fallback=HelloServiceFallback.class)
public interface HelloService {

	@RequestMapping("/hello")
	String hello();

	// feign进行参数传递
	@RequestMapping(value = "/hello1", method = RequestMethod.GET)
	String hello(@RequestParam("name") String name);

	@RequestMapping(value = "/hello2", method = RequestMethod.GET)
	Book hello(@RequestHeader("name") String name, @RequestHeader("author") String author,
			@RequestHeader("price") Integer price);

	@RequestMapping(value = "/hello3", method = RequestMethod.POST)
	String hello(@RequestBody Book book);

}
