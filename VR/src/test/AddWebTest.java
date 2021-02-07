package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import dbadapter.Configuration;
import dbadapter.DBFacade;
import junit.framework.TestCase;
import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.After;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddWebTest extends TestCase {
	
	public AddWebTest() {
		super();
	}
	
	WebTester tester;
	
	@Before
	public void setUp() throws Exception {
		tester = new WebTester();
		tester.setBaseUrl("http://localhost:8080/VR/");
		
		//clear database
		String deleteAppointments = "DELETE FROM appointments";
		String deleteParticipants = "DELETE FROM participants";
		String deletePossibleDates = "DELETE FROM possibleDates";
		
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {

			try (PreparedStatement psDeleteApts = connection.prepareStatement(deleteAppointments);
					PreparedStatement psDeletePars = connection.prepareStatement(deleteParticipants);
					PreparedStatement psDeletePDs = connection.prepareStatement(deletePossibleDates);) {
				psDeleteApts.executeUpdate();
				psDeletePars.executeUpdate();
				psDeletePDs.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Test
	public void testAddAppointment() {
		
//		DBFacade stub = mock(DBFacade.class);
//		DBFacade.setInstance(stub);
		tester.beginAt("add");
//		Signing in
		tester.setTextField("userid", "5");
		tester.clickButton("signIn");
		tester.gotoPage("add");
		
		//check all components of the input form
		tester.assertFormPresent();
		tester.assertTextPresent("Name");
		tester.assertFormElementPresent("name");
		tester.assertTextPresent("Participants");
		tester.assertFormElementPresent("participants");
		tester.assertTextPresent("Dates");
		tester.assertFormElementPresent("date1");
		tester.assertTextPresent("Description");
		tester.assertFormElementPresent("description");
		tester.assertTextPresent("Location");
		tester.assertFormElementPresent("location");
		tester.assertTextPresent("Duration");
		tester.assertFormElementPresent("duration");
		tester.assertTextPresent("Deadline");
		tester.assertFormElementPresent("deadline");
		tester.assertButtonPresent("addAppointment");
		
		//submit the form with given parameters
		tester.setTextField("name", "testName");
		tester.setTextField("participants", "1,2,3");
		tester.setTextField("date1", "01/01/2020");
		tester.setTextField("description", "testDescription");
		tester.setTextField("location", "testLocation");
		tester.setTextField("duration", "1");
		tester.setTextField("deadline", "02/02/2020");
		
		tester.clickButton("addAppointment");
		
		
//		int[] participants = {1,2,3};
//		Date[] dates = {java.sql.Date.valueOf("2020-01-01")};
//		verify(stub, times(1)).creatingAppointment(69, dates, participants, "testDescription", "testName", "testLocation", 1, java.sql.Date.valueOf("2020-02-02"), 420);

	}
	@Test
	public void testReplyToAppointment() {
		tester.beginAt("reply");
		
		//insert one Appointment into DB
		int[] participants = {5};
		Date[] dates = {java.sql.Date.valueOf("2020-01-01")};
		DBFacade.getInstance().creatingAppointment(69, dates, participants, "testDescription", "testName", "testLocation", 1, java.sql.Date.valueOf("2020-02-02"), 420);
		
		//login and go to invitations
		tester.setTextField("userid", "5");
		tester.clickButton("signIn");
		tester.gotoPage("reply");
		
		//confirm that the appointment is shown
		tester.assertTextPresent("Deadline");
		tester.assertTextPresent("Reply");
		
		//confirm that the details of the appointment are shown
		tester.assertTextPresent("testDescription");
		tester.assertTextPresent("testName");

		
		//click on the link to navigate to the reply page
		tester.clickLink("replyButton");
		
		//confirm that the reply form is shown
		tester.assertFormPresent();
		tester.assertTextPresent("Possible Dates");
		tester.assertTextPresent("Alternative Dates");
		
		//submit without selecting a date as possible
		tester.clickButton("addPossibleDates");
		tester.assertTextPresent("Es muss mindestens ein possible Date oder ein alternative Date angegeben werden");
		
		//go to the form again
		tester.gotoPage("reply");
		tester.clickLink("replyButton");
		
		//now select one date as possible and submit again
		tester.checkCheckbox("pddate0");
		tester.clickButton("addPossibleDates");
		tester.assertTextPresent("Deine Reply wurde gespeichert");
		
	}
	@Test
	public void testShowCalendar() {
		//insert one Appointment into DB
		int[] participants = {5};
		Date[] dates = {java.sql.Date.valueOf("2020-01-01")};
		DBFacade.getInstance().creatingAppointment(69, dates, participants, "testDescription", "testName", "testLocation", 1, java.sql.Date.valueOf("2020-02-02"), 420);
		
		tester.beginAt("calendar");
		
		//login and go to calendar
		tester.setTextField("userid", "5");
		tester.clickButton("signIn");
		tester.gotoPage("calendar");
		
		//confirm that the table is shown
		tester.assertTextPresent("Name");
		tester.assertTextPresent("Beschreibung");
		tester.assertTextPresent("Ort");
		tester.assertTextPresent("Dauer");
		tester.assertTextPresent("Daten");
		tester.assertTextPresent("Deadline");
		
		//confirm that the details of the appointment are shown
		tester.assertTextPresent("testDescription");
		tester.assertTextPresent("testName");
		tester.assertTextPresent("testLocation");
		tester.assertTextPresent("1");
	}

}


