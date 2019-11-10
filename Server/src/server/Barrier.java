package server;

public class Barrier {

	private int currentWorkers = 0;
	private int totalWorkers = 0;
	private int passedWaiters = 0;
	private int totalWaiters = 1;

	public Barrier(int totalWorkers) {
		this.totalWorkers = totalWorkers;
	}

	public synchronized void barrierWait() {
		while (currentWorkers != totalWorkers) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		passedWaiters++;
		if (passedWaiters == totalWaiters) {
			currentWorkers = 0;
			passedWaiters = 0;
			notifyAll();
		}
	}

	public synchronized void barrierPost() {
		currentWorkers++;
		if (currentWorkers == totalWorkers) {
			notifyAll();
		}
	}
}
