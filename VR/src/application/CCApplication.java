package application;

import java.sql.Date;

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
	
	public void createAppointment(int creator, Date[] dates, int[] participants, String description, String name,
			String location, int duration, Date deadline, int groupId) {
		DBFacade.getInstance().creatingAppointment(creator, dates, participants, description, name, location, duration, deadline, groupId);
		
	}
	
	
}
