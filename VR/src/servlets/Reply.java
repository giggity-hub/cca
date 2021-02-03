package servlets;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datatypes.Appointment;
import datatypes.PossibleDate;
import dbadapter.DBFacade;


public class Reply extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {

		// Set navtype
		request.setAttribute("navtype", "guest");

		// Catch error if there is no page contained in the request
		String action = (request.getParameter("action") == null) ? "" : request.getParameter("action");

		// selectAppointment -> addPossibleDate
		if (action.equals("selectAppointment")) {

			request.setAttribute("pagetitle", "Book Offer");

			int aid = Integer.parseInt(request.getParameter("aid"));
			ArrayList<String> possibleDates = DBFacade.getInstance().getPossibleDates(aid);
			

			try {
				request.setAttribute("aid", aid);
				request.setAttribute("possibleDates", possibleDates);
				request.getRequestDispatcher("/templates/addPossibleDate.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			
			// Dispatch request to template engine
			try {
				int userId = 69;
				int groupId = 420;
				ArrayList<Appointment> invitations = DBFacade.getInstance().getInvitations(userId, groupId);
				
				
				System.out.println(invitations.get(0).name);
				System.out.println(invitations.get(1).name);
				System.out.println(invitations.get(2).name);
				// Set request attributes
				request.setAttribute("pagetitle", "Invitations");
				request.setAttribute("invitations", invitations);
				
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
		
		if (request.getParameter("action").equals("postPossibleDates")) {
			
			
			int aid = Integer.parseInt(request.getParameter("aid"));
			ArrayList<String> possibleDates = DBFacade.getInstance().getPossibleDates(aid);
			request.setAttribute("aid", aid);
			System.out.println(aid);
			
			
			try {
//				request.getParameterNames();
//				request.setAttribute("possibleDates", possibleDates);
				request.setAttribute("pagetitle", "scurr");
				request.setAttribute("navtype", "guest");
				request.getRequestDispatcher("/templates/index.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			doGet(request, response);
		}
		
	}

}
