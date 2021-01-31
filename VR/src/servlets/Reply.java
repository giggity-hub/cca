package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Reply extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {

		// Set navtype
		request.setAttribute("navtype", "guest");

		// Catch error if there is no page contained in the request
		String action = (request.getParameter("action") == null) ? "" : request.getParameter("action");

		// Case: Request booking form
		if (action.equals("selectAppointment")) {
			// Set request attributes
			request.setAttribute("pagetitle", "Book Offer");
			request.setAttribute("hid", request.getParameter("hid"));

			// Dispatch request to template engine
			try {
				request.getRequestDispatcher("/templates/showBookHolidayOfferForm.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Otherwise show search form
		} else {

			// Set request attributes
			request.setAttribute("pagetitle", "Invitations");

			// Dispatch request to template engine
			try {
				request.getRequestDispatcher("/templates/invitations.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
