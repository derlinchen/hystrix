package com.climber.rabbitmq.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.climber.rabbitmq.BusRabbitmqApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusRabbitmqApplication.class)
public class RabbitmqHelloApplicationTests {
	@Autowired
    private Sender sender;

    @Test
    public void contextLoads() {
        sender.send();
    }
}
