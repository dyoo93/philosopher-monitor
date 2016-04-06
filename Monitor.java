/**
 * Class Monitor To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor {
	/*
	 * ------------ Data members ------------
	 */
	public enum states {
		THINKING, EATING, HUNGRY, TALKING
	}

	private states[] philState;

	private boolean someoneTalking = false;

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers) {
		philState = new states[piNumberOfPhilosophers];

		for (int i = 0; i < philState.length; i++) {
			philState[i] = states.THINKING;
		}
	}

	/*
	 * ------------------------------- User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void pickUp(final int piTID)
			throws InterruptedException {

		philState[piTID - 1] = states.HUNGRY;

		while (NeighborEating(piTID - 1)) {
			wait();
		}

		philState[piTID - 1] = states.EATING;

	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID) {
		philState[piTID - 1] = states.THINKING;
		notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy (while she is not
	 * eating).
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void requestTalk() throws InterruptedException {

		while (someoneTalking == true) {
			wait();
		}

		someoneTalking = true;

	}

	/**
	 * When one philosopher is done talking stuff, others can feel free to start
	 * talking.
	 */
	public synchronized void endTalk() {
		someoneTalking = false;
		notifyAll();
	}

	public synchronized boolean checkTalking() {

		for (int i = 0; i < philState.length; i++) {
			if (philState[i] == states.TALKING) {
				return true;
			}
		}

		return false;

	}

	public synchronized boolean NeighborEating(int x) {

		if (philState[(philState.length + x - 1) % philState.length] == states.EATING
				|| philState[(x + 1) % philState.length] == states.EATING) {
			return true;
		} else {
			return false;
		}

	}
}

// EOF
