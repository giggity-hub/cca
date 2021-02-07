package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.CCApplication;
import datatypes.Appointment;

public class Calendar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		if(!SessionHelper.validate(request, response))
			return;
		
		ArrayList<Appointment> appointments = CCApplication.getInstance().getCalendar(SessionHelper.getUserId(request));
		
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
