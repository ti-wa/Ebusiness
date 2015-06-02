package de.htwg_konstanz.ebus.wholesaler.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.management.modelmbean.XMLParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.security.Security;
import de.htwg_konstanz.ebus.wholesaler.demo.ControllerServlet;
import de.htwg_konstanz.ebus.wholesaler.demo.IAction;
import de.htwg_konstanz.ebus.wholesaler.demo.LoginBean;
import de.htwg_konstanz.ebut.main.BmecatOperations;
import de.htwg_konstanz.ebut.main.BmecatOperationsResult;
import de.htwg_konstanz.ebut.main.Upload;
import de.htwg_konstanz.ebut.main.XmlParser;

public class ImportAction implements IAction {

	private static final String ACTION_IMPORT = "import";
	public static final String PARAM_LOGIN_BEAN = "loginBean";
	private static final String PARAM_PRODUCT_LIST = "productList";

	/**
	 * Gets the Instanz of the Upload class.
	 */
	private final Upload upload = new Upload();

	public ImportAction() {
		super();
	}

	/**
	 * The execute method is automatically called by the dispatching sequence of
	 * the {@link ControllerServlet}.
	 * 
	 * @param request
	 *            the HttpServletRequest-Object provided by the servlet engine
	 * @param response
	 *            the HttpServletResponse-Object provided by the servlet engine
	 * @param errorList
	 *            a Stringlist for possible error messages occured in the
	 *            corresponding action
	 * @return the redirection URL
	 */
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
			if (Security.getInstance().isUserAllowed(loginBean.getUser(),
					Security.RESOURCE_ALL, Security.ACTION_READ)) {
				// find all available products and put it to the session
				List<?> productList = ProductBOA.getInstance().findAll();
				request.getSession(true).setAttribute(PARAM_PRODUCT_LIST,
						productList);
				
				InputStream is;
				org.w3c.dom.Document xmlDom;
				// infos to Ui Lists
				List<String> info = new ArrayList<String>();
				List<String> missingProducts = new ArrayList<String>();
				try {
					if (ServletFileUpload.isMultipartContent(request)) {
						is = upload.upload(request);
						//filenamecheck
						if (is == null) {
							info.add("filename extension has to be xml");
							throw new XMLParseException();
						}
						XmlParser parser = new XmlParser();
						
						//welform check
						xmlDom = parser.parseXML(is);
						if (xmlDom == null) {
							info.add("XML is not wellformed");
							throw new XMLParseException();
						}
						//valid check
						if (!parser.isValidBmecat(xmlDom)) {
							info.add("XML is not valid Bmecat");
							throw new XMLParseException();
						}
						BmecatOperations bmecatoperations = BmecatOperations
								.getInstance();
						BmecatOperationsResult result = bmecatoperations
								.checkSupplierExist(xmlDom);
						//supplierlist emtpy check
						if (result.isSupplierListIsEmpty()) {
							info.add("Supplier List is Empty");
							throw new SupplierException();
						}
						//supplier missing check
						if (!result.isSupplierExists()) {
							info.add(result.getSuppliername()
									+ " is missing in Supplierlist");
							throw new SupplierException();
						}
						//product import + result
						result = bmecatoperations.writeXmlToDatabase(xmlDom);
						if (result.getNonImportetProducts() != null) {
							info.add("Not all products imported. Only: "
									+ result.getProductCounter());
							missingProducts = result.getNonImportetProducts();
						} else {
							info.add("All products importet. number: "
									+ result.getProductCounter());
						}
					}
				} catch (XMLParseException e) {
					System.out.println("no valid XML");
				} catch (SupplierException e) {
					System.out.println("SupplierError");
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (FileUploadException e) {
					e.printStackTrace();
				}
				HttpSession session = request.getSession();
				session.setAttribute("info", info);
				session.setAttribute("missingProducts", missingProducts);
				// redirect to the import page
				return "import.jsp";
			} else {
				// authorization failed -> show error message
				errorList.add("You are not allowed to perform this action!");

				// redirect to the welcome page
				return "welcome.jsp";
			}
		} else
			// redirect to the login page
			return "login.jsp";
	}

	@Override
	public boolean accepts(String actionName) {
		return actionName.equalsIgnoreCase(ACTION_IMPORT);
	}

	/**
	 * @author tim Exception to simplify code
	 */
	public class SupplierException extends Exception {
		private static final long serialVersionUID = -8717517235010572406L;
	}
}
