package servlets;

//Dies ist ein Kommentar von Stefan!!!
// Hello there Stefan war hier jaja weiste bescheid, wa?
//Alex auch
//Lososcos
// Lukas
//ich bin der uwe und ich bin auch dabei
//Alex ist wieder da
import java.io.IOException;

import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.CCApplication;
import dbadapter.DBFacade;

/**
 * 
 * @author swe.uni-due.de
 *
 *         This servlet only contains a small index webpage where the user is
 *         able to choose his role.
 */



public class Test extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * doGet contains the call for the index webpage
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		// Set pagetitle and navtype
		request.setAttribute("pagetitle", "Welcome");
		request.setAttribute("navtype", "general");

		// Dispatch request to template engine
		try {
			request.getRequestDispatcher("/templates/testWebpage.ftl").forward(
					request, response);
		} catch (ServletException | IOException e) {
			request.setAttribute("errormessage",
					"Template error: please contact the administrator");
			e.printStackTrace();
		}
	}
	protected int[] participantArrayFromString(String participants) {
		String[] strArr = participants.split(",");
		int[] intArr = new int[strArr.length];
		for (int i=0; i< strArr.length; i++) {
			intArr[i]= Integer.parseInt(strArr[i]);
		}
		return intArr;
	}
	/**
	 * Call doGet instead of doPost
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		if (request.getParameter("action").equals("addAppointment")) {
			System.out.println("Registered Post request with action=addAppointment");
			
			int creator = 69;
			int groupId = 420;
			Date[] dates = {Date.valueOf((String) request.getParameter("dates"))}; //only one date for now
			int[] participants = participantArrayFromString(request.getParameter("participants"));
			String description = (String)request.getParameter("description");
			String name = (String)request.getParameter("name");
			String location = (String) request.getParameter("location");
			int duration = Integer.parseInt(request.getParameter("duration"));
			Date deadline = Date.valueOf((String) request.getParameter("dates"));
			
			
//			System.out.println(dates);
//			System.out.println(duration);
//			System.out.println(participants[1]);
			
			//call the CCA singleton to post the appontment
			DBFacade.getInstance().creatingAppointment(creator, dates, participants, description, name, location, duration, deadline, groupId);

		}
		
		doGet(request, response);
	}
}