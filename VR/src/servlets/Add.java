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


public class Add extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * doGet contains the call for the index webpage
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {

		if(!SessionHelper.validate(request,response))
			return;
		
		request.setAttribute("pagetitle", "Termin hinzufugen");

		// Dispatch request to template engine
		try {
			request.getRequestDispatcher("/templates/add.ftl").forward(
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		if(!SessionHelper.validate(request,response))
			return;
		
		if (request.getParameter("action").equals("addAppointment")) {
			
			if(request.getParameter("participants") == null || 
					request.getParameter("description") == null ||
					request.getParameter("name") == null ||
					request.getParameter("location") == null ||
					request.getParameter("duration") == null ||
					request.getParameter("date1") == null ||
					request.getParameter("numOfDates") == null) {
				try {
					request.setAttribute("pagetitle", "Alles kaputt");
					request.setAttribute("message", "Alles kaputt! Anfrage falsch. Irgendwas war null");
					request.getRequestDispatcher("/templates/failInfoRepresentation.ftl").forward(
							request, response);
				} catch (ServletException | IOException e) {
					request.setAttribute("errormessage",
							"Template error: please contact the administrator");
					e.printStackTrace();
				}
				System.out.println("Anfrage falsch");
				return;
			}
			if(request.getParameter("participants").isBlank() || 
					request.getParameter("description").isBlank() ||
					request.getParameter("name").isBlank() ||
					request.getParameter("location").isBlank() ||
					request.getParameter("duration").isBlank() ||
					request.getParameter("date1").isBlank() ||
					request.getParameter("numOfDates").isBlank()) {
				try {
					request.setAttribute("pagetitle", "Alles kaputt");
					request.setAttribute("message", "<h1>Du hast alles kaputt gemacht!</h1><br> Anfrage unvollständig");
					request.getRequestDispatcher("/templates/failInfoRepresentation.ftl").forward(
							request, response);
				} catch (ServletException | IOException e) {
					request.setAttribute("errormessage",
							"Template error: please contact the administrator");
					e.printStackTrace();
				}
				System.out.println("Anfrage unvollständig");
				return;
			}
			try {
				int numOfDates = Integer.parseInt(request.getParameter("numOfDates"));
				Date[] dates = new Date[numOfDates];
				for(int i=0;i<numOfDates;i++){
					dates[i]= Date.valueOf((String) request.getParameter("date"+(i + 1)));
				}
				
				int creator = 69;
				
				int[] participants = participantArrayFromString(request.getParameter("participants"));
				String description = (String)request.getParameter("description");
				String name = (String)request.getParameter("name");
				String location = (String) request.getParameter("location");
				int duration = Integer.parseInt(request.getParameter("duration"));
				Date deadline = Date.valueOf((String) request.getParameter("deadline"));
				
				int groupID = Integer.parseInt(request.getParameter("groupID"));


				//call the CCA singleton to post the appontment

				CCApplication.getInstance().createAppointment(creator, dates, participants, description, name, location, duration, deadline, groupID);

			}
			catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			

		}
		
		try {
			request.setAttribute("pagetitle", "Super!");
			request.setAttribute("message", "Termin Erfolgreich hinzugefügt");
			request.getRequestDispatcher("/templates/okRepresentation.ftl").forward(
					request, response);
		} catch (ServletException | IOException e) {
			request.setAttribute("errormessage",
					"Template error: please contact the administrator");
			e.printStackTrace();
		}
	}
}