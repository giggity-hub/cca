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
public class DBFacade implements IHolidayOffer {
	private static DBFacade instance;

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

	public static void setInstance(DBFacade dbfacade) {
		instance = dbfacade;
	}
	
	public void replyingToAppointment(int userId, int aid, ArrayList<Date> pds ) {
		
	}
	
	public ArrayList<Appointment> getInvitations(int userId, int groupId){
		ArrayList<Appointment> apts = new ArrayList<Appointment>();
		
		apts.add(new Appointment(88, 88, "description1", "name1", "loc", 500, Date.valueOf("2020-02-02"), false));
		apts.add(new Appointment(89, 88, "description1", "name2", "loc", 500, Date.valueOf("2020-02-02"), false));
		apts.add(new Appointment(90, 88, "description1", "name3", "loc", 500, Date.valueOf("2020-02-02"), false));
		
		return apts;
	}
	
	public ArrayList<String> getPossibleDates(int aid){
		ArrayList<String> pds = new ArrayList<String>();
		pds.add(Date.valueOf("2020-02-02").toString());
		pds.add(Date.valueOf("2020-02-03").toString());
		pds.add(Date.valueOf("2020-02-04").toString());
		return pds;
//		return 
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
	
	public Appointment[] getCalendar(int userId) {

		String selapt = "SELECT * FROM Appointments WHERE aid in (SELECT aid FROM Participants WHERE mid = ?)";
		String selpd = "SELECT * FROM PossibleDates WHERE aid = ?";
		Appointment[] res = null;

		try (Connection connection = DriverManager.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {

			try (PreparedStatement psapt = connection.prepareStatement(selapt);
					PreparedStatement pspd = connection.prepareStatement(selpd)) {
				psapt.setInt(1, userId);

				try (ResultSet rs = psapt.executeQuery()) {
					int length = 0;
					if (rs.last()) {
						length = rs.getRow();
						rs.beforeFirst();
					}
					res = new Appointment[length];

					for (int i = 0; i < length && rs.next(); i++) {

						pspd.setInt(1, rs.getInt("aid"));

						try (ResultSet rspd = pspd.executeQuery()) {
							res[i] = new Appointment(rs, rspd);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Function that returns all appropriate offers from the database.
	 * 
	 * @param arrivalTime   compared with existing bookings and start time.
	 * @param departureTime compared with existing bookings and start time.
	 * @param persons       compared with capacity.
	 * @return Arraylist of all offer objects.
	 */
	public ArrayList<HolidayOffer> getAvailableHolidayOffers(Timestamp arrivalTime, Timestamp departureTime,
			int persons) {
		ArrayList<HolidayOffer> result = new ArrayList<HolidayOffer>();

		// Declare the necessary SQL queries.
		String sqlSelect = "SELECT * FROM HolidayOffer WHERE starttime <= ? AND endtime >= ? AND capacity >= ?";
		String sqlSelectB = "SELECT * FROM Booking WHERE hid = ?";

		// Query all offers that fits to the given criteria.
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {

			try (PreparedStatement ps = connection.prepareStatement(sqlSelect);
					PreparedStatement psSelectB = connection.prepareStatement(sqlSelectB)) {
				ps.setTimestamp(1, arrivalTime);
				ps.setTimestamp(2, departureTime);
				ps.setInt(3, persons);

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						HolidayOffer temp = new HolidayOffer(rs.getInt(1), rs.getTimestamp(2), rs.getTimestamp(3),
								new AddressData(rs.getString(4), rs.getString(5)), rs.getInt(6), rs.getDouble(7));
						psSelectB.setInt(1, temp.getId());

						// Query all bookings for the offer to check if its
						// available.
						try (ResultSet brs = psSelectB.executeQuery()) {
							ArrayList<Booking> bookings = new ArrayList<Booking>();
							while (brs.next()) {
								bookings.add(new Booking(brs.getInt(1), brs.getTimestamp(2), brs.getTimestamp(3),
										brs.getTimestamp(4), brs.getBoolean(5),
										new GuestData(brs.getString(6), brs.getString(7)), brs.getDouble(8),
										brs.getInt(9)));
							}
							temp.setBookings(bookings);
						}
						if (temp.available(arrivalTime, departureTime))
							result.add(temp);
					}
					;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Inserts a new offer in the database.
	 * 
	 * @param startTime
	 * @param endTime
	 * @param address
	 * @param capacity
	 * @param fee
	 */
	public void insertOffer(Timestamp startTime, Timestamp endTime, AddressData address, int capacity, double fee) {

		// Declare SQL query to insert offer.
		String sqlInsert = "INSERT INTO HolidayOffer (startTime,endTime,street,town,capacity,fee) VALUES (?,?,?,?,?,?)";

		// Insert offer into database.
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {

			try (PreparedStatement ps = connection.prepareStatement(sqlInsert)) {
				ps.setTimestamp(1, startTime);
				ps.setTimestamp(2, endTime);
				ps.setString(3, address.getStreet());
				ps.setString(4, address.getTown());
				ps.setInt(5, capacity);
				ps.setDouble(6, fee);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Inserts a booking into the database if there are enough capacities
	 * 
	 * @param arrivalTime
	 * @param departureTime
	 * @param hid
	 * @param guestData
	 * @param persons
	 * @return new booking object if available or null if not available
	 */
	public Booking bookingHolidayOffer(Timestamp arrivalTime, Timestamp departureTime, int hid, GuestData guestData,
			int persons) {
		HolidayOffer ho = null;
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		Booking booking = null;

		// Declare necessary SQL queries.
		String sqlSelectHO = "SELECT * FROM HolidayOffer WHERE id=?";
		String sqlInsertBooking = "INSERT INTO Booking (creationDate,arrivalTime,departureTime,paid,name,email,price,hid) VALUES (?,?,?,?,?,?,?,?)";
		String sqlSelectB = "SELECT * FROM Booking WHERE hid=?";

		// Get selected offer
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psSelect = connection.prepareStatement(sqlSelectHO);
					PreparedStatement psSelectB = connection.prepareStatement(sqlSelectB);
					PreparedStatement psInsert = connection.prepareStatement(sqlInsertBooking,
							PreparedStatement.RETURN_GENERATED_KEYS)) {
				psSelect.setInt(1, hid);
				try (ResultSet hors = psSelect.executeQuery()) {
					if (hors.next()) {
						ho = new HolidayOffer(hors.getInt(1), hors.getTimestamp(2), hors.getTimestamp(3),
								new AddressData(hors.getString(4), hors.getString(5)), hors.getInt(6),
								hors.getDouble(7));
					}
				}

				// Check if offer is still available
				if (ho != null) {
					psSelectB.setInt(1, hid);
					try (ResultSet brs = psSelectB.executeQuery()) {
						while (brs.next()) {
							bookings.add(new Booking(brs.getInt(1), brs.getTimestamp(2), brs.getTimestamp(3),
									brs.getTimestamp(4), brs.getBoolean(5),
									new GuestData(brs.getString(6), brs.getString(7)), brs.getDouble(8),
									brs.getInt(9)));
						}
						ho.setBookings(bookings);
					}

					// Insert new booking
					if (ho.available(arrivalTime, departureTime) && ho.getCapacity() >= persons) {
//						Timestamp creationDate = new Timestamp(new Date().getTime());
//						booking = new Booking(0, new Timestamp(creationDate.getTime()), arrivalTime, departureTime,
//								false, guestData, calculatePrice(arrivalTime, departureTime, ho.getFee()), ho.getId());
//						psInsert.setTimestamp(1, booking.getCreationDate());
//						psInsert.setTimestamp(2, booking.getArrivalTime());
//						psInsert.setTimestamp(3, booking.getDepartureTime());
//						psInsert.setBoolean(4, booking.isPaid());
//						psInsert.setString(5, booking.getGuestData().getName());
//						psInsert.setString(6, booking.getGuestData().getEmail());
//						psInsert.setDouble(7, booking.getPrice());
//						psInsert.setInt(8, booking.getHid());
//						psInsert.executeUpdate();
//						try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
//							if (generatedKeys.next()) {
//								booking.setId(generatedKeys.getInt(1));
//							}
//						}

					} else
						ho = null;

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return booking;
	}

	/**
	 * Delete all bookings not paid and older than 14 days.
	 */
	public void setAvailableHolidayOffer() {

		// Declare necessary SQL statement.
		String deleteBO = "DELETE FROM Booking WHERE (paid=false) AND \"creationDate + 14 days < date\"";

		// Update Database.
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psDelete = connection.prepareStatement(deleteBO)) {
				psDelete.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if offer with given id exists.
	 * 
	 * @param hid
	 * @return
	 */
	public boolean checkHolidayOfferById(int hid) {

		// Declare necessary SQL query.
		String queryHO = "SELECT FROM HolidayOffer WHERE id=?";

		// query data.
		try (Connection connection = DriverManager
				.getConnection(
						"jdbc:" + Configuration.getType() + "://" + Configuration.getServer() + ":"
								+ Configuration.getPort() + "/" + Configuration.getDatabase(),
						Configuration.getUser(), Configuration.getPassword())) {
			try (PreparedStatement psSelect = connection.prepareStatement(queryHO)) {
				psSelect.setInt(1, hid);
				try (ResultSet rs = psSelect.executeQuery()) {
					return rs.next();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Function used to calculate the price for a booking.
	 * 
	 * @param date1 arrival date
	 * @param date2 departure date
	 * @param fee   price per night for the offer
	 * @return
	 */
	private double calculatePrice(Timestamp date1, Timestamp date2, double fee) {
		long dayDifference = (date2.getTime() - date1.getTime()) / 1000 / 60 / 60 / 24;

		return dayDifference * fee;
	}

	
}
