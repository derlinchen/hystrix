package com.climber.consumer.command;

import org.springframework.web.client.RestTemplate;

import com.climber.commons.Book;
import com.netflix.hystrix.HystrixCommand;

/**
 * 自定义断路器调用的方法
 * @author Derlin
 *
 */
public class BookCommand extends HystrixCommand<Book> {
	
	private RestTemplate restTemplate;
	private long id;
	
	public BookCommand(Setter setter, RestTemplate restTemplate) {
		super(setter);
		this.restTemplate = restTemplate;
	}

	/**
	 * 自定义Hystrix进行降级处理，同时进行异常处理
	 */
	@Override
	protected Book getFallback() {
		Throwable executionException = getExecutionException();
		System.out.println(executionException.getMessage());
		return new Book("宋诗选注", 88, "钱钟书", "三联书店");
	}
	
//	@Override
//	protected Book run() throws Exception {
//		return restTemplate.getForObject("http://HELLO-SERVICE/getbook1", Book.class);
//	}
	
	

	public BookCommand(Setter setter, RestTemplate restTemplate, long id) {
		super(setter);
		this.restTemplate = restTemplate;
		this.id = id;
	}
	
	@Override
	protected Book run() throws Exception {
		return restTemplate.getForObject("http://HELLO-SERVICE/getbook5", Book.class);
	}
	
	/**
	 * 开启缓存处理
	 */
	@Override
	protected String getCacheKey() {
		return String.valueOf(id);
	}
}
