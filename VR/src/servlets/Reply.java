package servlets;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.CCApplication;
import datatypes.Appointment;
import datatypes.PossibleDate;


public class Reply extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		if(!SessionHelper.validate(request,response))return;

		// Catch error if there is no page contained in the request
		String action = (request.getParameter("action") == null) ? "" : request.getParameter("action");

		// selectAppointment -> addPossibleDate
		if (action.equals("selectAppointment")) {
			request.setAttribute("pagetitle", "Select Apponitment");

			int aid = Integer.parseInt(request.getParameter("aid"));
			ArrayList<PossibleDate> possibleDates = CCApplication.getInstance().getPossibleDates(aid);
			ArrayList<PossibleDate> replyedDates = CCApplication.getInstance().getReplyedDatesNotFinal(aid, SessionHelper.getUserId(request));
			ArrayList<String> outDates = new ArrayList<>();
			boolean[] isMarked = new boolean[possibleDates.size()];
			if (possibleDates != null && replyedDates != null && replyedDates.size() != 0) {
				for (int i = 0; i < isMarked.length; i++) {
					outDates.add(possibleDates.get(i).getDate().toString());
					for (int j = 0; j < replyedDates.size(); j++) {
						if(replyedDates.get(j).getDate().equals(possibleDates.get(i).getDate())) {
							isMarked[i] = true;
							replyedDates.remove(j);
							break;
						}
					}
				}
			}
			else if(possibleDates != null) {
				for (int i = 0; i < isMarked.length; i++) {
					outDates.add(possibleDates.get(i).getDate().toString());
				}
			}
			

			try {
				request.setAttribute("aid", aid);
				request.setAttribute("possibleDates", outDates);
				request.setAttribute("isMarked", isMarked);
				request.getRequestDispatcher("/templates/addPossibleDate.ftl").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			
			// Dispatch request to template engine
			try {
				int userId = SessionHelper.getUserId(request);
				ArrayList<Appointment> appointments = CCApplication.getInstance().getReplyedNotFinal(userId);
				
				// Set request attributes
				request.setAttribute("pagetitle", "Invitations");
				request.setAttribute("appointments", appointments);
				
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
		if(!SessionHelper.validate(request,response)) return;
		
		if (request.getParameter("action").equals("postPossibleDates")) {
			
			int aid = Integer.parseInt(request.getParameter("aid"));
			int numofpd = Integer.parseInt(request.getParameter("size"));
			ArrayList<Date> pds = new ArrayList<>();
			
			for (int i = 0; i < numofpd; i++) {
				if (request.getParameter("pddate" + i) != null) {
					/*String[] date = request.getParameter("pddate" + i).split("\\.");
					pds.add(Date.valueOf(date[2] + "-" + date[1] + "-" + date[0]));*/
					pds.add(Date.valueOf(request.getParameter("pddate" + i)));
				}
			}
			
			int i = 1;
			while(request.getParameter("date" + i) != null && request.getParameter("date" + i) != "" ) {
				System.out.println(request.getParameter("date" + i));
				pds.add((Date.valueOf(request.getParameter("date" + i))));
				i++;
			}
			
			System.out.println("amount of dates: " + pds.size());
			if (pds.size() > 0) {
				// Set request attributes
				CCApplication.getInstance().replyingToAppointment(aid, SessionHelper.getUserId(request), pds);
				
				request.setAttribute("pagetitle", "Reply successfull");
				request.setAttribute("message","Deine Reply wurde gespeichert");

				// Dispatch to template engine
				try {
					request.getRequestDispatcher("/templates/okRepresentation.ftl").forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
				
				
			}else {
				request.setAttribute("pagetitle", "Reply failed");
				request.setAttribute("message","Es muss mindestens ein possible Date oder ein alternative Date angegeben werden");

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
