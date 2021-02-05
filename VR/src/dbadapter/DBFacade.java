package dbadapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
//import java.util.Date;
import java.util.Calendar;
import java.sql.Date;

import datatypes.AddressData;
import datatypes.Appointment;
import datatypes.GuestData;
import datatypes.PossibleDate;
import interfaces.IHolidayOffer;

/**
 * Class which acts as the connector between application and database. Creates
 * Java objects from SQL returns. Exceptions thrown in this class will be
 * catched with a 500 error page.
 * 
 * @author swe.uni-due.de
 *
 */
public class DBFacade {
	private static DBFacade instance;

	/**
	 * Implementation of the Singleton pattern.
	 * 
	 * @return
	 */
	public static DBFacade getInstance() {
		if (instance == null) {
			instance = new DBFacade();
		}

		return instance;
	}
	
	/**
	 * Constructor which loads the corresponding driver for the chosen database type
	 */
	private DBFacade() {
		try {
			Class.forName("com." + Configuration.getType() + ".jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void setInstance(DBFacade instance) {
		DBFacade.instance = instance;
	}
	
	public void replyingToAppointment(int aid, int mid, ArrayList<Date> pds ) {
		String deletPrevious = "DELETE FROM possibleDates WHERE aid = ? AND mid = ?";
		String insertPossibleDate = "INSERT INTO possibleDates (aid, mid, date) VALUES (?, ?, ?)";
		
		
		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {
			
			try (PreparedStatement psDelete = connection.prepareStatement(deletPrevious)) {
				psDelete.setInt(1, aid);
				psDelete.setInt(2, mid);
				psDelete.executeUpdate();
			}
			
			try (PreparedStatement psInsert = connection.prepareStatement(insertPossibleDate)) {
				
				for (Date date: pds) {
					psInsert.setInt(1, aid);
					psInsert.setInt(2, mid);
					psInsert.setDate(3, date);
					psInsert.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public void creatingAppointment(int creator, Date[] dates, int[] participants, String description, String name,
			String location, int duration, Date deadline, int groupId) {
		// TODO Auto-generated method stub
		System.out.println(creator);
		
		
		String insertAppointment = "INSERT INTO appointments (groupId, description, name, location, duration, deadline, isFinal)VALUES (?, ?, ?, ?, ?, ?, false)";
		
		String insertPossibleDate = "INSERT INTO possibleDates (date, aid, mid) VALUES (?, ?, ?)";
		String insertParticipant = "INSERT INTO participants (mid, aid) VALUES (?, ?)";
		
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psInsertApt = connection.prepareStatement(insertAppointment, Statement.RETURN_GENERATED_KEYS);
					PreparedStatement psInsertPD = connection.prepareStatement(insertPossibleDate);
					PreparedStatement psInsertPar = connection.prepareStatement(insertParticipant)){
				psInsertApt.setInt(1, groupId);
				psInsertApt.setString(2, description);
				psInsertApt.setString(3, name);
				psInsertApt.setString(4, location);
				psInsertApt.setInt(5, duration);
				psInsertApt.setDate(6, deadline);
				psInsertApt.executeUpdate();
//				psDelete.executeUpdate();
				try (ResultSet generatedKeys = psInsertApt.getGeneratedKeys()){
					if(generatedKeys.next()) {
						int aid = Math.toIntExact(generatedKeys.getLong(1));
//						System.out.println(generatedKeys.getLong(1));
						
						for (Date d : dates) {
							psInsertPD.setDate(1, d);
							psInsertPD.setInt(2, aid);
							psInsertPD.setInt(3, creator);
							psInsertPD.executeUpdate();
						}
						
						for (int mid : participants) {
							psInsertPar.setInt(1, mid);
							psInsertPar.setInt(2, aid);
							psInsertPar.executeUpdate();
						}
						//und noch einmal für den creator
						psInsertPar.setInt(1, creator);
						psInsertPar.setInt(2, aid);
						psInsertPar.executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * Die finalizingAppointment methode halt!!
	 */
	public void finalizingAppointment() {
		System.out.println("die finalizing methode wurde gecalled");
		// Declare necessary SQL statement.
		String selectAppointmentsOverdue = "SELECT * FROM appointments WHERE (isFinal=false) AND deadline < ?";
		String selectPossibleDates = "SELECT * FROM possibleDates WHERE aid=?";
		String setFinal = "UPDATE appointments SET isFinal = true WHERE aid=?";

		String deletePossibleDates = "DELETE FROM possibleDates WHERE aid =? AND date !=?";
		String deleteParticipants = "DELETE FROM participants WHERE aid=? AND mid NOT IN (SELECT mid FROM possibleDates WHERE aid=? AND date=?)";
		// Update Database.
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psSelect = connection.prepareStatement(selectAppointmentsOverdue);
					PreparedStatement psSelectPDs = connection.prepareStatement(selectPossibleDates);
					PreparedStatement psSetFinal = connection.prepareStatement(setFinal);
					PreparedStatement psDeletePDs = connection.prepareStatement(deletePossibleDates);
					PreparedStatement psDeletePars = connection.prepareStatement(deleteParticipants)) {
				//set today
				Calendar calendar = Calendar.getInstance();
				java.sql.Date today = new java.sql.Date(calendar.getTime().getTime());
				psSelect.setDate(1, today);
				try(ResultSet rs = psSelect.executeQuery()){
					//loop over every appointment in SQL res
					while (rs.next()) {

						psSelectPDs.setInt(1, rs.getInt("aid"));
						try (ResultSet pdrs = psSelectPDs.executeQuery()) {
							Appointment a = new Appointment(rs,pdrs);
						
							if (a.possibleDates.size() > 0) {
								Date finalDate = a.makeFinal();
								System.out.println(finalDate);
								
								//Update the isFinal column for the appointment
								psSetFinal.setInt(1, a.aid);
								psSetFinal.executeUpdate();
								//remove all possible dates which are not on the final Date
								psDeletePDs.setInt(1, a.aid);
								psDeletePDs.setDate(2, finalDate);
								psDeletePDs.executeUpdate();
								//remove all participants who can't attent on the final Date
								psDeletePars.setInt(1, a.aid);
								psDeletePars.setInt(2, a.aid);
								psDeletePars.setDate(3, finalDate);
								psDeletePars.executeUpdate();
								//
								System.out.println(a.aid);
								System.out.println(a.isFinal);
							}else {
								System.out.println("Yooooo Das Appointment hat keine PossibleDates. Die Precondition wurde violated und sou");
							}
						}
						
						

					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Appointment> getAppointmentsWithPossibleDates(int userId) {

		String sel = "SELECT * FROM Appointments WHERE aid in (SELECT aid FROM Participants WHERE mid = ?)";
		ArrayList<Appointment> res = null;

		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {

			try (PreparedStatement psapt = connection.prepareStatement(sel)){
				psapt.setInt(1, userId);

				try (ResultSet rs = psapt.executeQuery()) {
					res = new ArrayList<>();

					for (int i = 0; rs.next(); i++) {
						res.add(new Appointment(rs));
						res.get(i).setPossibleDates(getPossibleDates(rs.getInt("aid")));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;

	}
	
	public ArrayList<Appointment> getAppointmentsWithReplyedDates(int userid, boolean withFinalDates) {

		String sel = "SELECT * FROM Appointments WHERE aid in (SELECT aid FROM Participants WHERE mid = ?)";
		sel += withFinalDates ? "" : " AND isFinal = 0";
		ArrayList<Appointment> res = null;

		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {

			try (PreparedStatement psapt = connection.prepareStatement(sel)){
				psapt.setInt(1, userid);

				try (ResultSet rs = psapt.executeQuery()) {
					res = new ArrayList<>();

					for (int i = 0; rs.next(); i++) {
						res.add(new Appointment(rs));
						res.get(i).setPossibleDates(getReplyedDates(rs.getInt("aid"), userid));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;

	}
	
	public ArrayList<PossibleDate> getPossibleDates(int aid) {
		String sel = "SELECT * FROM PossibleDates WHERE aid = ? GROUP BY date";
		ArrayList<PossibleDate> res = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {

			try (PreparedStatement ps = connection.prepareStatement(sel)) {
				ps.setInt(1, aid);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					res.add(new PossibleDate(rs));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}
	
	public ArrayList<PossibleDate> getReplyedDates(int aid, int userid) {
		String sel = "SELECT * FROM PossibleDates WHERE aid = ? AND mid = ?";
		ArrayList<PossibleDate> res = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {

			try (PreparedStatement ps = connection.prepareStatement(sel)) {
				ps.setInt(1, aid);
				ps.setInt(2, userid);
				
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					res.add(new PossibleDate(rs));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}


	
}
