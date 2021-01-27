package datatypes;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PossibleDate {
	Date date;
	int mid;
	int aid;
	
	public PossibleDate(Date date, int mid, int aid) {
		this.date = date;
		this.mid = mid;
		this.aid = aid;
	}
	
	public PossibleDate(ResultSet rs) throws SQLException {
		this.date = rs.getDate("date");
		this.mid = rs.getInt("mid");
		this.aid = rs.getInt("aid");
	}
}
