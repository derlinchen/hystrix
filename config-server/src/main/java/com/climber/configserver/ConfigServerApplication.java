package com.climber.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	// 访问地址为http://localhost:2007/app/prod/master
	// app表示在git上传的app.properties
	// prod表示环境，配置还有dev\test\prod\default
	// master表示在github上的分支
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}
}
