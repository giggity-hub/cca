package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datatypes.Appointment;
import dbadapter.DBFacade;

public class Calendar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		if(!SessionHelper.validate(request, response))
			return;
		
		Appointment[] appointments = DBFacade.getInstance().getCalendar(SessionHelper.getUserId(request));
		request.setAttribute("appointments", appointments);
		
		
		request.setAttribute("pagetitle", "Calendar");
		try {
			request.getRequestDispatcher("/templates/calendar.ftl").forward(request, response);
		} catch (ServletException | IOException e) {
			request.setAttribute("errormessage", "Template error: please contact the administrator");
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
