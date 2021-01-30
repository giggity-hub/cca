package tests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.Test;

import datatypes.Appointment;
import datatypes.PossibleDate;

import org.junit.Before;




public class AppointmentTest {
	
	Appointment a;
	ArrayList<PossibleDate> possibleDates;
	@Before
	public void setUp() throws Exception{
		Date date = java.sql.Date.valueOf("2021-01-01");
		a = new Appointment(1, 1, "test", "test", "test", 1, date , false );
		
		possibleDates = new ArrayList<PossibleDate>();
	}
	
	@Test
	public void testMakeFinal() {
		
		//1. Test
		//There is only one possible dates
		Date d1 = Date.valueOf("2020-02-02");
		possibleDates.add(new PossibleDate(d1, 1, 1));
		a.setPossibleDates(possibleDates);
		assertEquals(d1, a.makeFinal());
		assertEquals(a.isFinal, true);
		
		//reset
		possibleDates.clear();

		//2. Test
		//There are three possible dates
		d1 = Date.valueOf("2020-02-02");
		Date d2 = Date.valueOf("2020-02-02");
		Date d3 = Date.valueOf("2020-03-03");
		
		possibleDates.add(new PossibleDate(d1, 1, 1));
		possibleDates.add(new PossibleDate(d2, 1, 1));
		possibleDates.add(new PossibleDate(d3, 1, 1));
		a.setPossibleDates(possibleDates);
		assertEquals(d1, a.makeFinal());
		
		//reset
		possibleDates.clear();

		//3. Test
		//There are 5 possible dates
		d1 = Date.valueOf("2020-02-02");
		d2 = Date.valueOf("2020-02-02");
		d3 = Date.valueOf("2020-03-03");
		Date d4 = Date.valueOf("2020-04-04");
		Date d5 = Date.valueOf("2020-05-05");
		
		possibleDates.add(new PossibleDate(d1, 1, 1));
		possibleDates.add(new PossibleDate(d2, 1, 1));
		possibleDates.add(new PossibleDate(d3, 1, 1));
		possibleDates.add(new PossibleDate(d4, 1, 1));
		possibleDates.add(new PossibleDate(d5, 1, 1));
		a.setPossibleDates(possibleDates);
		assertEquals(d1, a.makeFinal());
	}

}
