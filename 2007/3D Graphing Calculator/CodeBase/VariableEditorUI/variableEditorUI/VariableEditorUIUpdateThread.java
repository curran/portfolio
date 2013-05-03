package variableEditorUI;

import java.util.Observable;

/**
 * The class responsible for runnung a thread which updates it's Observables
 * every second. It is an Observable which sends out notifications passing
 * <code>VariableEditorUIUpdateThread.UPDATE_ARGUMENT</code> as the argument.
 * 
 * @author Curran Kelleher
 * 
 */
public class VariableEditorUIUpdateThread extends Observable implements
		Runnable {

	/**
	 * The object which is passed as the argument of notifications.
	 */
	public static final Object UPDATE_ARGUMENT = "This update came from VariableEditorUIUpdateThread";

	/**
	 * The singleton instance of VariableEditorUIUpdateThread.
	 */
	private static VariableEditorUIUpdateThread INSTANCE = null;

	/**
	 * The constructor is private because this class is a singleton.
	 * 
	 */
	private VariableEditorUIUpdateThread() {
		// start the update thread
		(new Thread(this)).start();
	}

	/**
	 * Gets the singleton instance of VariableEditorUIUpdateThread
	 * 
	 * @return the singleton instance of VariableEditorUIUpdateThread
	 */
	public static VariableEditorUIUpdateThread getInstance() {
		if (INSTANCE == null)
			INSTANCE = new VariableEditorUIUpdateThread();
		return INSTANCE;
	}

	/**
	 * Called to start the update thread. Notifies all registered Observers
	 * every 1000 milliseconds.
	 * 
	 */
	public void run() {
		while (true) {
			// wait for 1000 ms
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// notify all Observers
			if (countObservers() > 0) {
				setChanged();
				notifyObservers(UPDATE_ARGUMENT);
			}
			//System.out.println("countObservers() = "+countObservers());
		}
	}
}
