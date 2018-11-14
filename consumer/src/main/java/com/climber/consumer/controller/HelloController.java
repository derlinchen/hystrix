package com.climber.consumer.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.climber.commons.Book;
import com.climber.consumer.command.BookCollapseCommand;
import com.climber.consumer.command.BookCommand;
import com.climber.consumer.service.BookService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;


@RestController	
public class HelloController {
	
	@Autowired
    RestTemplate restTemplate;
	
	@Autowired
	BookService bookService;
	
	@RequestMapping("/test")
    public Book test() throws InterruptedException, ExecutionException {
		BookCommand bookCommand = new BookCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")), restTemplate);
	    //同步调用
	    //Book book1 = bookCommand.execute();
	    //异步调用
	    Future<Book> queue = bookCommand.queue();
	    Book book = queue.get();
	    return book;
	}
	
	/**
	 * 异步调用断路器
	 * @return
	 */
	@RequestMapping("/test1")
	@com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
	public Book test1(){
		return new AsyncResult<Book>() {

			@Override
			public Book invoke() {
				return restTemplate.getForObject("http://HELLO-SERVICE/getbook1", Book.class);
			}
			
			public Book get() throws UnsupportedOperationException {
				return invoke();
			};
		}.get();
	}
	
	@RequestMapping("/test3")
	public Book test5() {
	    HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("commandKey");
	    HystrixRequestContext.initializeContext();
	    BookCommand bc1 = new BookCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")).andCommandKey(commandKey), restTemplate, 1l);
	    Book e1 = bc1.execute();
	    BookCommand bc2 = new BookCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")).andCommandKey(commandKey), restTemplate, 1l);
	    Book e2 = bc2.execute();
	    BookCommand bc3 = new BookCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")).andCommandKey(commandKey), restTemplate, 1l);
	    Book e3 = bc3.execute();
	    System.out.println("e1:" + e1);
	    System.out.println("e2:" + e2);
	    System.out.println("e3:" + e3);
	    return e1;
	}
	
	@RequestMapping("/test6")
	public Book test6() {
	    HystrixRequestContext.initializeContext();
	    //第一次发起请求
	    Book b1 = bookService.test6(2, "");
	    //参数和上次一致，使用缓存数据
	    Book b2 = bookService.test6(2, "");
	    //参数不一致，发起新请求
	    Book b3 = bookService.test6(2, "aa");
	    return b1;
	}
	
	
	@RequestMapping("/test7")
	@ResponseBody
	public void test7() throws ExecutionException, InterruptedException {
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
	    BookCollapseCommand bc1 = new BookCollapseCommand(bookService, 1l);
	    BookCollapseCommand bc2 = new BookCollapseCommand(bookService, 2l);
	    BookCollapseCommand bc3 = new BookCollapseCommand(bookService, 3l);
	    BookCollapseCommand bc4 = new BookCollapseCommand(bookService, 4l);
	    Future<Book> q1 = bc1.queue();
	    Future<Book> q2 = bc2.queue();
	    Future<Book> q3 = bc3.queue();
	    Book book1 = q1.get();
	    Book book2 = q2.get();
	    Book book3 = q3.get();
	    Thread.sleep(3000);
	    Future<Book> q4 = bc4.queue();
	    Book book4 = q4.get();
	    System.out.println("book1>>>"+book1);
	    System.out.println("book2>>>"+book2);
	    System.out.println("book3>>>"+book3);
	    System.out.println("book4>>>"+book4);
	    context.close();
	}
	
	/**
	 * 将请求合并
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@RequestMapping("/test8")
	@ResponseBody
	public void test8() throws ExecutionException, InterruptedException {
	    HystrixRequestContext context = HystrixRequestContext.initializeContext();
	    Future<Book> f1 = bookService.test10(1l);
	    Future<Book> f2 = bookService.test10(2l);
	    Future<Book> f3 = bookService.test10(3l);
	    Book b1 = f1.get();
	    Book b2 = f2.get();
	    Book b3 = f3.get();
	    Thread.sleep(3000);
	    Future<Book> f4 = bookService.test10(4l);
	    Book b4 = f4.get();
	    System.out.println("b1>>>"+b1);
	    System.out.println("b2>>>"+b2);
	    System.out.println("b3>>>"+b3);
	    System.out.println("b4>>>"+b4);
	    context.close();
	}

}
