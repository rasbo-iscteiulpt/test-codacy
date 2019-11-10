package server;

import java.util.LinkedList;

public class BlockingQueue<T> {
	private LinkedList<T> elements = new LinkedList<>();

	public synchronized void offer(T elem) throws InterruptedException {
		elements.offer(elem);
		notifyAll();
	}

	public synchronized T poll() throws InterruptedException {
		while (elements.isEmpty()) {
			wait();
		}
		if (elements.size() != -1) {
			notifyAll();
		}
		return elements.poll();
	}

	public int size() {
		return elements.size();
	}

	public void clear() {
		elements.clear();
	}
}
