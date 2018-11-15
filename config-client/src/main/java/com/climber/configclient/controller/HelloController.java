package com.climber.configclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class HelloController {

	@Value("${climber}")
    String climber;
	
    @Autowired
    Environment env;

    @RequestMapping("/climber")
    public String climber() {
        return this.climber;
    }
    @RequestMapping("/climber1")
    public String climber1() {
        return env.getProperty("climber", "未定义");
    }
	
}
