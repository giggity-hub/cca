package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author swe.uni-due.de
 *
 *         This servlet only contains a small index webpage where the user is
 *         able to choose his role.
 */
public class WelcomeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	

	/**
	 * doGet contains the call for the index webpage
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	
		if (request.getSession(false) == null || request.getSession(false).getAttribute("userid") == null || ((String) request.getSession(false).getAttribute("userid")).isBlank()) {
			request.setAttribute("navtype", "notSignedIn");
			request.setAttribute("pagetitle", "Bitte Einloggen");
		} 
		else if (request.getParameter("action") != null && request.getParameter("action").equals("logOut")) {
			if (request.getSession(false) != null)
				request.getSession(false).invalidate();
			request.setAttribute("navtype", "notSignedIn");
			request.setAttribute("pagetitle", "Bitte Einloggen");
		} 
		else {
			request.setAttribute("navtype", "signedIn");
			request.setAttribute("pagetitle", "Wilkommen");
		}

		// Dispatch request to template engine
		try {
			request.getRequestDispatcher("/templates/index.ftl").forward(request, response);
		} catch (ServletException | IOException e) {
			request.setAttribute("errormessage", "Template error: please contact the administrator");
			e.printStackTrace();
		}
	}

	/**
	 * Call doGet instead of doPost
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		if (request.getParameter("action").equals("signin")) {
			if (request.getParameter("userid") != null) {
				if(request.getSession(false) != null)
					request.getSession(false).invalidate();
				request.getSession(true).setAttribute("userid", request.getParameter("userid"));
			}
		}
		
		doGet(request, response);
	}
}