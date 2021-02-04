package servlets;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datatypes.Appointment;
import datatypes.PossibleDate;
import dbadapter.DBFacade;


public class Reply extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		if(!SessionHelper.validate(request,response))return;



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
				int userId = SessionHelper.getUserId(request);
				int groupId = 420;
				ArrayList<Appointment> invitations = DBFacade.getInstance().getInvitations(userId, groupId);
				
				
//				System.out.println(invitations.get(0).name);
//				System.out.println(invitations.get(1).name);
//				System.out.println(invitations.get(2).name);
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
		if(!SessionHelper.validate(request,response))return;
		
		if (request.getParameter("action").equals("postPossibleDates")) {
			
			
			int aid = Integer.parseInt(request.getParameter("aid"));
			ArrayList<String> dates = DBFacade.getInstance().getPossibleDates(aid);
			
			System.out.println(aid);
			
			ArrayList<Date> possibleDates = new ArrayList<Date>();
			
			for (String date: dates) {
				if (request.getParameter(date) != null) {
					possibleDates.add(Date.valueOf(date));
				}
			}
			
			int i = 1;
			while(request.getParameter("date" + i) != null && request.getParameter("date" + i) != "" ) {
				possibleDates.add(Date.valueOf(request.getParameter("date" + i)));
				i++;
			}
			
			System.out.println("amount of dates: " + possibleDates.size());
			if (possibleDates.size() > 0) {
				// Set request attributes
//				int uid = 69;
//				DBFacade.getInstance().replyingToAppointment(uid, aid, possibleDates);
				
				request.setAttribute("pagetitle", "Reply successfull");
				request.setAttribute("message",
						"Wow du hast 1 reply geschickt. Fühlst du dich jtz cool oder was?");

				// Dispatch to template engine
				try {
					request.getRequestDispatcher("/templates/okRepresentation.ftl").forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
				
				
			}else {
				request.setAttribute("pagetitle", "Reply failed");
				request.setAttribute("message",
						"Du dumme Miesgebuaht. hast du nicht einen freien tag?");

				try {
					request.getRequestDispatcher("/templates/failInfoRepresentation.ftl").forward(request,
							response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			}
		}else {
			doGet(request, response);
		}
		
	}

}
