package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionHelper {
	
	public static boolean validate(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession(false) == null || request.getSession(false).getAttribute("userid") == null || ((String)request.getSession(false).getAttribute("userid")).isBlank()) {
			try {
				request.setAttribute("navtype", "notSignedIn");
				request.setAttribute("pagetitle", "Bitte Einloggen");
				request.getRequestDispatcher("/templates/index.ftl").forward(request, response);
			} catch (ServletException | IOException e) {
				request.setAttribute("errormessage", "Template error: please contact the administrator");
				e.printStackTrace();
			}
			
			return false;
		}
		else if (request.getParameter("action") != null && request.getParameter("action").equals("logOut")) {
			if (request.getSession(false) != null)
				request.getSession(false).invalidate();
			System.out.println("ausgeloggt");
			
			try {
				request.setAttribute("navtype", "notSignedIn");
				request.setAttribute("pagetitle", "Bitte Einloggen");
				request.getRequestDispatcher("/templates/index.ftl").forward(request, response);
			} catch (ServletException | IOException e) {
				request.setAttribute("errormessage", "Template error: please contact the administrator");
				e.printStackTrace();
			}
			
			return false;
		}
		else {
			request.setAttribute("navtype", "signedIn");
			return true;
		}
	}
	
	public static int getUserId(HttpServletRequest request) {
		try {
			return Integer.parseInt((String) request.getSession(false).getAttribute("userid"));
		} catch (Exception e) {
			System.out.println("Die Session ist kaputt :(");
			request.getSession().invalidate();
			return -1;
		}
	}

}
