package com.climber.provider.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.web.bind.annotation.RestController;

import com.climber.api.service.HelloService;
import com.climber.commons.Book;

@RestController
public class BookController1 implements HelloService {

	@Override
	public String hello(String name) {
		return "hello i'm " + name + "!";
	}

	@Override
	public Book hello(String name, String author, Integer price) throws UnsupportedEncodingException {
		Book book = new Book();
		book.setName(URLDecoder.decode(name,"UTF-8"));
        book.setAuthor(URLDecoder.decode(author,"UTF-8"));
        book.setPrice(price);
        System.out.println(book);
        return book;
	}

	@Override
	public String hello(Book book) {
		return "书名为：" + book.getName() + ";作者为：" + book.getAuthor();
	}

}
