package com.climber.feign.service.imp;

import org.springframework.stereotype.Component;

import com.climber.commons.Book;
import com.climber.feign.service.HelloService;

@Component
public class HelloServiceFallback implements HelloService {

	@Override
    public String hello() {
        return "hello error";
    }

    @Override
    public String hello(String name) {
        return "error " + name;
    }

    @Override
    public Book hello(String name, String author, Integer price) {
        Book book = new Book();
        book.setName("error");
        return book;
    }

    @Override
    public String hello(Book book) {
        return "error book";
    }
}
