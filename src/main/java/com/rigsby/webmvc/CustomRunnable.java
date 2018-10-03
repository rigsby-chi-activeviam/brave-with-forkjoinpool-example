package com.rigsby.webmvc;

import java.util.concurrent.ForkJoinTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomRunnable implements Runnable {
	Logger logger = LoggerFactory.getLogger(CustomRunnable.class);

	private String str;

	public CustomRunnable(String str) {
		this.str = str;
	}

	@Override
	public void run() {
		logger.info(String.format("[%s] Inside Runnable", str));
		ForkJoinTask task = new CustomRecursiveTask(str);
		task.fork();
	}

}