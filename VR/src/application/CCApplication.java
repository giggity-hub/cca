package application;

import dbadapter.DBFacade;

public class CCApplication {
	
	private static CCApplication instance;

	/**
	 * Implementation of the Singleton pattern.
	 * 
	 * @return
	 */
	public static CCApplication getInstance() {
		if (instance == null) {
			instance = new CCApplication();
		}

		return instance;
	}
	
	
	public void checkDeadlines() {
		DBFacade.getInstance().finalizingAppointment();
	}
	
	
}
