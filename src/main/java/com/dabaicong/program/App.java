package com.dabaicong.program;

/**
 * Hello world!
 *
 */
public class App implements Runnable {

	public void run() {
		for (int i = 1; true; i++) {
			System.out.println("test" + Thread.currentThread().getName());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
