package lexprd006.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor {

	private @Value("#{config['mail_user_name'] ?: 'null'}") String username;
	private @Value("#{config['mail_password'] ?: 'null'}") String password;
	private @Value("#{config['mail_smtp_host'] ?: 'null'}") String hostName;
	private @Value("#{config['mail_smtp_port'] ?: 'null'}") String portNo;
	private @Value("#{config['mail_from'] ?: 'null'}") String mailFrom;
	private @Value("#{config['project_name'] ?: 'null'}") String projectName;
	private @Value("#{config['project_url'] ?: 'null'}") String url;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		// JSONObject objForSend = new JSONObject();
		System.out.println("Reached interceptor");

		System.out.println("This request coming from: " + arg0.getRequestURI());
		System.out.println("This is project name: " + "/" + projectName + "/authenticateUser");
		String token = arg0.getHeader("Authorization");

		if (arg0.getRequestURI().equals("/" + projectName + "/authenticateUser")
				|| arg0.getRequestURI().equals("/" + projectName + "/userlogout")
				|| arg0.getRequestURI().equals("/" + projectName + "/forgotpassword")
				|| arg0.getRequestURI().equals("/" + projectName + "/downloadDocuments")
				|| arg0.getRequestURI().equals("/" + projectName + "/downloadProofOfCompliance")
				|| arg0.getRequestURI().equals("/" + projectName + "/downloadComplianceDocument")
				|| arg0.getRequestURI().equals("/" + projectName + "/getExportData")
				|| arg0.getRequestURI().equals("/" + projectName + "/getExportReport")
				|| arg0.getRequestURI().equals("/" + projectName + "/downloadShowCauseDocument")
				|| arg0.getRequestURI().equals("/" + projectName + "/downloadExportDrillReport")
				|| arg0.getRequestURI().equals("/" + projectName + "/downloadSubtaskDocument")) { 
			/*if(arg0.getRequestURI().equals("/authenticateUser") || arg0.getRequestURI().equals("/userlogout") 
				|| arg0.getRequestURI().equals("/forgotpassword") || arg0.getRequestURI().equals("/downloadProofOfCompliance") 
				|| arg0.getRequestURI().equals("/downloadShowCauseDocument")
				|| arg0.getRequestURI().equals("/downloadShowCauseDocument")
				|| arg0.getRequestURI().equals("/downloadComplianceDocument")
				|| arg0.getRequestURI().equals("/getExportData")
				|| arg0.getRequestURI().equals("/getExportReport") 
				|| arg0.getRequestURI().equals("/downloadExportDrillReport")){*/
		
			System.out.println("in authenticate/forgot password user: ");
			return true;
		} else {
			System.out.println("this is token: " + token);
			System.out.println("this is session token: " + arg0.getSession().getAttribute("authentication_token"));
			if (token != null && arg0.getSession().getAttribute("authentication_token") != null) {
				if (arg0.getSession().getAttribute("authentication_token").toString().equals(token)) {
					return true;
				} else {
					arg1.sendError(HttpServletResponse.SC_UNAUTHORIZED);
					return false;
				}
			} else {
				System.out.println("Problem in token");
				arg1.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
		}

	}

}
