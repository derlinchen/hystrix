package com.climber.feign.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climber.commons.Book;
import com.climber.feign.service.HelloService1;

@RestController
public class FeignController {

	@Autowired
	HelloService1 helloService1;

	@RequestMapping("/fhello1")
	public String hello1() {
		return helloService1.hello("张三");
	}

	@RequestMapping(value = "/fhello2")
	public Book hello2() throws UnsupportedEncodingException {
		Book book = helloService1.hello(URLEncoder.encode("三国演义", "UTF-8"), URLEncoder.encode("罗贯中", "UTF-8"), 33);
		System.out.println(book);
		return book;
	}

	@RequestMapping("/fhello3")
	public String hello3() {
		Book book = new Book();
		book.setName("红楼梦");
		book.setPrice(44);
		book.setAuthor("曹雪芹");
		return helloService1.hello(book);
	}
}
