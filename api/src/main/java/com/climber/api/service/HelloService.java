package com.climber.api.service;

import java.io.UnsupportedEncodingException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.climber.commons.Book;

@RequestMapping("/hs2")
public interface HelloService {

	@RequestMapping(value = "/hello1", method = RequestMethod.GET)
    String hello(@RequestParam("name") String name);

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    Book hello(@RequestHeader("name") String name, @RequestHeader("author") String author, @RequestHeader("price") Integer price) throws UnsupportedEncodingException;

    @RequestMapping(value = "/hello3", method = RequestMethod.POST)
    String hello(@RequestBody Book book);
    
}
