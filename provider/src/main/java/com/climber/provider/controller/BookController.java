package com.climber.provider.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.climber.commons.Book;

@RestController
public class BookController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private DiscoveryClient client;

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello() {
		List<ServiceInstance> instances = client.getInstances("hello-service");
		for (int i = 0; i < instances.size(); i++) {
			ServiceInstance instance = instances.get(i);
			logger.info("/hello,host:" + instance.getHost() + ",serivce_id:" + instance.getServiceId());
		}
		return "hello";
	}
	
	@RequestMapping(value = "/hello1", method = RequestMethod.GET)
	public String hello1(@RequestParam String name) {
	    return "hello " + name + "!";
	}
	
	@RequestMapping(value = "/hello2", method = RequestMethod.GET)
	public Book hello2(@RequestHeader String name, @RequestHeader String author, @RequestHeader Integer price) throws UnsupportedEncodingException {
	    Book book = new Book();
	    book.setName(URLDecoder.decode(name,"UTF-8"));
	    book.setAuthor(URLDecoder.decode(author,"UTF-8"));
	    book.setPrice(price);
	    System.out.println(book);
	    return book;
	}

	@RequestMapping(value = "/hello3", method = RequestMethod.POST)
	public String hello3(@RequestBody Book book) {
	    return "书名为：" + book.getName() + ";作者为：" + book.getAuthor();
	}

	@RequestMapping(value = "/sayhello", method = RequestMethod.GET)
	public String sayHello(String name) {
		return "hello " + name;
	}

	@RequestMapping(value = "/getbook1", method = RequestMethod.GET)
	public Book book1() {
		return new Book("三国演义", 90, "罗贯中", "花城出版社");
	}

	@RequestMapping(value = "/getbook2", method = RequestMethod.POST)
	public Book book2(@RequestBody Book book) {
		System.out.println(book.getName());
		book.setPrice(33);
		book.setAuthor("曹雪芹");
		book.setPublisher("人民文学出版社");
		return book;
	}

	@RequestMapping(value = "/getbook3/{id}", method = RequestMethod.PUT)
	public void book3(@RequestBody Book book, @PathVariable int id) {
		logger.info("book:" + book);
		logger.info("id:" + id);
	}

	@RequestMapping(value = "/getbook4/{id}", method = RequestMethod.DELETE)
	public void book4(@PathVariable int id) {
		logger.info("id:" + id);
	}
	
	@RequestMapping(value = "/getbook5/{id}", method = RequestMethod.GET)
	public Book book5(@PathVariable Integer id) {
		System.out.println(">>>>>>>>/getbook5/{id}");
	    if (id == 1) {
	        return new Book("《李自成》", 55, "姚雪垠", "人民文学出版社");
	    } else if (id == 2) {
	        return new Book("中国文学简史", 33, "林庚", "清华大学出版社");
	    }
	    return new Book("文学改良刍议", 33, "胡适", "无");
	}

	@RequestMapping("/getbook6")
	public List<Book> book6(String ids) {
	    System.out.println("ids>>>>>>>>>>>>>>>>>>>>>" + ids);
	    ArrayList<Book> books = new ArrayList<>();
	    books.add(new Book("《李自成》", 55, "姚雪垠", "人民文学出版社"));
	    books.add(new Book("中国文学简史", 33, "林庚", "清华大学出版社"));
	    books.add(new Book("文学改良刍议", 33, "胡适", "无"));
	    books.add(new Book("ids", 22, "helloworld", "haha"));
	    return books;
	}

	@RequestMapping("/getbook6/{id}")
	public Book book61(@PathVariable Integer id) {
	    Book book = new Book("《李自成》2", 55, "姚雪垠2", "人民文学出版社2");
	    return book;
	}
}
