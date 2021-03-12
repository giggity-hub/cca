package timer;

//import application.VRApplication;
import application.CCApplication;

/**
 * Timer class to call the method checkPayment in the application. Main method
 * can be executed in a scheduled way.
 * 
 * @author swe.uni-due.de
 *
 */
public class Timer {

	public static void main(String[] args) {
		CCApplication ccaApp = new CCApplication();
		ccaApp.checkDeadlines();
	}
}
