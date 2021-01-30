package dbadapter;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import datatypes.Appointment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DBFacadeTest {
	int creator = 69;
	Date[] dates = {Date.valueOf("2020-01-01"), Date.valueOf("2020-02-02")};
	int[] participants = {1,2};
	String description = "testDescription";
	String name = "testName";
	String location = "testLocation";
	int duration = 1;
	Date deadline = Date.valueOf("2020-03-03");
	int groupId = 420;


	@Before
	public void setUp() throws Exception {
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
	public void testCreatingAppointment() {
		DBFacade.getInstance().creatingAppointment(creator, dates, participants, description, name, location, duration, deadline, groupId);
		
		String selectAppointments = "SELECT * FROM appointments";
		String selectPossibleDates = "SELECT * FROM possibleDates";
		String selectParticipants = "SELECT * FROM participants";
		
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {

			try (PreparedStatement psSelectApts = connection.prepareStatement(selectAppointments);
					PreparedStatement psSelectPDs = connection.prepareStatement(selectPossibleDates);
					PreparedStatement psSelectPars = connection.prepareStatement(selectParticipants);) {
				
				int aid = 0;
				//exactly one appointment with the specified information was created 
				try (ResultSet rs = psSelectApts.executeQuery()){
					if (rs.next()) {
						assertEquals(groupId, rs.getInt("groupId"));
						assertEquals(description, rs.getString("description"));
						assertEquals(name, rs.getString("name"));
						assertEquals(location, rs.getString("location"));
						assertEquals(duration, rs.getInt("duration"));
						assertEquals(deadline, rs.getDate("deadline"));
						assertEquals(false, rs.getBoolean("isFinal"));
						
						aid = rs.getInt("aid");
					}
					//no other appointment was created
					assertEquals(false, rs.next());
				}
				
				//for every entered date there exists a possible date associated with the creator
				try (ResultSet rs = psSelectPDs.executeQuery()){
					ArrayList<Date> pds = new ArrayList<Date>();
					while (rs.next()) {
						assertEquals(aid, rs.getInt("aid"));
						assertEquals(creator, rs.getInt("mid"));
						pds.add(rs.getDate("date"));
					}
					assertEquals(2, pds.size());
					assertTrue(pds.contains(dates[0]));
					assertTrue(pds.contains(dates[1]));
				}
				
				//the creator and every invited member are stored as participants for the appointment
				try (ResultSet rs = psSelectPars.executeQuery()){
					ArrayList<Integer> pars = new ArrayList<Integer>();
					while (rs.next()) {
						assertEquals(aid, rs.getInt("aid"));
						pars.add(rs.getInt("mid"));
					}
					assertEquals(3, pars.size());
					assertTrue(pars.contains(participants[0]));
					assertTrue(pars.contains(participants[1]));
					assertTrue(pars.contains(creator));
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@After
	public void tearDown() throws Exception {
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

	

}
