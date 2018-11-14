package com.climber.feign.service;

import org.springframework.cloud.openfeign.FeignClient;

import com.climber.api.service.HelloService;

@FeignClient("hello-service")
public interface HelloService1 extends HelloService {

}
