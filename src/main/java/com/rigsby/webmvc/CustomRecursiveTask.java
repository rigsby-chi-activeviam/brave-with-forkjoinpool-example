package com.rigsby.webmvc;

import java.util.concurrent.RecursiveTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomRecursiveTask extends RecursiveTask<Integer> {

	Logger logger = LoggerFactory.getLogger(CustomRecursiveTask.class);

	private String str;

	public CustomRecursiveTask(String str) {
		this.str = str;
	}

	@Override
	protected Integer compute() {
		logger.info(String.format("[%s] Inside ForkJoinTask", str));
		return 0;
	}
}