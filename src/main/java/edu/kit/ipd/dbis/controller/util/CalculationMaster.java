package edu.kit.ipd.dbis.controller.util;

import java.util.List;

public class CalculationMaster {
	public static void executeCalculation(List<Thread> jobs) {
		try {
			int runningJobs = 0;
			final int maxJobs = 8 * Runtime.getRuntime().availableProcessors();

			for (Thread job : jobs) {
				job.start();
				if (runningJobs < maxJobs) {
					runningJobs++;
				} else {
					job.join();
				}
			}
			for (Thread job : jobs) {
				job.join();
			}
		} catch (InterruptedException ignored) { }
	}
}
