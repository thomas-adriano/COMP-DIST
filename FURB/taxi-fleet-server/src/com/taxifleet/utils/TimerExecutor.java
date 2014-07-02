package com.taxifleet.utils;

import com.taxifleet.server.network.socket.Listener;

public class TimerExecutor {

	private Thread thread;

	public void execute(Listener runnable) {
		thread = new Thread(runnable);
		thread.start();
	}

	public void interruptExecution() {
		if (thread == null)
			return;
		
		if (thread.isAlive() && !thread.isInterrupted())
			thread.interrupt();
	}

	public static void sleepCurrentThread(long timeMillis) {
		try {
			Thread.sleep(timeMillis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
