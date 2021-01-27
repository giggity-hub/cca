package datatypes;

import java.sql.Date;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Appointment {
	public int aid;
	int groupId;
	String description;
	String name;
	String location;
	int duration;
	Date deadline;
	public Boolean isFinal;
	public ArrayList<PossibleDate> possibleDates;
	
	public Appointment(int aid, int groupId, String description, String name, String location, int duration, 
			Date deadline, Boolean finalized) {
		this.aid = aid;
		this.groupId = groupId;
		this.description = description;
		this.name = name;
		this.location = location;
		this.duration = duration;
		this.deadline = deadline;
		this.isFinal = finalized;
	}
	
	public Appointment(ResultSet rs) throws SQLException {
		this.aid = rs.getInt("aid");
		this.groupId =rs.getInt("groupId");
		this.description = rs.getString("description");
		this.name = rs.getString("name");
		this.location = rs.getString("location");
		this.duration = rs.getInt("duration");
		this.deadline = rs.getDate("deadline");
		this.isFinal = rs.getBoolean("isFinal");
	}
	
	public void setPossibleDates(ArrayList<PossibleDate> pds) {
		this.possibleDates = pds;
	}
	
	public Appointment(ResultSet apt, ResultSet possibleDates) throws SQLException {
		this.aid = apt.getInt("aid");
		this.groupId =apt.getInt("groupId");
		this.description = apt.getString("description");
		this.name = apt.getString("name");
		this.location = apt.getString("location");
		this.duration = apt.getInt("duration");
		this.deadline = apt.getDate("deadline");
		this.isFinal = apt.getBoolean("isFinal");
		
		this.possibleDates = new ArrayList<PossibleDate>();
		while (possibleDates.next()) {
			this.possibleDates.add(new PossibleDate(possibleDates));
		}
	}
	
	public Appointment(int aid, int groupId, String description, String name, String location, int duration,
			Date deadline, Boolean isFinal, ArrayList<PossibleDate> possibleDates) {
		this.aid = aid;
		this.groupId = groupId;
		this.description = description;
		this.name = name;
		this.location = location;
		this.duration = duration;
		this.deadline = deadline;
		this.isFinal = isFinal;
		this.possibleDates = possibleDates;
	}

	public Date makeFinal() {
		Map<Date, Integer> map = new HashMap<Date, Integer>();
		//i better hope this shit dont receives an empty array
		Date finalDate = this.possibleDates.get(0).date;
	
		for (PossibleDate pd: this.possibleDates) {
			Integer count = map.get(pd.date);
			
			if (count == null) {
				map.put(pd.date, 1);
			}else {
				map.put(pd.date, count+1);
			}
			finalDate = map.get(finalDate) < map.get(pd.date) ? pd.date : finalDate;
		}
		this.isFinal = true;
		return finalDate;
		
	}
}
