package com.rigsby.webmvc;

import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.rigsby.customized.CustomizedThreadLocalCurrentTraceContext;

import brave.Tracing;

@EnableWebMvc
@RestController
@Configuration
@CrossOrigin // So that javascript can be hosted elsewhere
public class Frontend {
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	Tracing tracing;

	Logger logger = LoggerFactory.getLogger(Frontend.class);

	public static ForkJoinPool forkJoinPool = null;

	@RequestMapping("/")
	public String frontend() throws InterruptedException {

		logger.info("Frontend Start");

		// Init fork join pool only once
		if (forkJoinPool == null) {
			forkJoinPool = ((CustomizedThreadLocalCurrentTraceContext) tracing.currentTraceContext())
					.getTraceableForkJoinPool(4, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, false);
		}

		// Submit a ForkJoinTask
		// [Success]
		forkJoinPool.submit(new CustomRecursiveTask("111111"));

		// Submit a Runnable, which will init a new ForkJoinTask and fork
		// [Failed]
		forkJoinPool.submit(new CustomRunnable("222222"));

		return "Hello World";
	}
}
