package com.climber.consumer.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.climber.commons.Book;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;

@Service
public class BookService {

	@Autowired
	RestTemplate restTemplate;

	// Hystrix注解缓存 @CacheResult、@CacheKey和@CacheRemove
	// @CacheResult表示对参数全部缓存
	// @CacheKey对指定参数进行缓存，注解加在参数处
	// @CacheRemove溢出参数缓存
	@CacheResult
	@HystrixCommand
	public Book test6(Integer id, String aa) {
		return restTemplate.getForObject("http://HELLO-SERVICE/getbook5/{1}", Book.class, id);
	}

//	public Book test8(Long id) {
//		return restTemplate.getForObject("http://HELLO-SERVICE/getbook6/{1}", Book.class, id);
//	}

	/**
	 * hystrix请求合并
	 * @param ids
	 * @return
	 */
	public List<Book> test9(List<Long> ids) {
		System.out.println(
				"test9---------" + ids + "Thread.currentThread().getName():" + Thread.currentThread().getName());
		Book[] books = restTemplate.getForObject("http://HELLO-SERVICE/getbook6?ids={1}", Book[].class,
				StringUtils.join(ids, ","));
		return Arrays.asList(books);
	}

	// 合并请求，将test11的所有的请求合并为一个请求
	@HystrixCollapser(batchMethod = "test11", collapserProperties = {
			@HystrixProperty(name = "timerDelayInMilliseconds", value = "100") })
	public Future<Book> test10(Long id) {
		return null;
	}

	@HystrixCommand
	public List<Book> test11(List<Long> ids) {
		System.out.println(
				"test9---------" + ids + "Thread.currentThread().getName():" + Thread.currentThread().getName());
		Book[] books = restTemplate.getForObject("http://HELLO-SERVICE/getbook6?ids={1}", Book[].class,
				StringUtils.join(ids, ","));
		return Arrays.asList(books);
	}

}
