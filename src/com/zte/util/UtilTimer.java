package com.zte.util;

import java.util.Timer;
import java.util.TimerTask;

public class UtilTimer {

	private Timer timer = null;

	private TimerTask task = null;

	//private Class taskClass = null;

	private boolean isTimerStart = false;

	public UtilTimer(TimerTask task) {

		this.timer = new Timer();
		//this.taskClass = task.getClass();
		this.task = task;
	}

	public void start(int delay, int period) {

		if (isTimerStart) {
			stop();
		}

//		try {
//			task = (TimerTask) taskClass.newInstance();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		timer.schedule(task, delay, period); // 利用timer.schedule方法
		isTimerStart = true;
	}

	public void stop() {

		if (isTimerStart) {
			task.cancel();
			isTimerStart = false;
		}
	}
}
