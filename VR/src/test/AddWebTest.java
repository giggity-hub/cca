package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import dbadapter.DBFacade;
import junit.framework.TestCase;
import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.After;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Date;

public class AddWebTest extends TestCase {
	
	public AddWebTest() {
		super();
	}
	
	WebTester tester;
	
	@Before
	public void setUp() throws Exception {
		tester = new WebTester();
		tester.setBaseUrl("http://localhost:8080/VR/");
	}

	
	@Test
	public void testAddAppointment() {
		
//		DBFacade stub = mock(DBFacade.class);
//		DBFacade.setInstance(stub);
		
		
		
		tester.beginAt("add");
		
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

}


