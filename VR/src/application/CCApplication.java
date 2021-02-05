package application;

import java.sql.Date;
import java.util.ArrayList;

import datatypes.Appointment;
import datatypes.PossibleDate;
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
	
	public ArrayList<Appointment> getCalendar(int userid) {
		return DBFacade.getInstance().getAppointmentsWithPossibleDates(userid);
	}
	
	public ArrayList<Appointment> getReplyed(int userid) {
		return DBFacade.getInstance().getAppointmentsWithReplyedDates(userid, true);
	}
	
	public ArrayList<Appointment> getReplyedNotFinal(int userid) {
		return DBFacade.getInstance().getAppointmentsWithReplyedDates(userid, false);
	}
	
	public ArrayList<PossibleDate> getPossibleDates(int aid) {
		return DBFacade.getInstance().getPossibleDates(aid);
	}
	
	public ArrayList<PossibleDate> getReplyedDatesNotFinal(int aid, int userid) {
		return DBFacade.getInstance().getReplyedDates(aid, userid);
	}
	
	public void replyingToAppointment(int aid, int mid, ArrayList<Date> pds ) {
		DBFacade.getInstance().replyingToAppointment(aid, mid, pds);
	}
	
	
}
