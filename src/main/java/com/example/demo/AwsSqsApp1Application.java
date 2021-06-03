package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {ContextStackAutoConfiguration.class,ContextRegionProviderAutoConfiguration.class})
@RestController
public class AwsSqsApp1Application {
	
	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;
	
	private static Logger logger=LoggerFactory.getLogger(AwsSqsApp1Application.class);
	

	@Value("${cloud.aws.end-point.uri}")
	private String endpoint;

	/*
	 * localhost:8080/send/Welcome
	 */
	@GetMapping(value = "/send/{message}")
	public String sendMessageToQueue(@PathVariable String message) {
		queueMessagingTemplate.send(endpoint, MessageBuilder.withPayload(message).build());
		return "<h3 style='color:red'>Message Sent</h3>";
	}

	@SqsListener(value = "my-queue-3")
	public void loadMessageFromSQS(String message)
	{
			logger.info("Message from SQS Queue {}",message);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AwsSqsApp1Application.class, args);
	}

}
