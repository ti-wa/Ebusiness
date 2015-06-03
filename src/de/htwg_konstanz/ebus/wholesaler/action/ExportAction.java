package de.htwg_konstanz.ebus.wholesaler.action;


import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.IBOUser;
import de.htwg_konstanz.ebus.framework.wholesaler.api.security.Security;
import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.LoginBean;
import de.htwg_konstanz.ebut.main.BmecatExport;

public class ExportAction implements IAction {
	private static final String ACTION_EXPORT = "export";
	public static final String PARAM_LOGIN_BEAN = "loginBean";

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ArrayList<String> errorList) {

		// get the login bean from the session
		LoginBean loginBean = (LoginBean) request.getSession(true)
				.getAttribute(PARAM_LOGIN_BEAN);

		// ensure that the user is logged in
		if (loginBean != null && loginBean.isLoggedIn()) {
			// ensure that the user is allowed to execute this action
			// (authorization)
			// at this time the authorization is not fully implemented.
			// -> use the "Security.RESOURCE_ALL" constant which includes all
			// resources.

			IBOUser user = loginBean.getUser();

			if (Security.getInstance().isUserAllowed(user,
					Security.ACTION_READ, Security.ACTION_READ)) {
				Collection<BOProduct> products;
				String action = (String) request.getParameter("view");
				String search = (String) request.getParameter("search");
				if (action != null) {
					BmecatExport bmecatexport = new BmecatExport();
					products = bmecatexport.filter(search);
					try {
						bmecatexport.export(action, response, products);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return "export.jsp";
			} else {
				// authorization failed -> show error message
				errorList.add("You are not allowed to perform this action!");

				// redirect to the welcome page
				return "welcome.jsp";
			}
		} else {
			// redirect to the login page
			return "login.jsp";
		}
	}

	public boolean accepts(String actionName) {

		return actionName.equalsIgnoreCase(ACTION_EXPORT);
	}
}