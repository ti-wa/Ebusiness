package de.htwg_konstanz.ebut.main;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class to generate the Bmecat
 * 
 * @author tim
 *
 */
public class BmecatExport {

	/**
	 * used to filter object to a search pattern
	 * 
	 * @param search
	 */
	public Collection<BOProduct> filter(final String search) {
		Collection<BOProduct> products;
		if (search == null) {
			products = ProductBOA.getInstance().findAll();
			System.out.println("all products");
		} else {
			products = ProductBOA.getInstance().findByCriteriaLike(
					"Shortdescription", "%" + search + "%");
			System.out.println(products.size() + " products found");
		}
		return products;
	}

	/**
	 * Generate Export OutputStream
	 * 
	 * @param action
	 * @param response
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public OutputStream export(final String action,
			final HttpServletResponse response,
			final Collection<BOProduct> products)
			throws ParserConfigurationException, IOException,
			TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		OutputStream out = response.getOutputStream();
		document = generateDocument(document);
		Result result = new StreamResult(out);
		Source source = new DOMSource(document);
		// write dom to output stream
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		transformer.transform(source, result);
		out.close();
		return out;
	}

	private Document generateDocument(Document document) {
		// create root element and prolog
		Element root = document.createElement("BMECAT");
		root.setAttribute("version", "1.2");
		root.setAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");

		// crate elements
		Element header = document.createElement("HEADER");
		Element catalog = document.createElement("CATALOG");
		Element supplier = document.createElement("SUPPLIER");
		Element language = document.createElement("LANGUAGE");
		Element catalogId = document.createElement("CATALOG_ID");
		Element catalogVersion = document.createElement("CATALOG_VERSION");
		Element catalogName = document.createElement("CATALOG_NAME");
		Element supplierName = document.createElement("SUPPLIER_NAME");
		Element tNewCatalog = document.createElement("T_NEW_CATALOG");
		// create text
		language.appendChild(document.createTextNode("deu"));
		catalogId.appendChild(document.createTextNode("Erman und Tim Ebus"));
		catalogVersion.appendChild(document.createTextNode("1.0"));
		catalogName
				.appendChild(document
						.createTextNode("Beispielproduktkatalog für E-Business Laborpraktika - Export"));
		supplierName.appendChild(document.createTextNode("HTWG Konstanz"));
		// append elements
		document.appendChild(root);
		root.appendChild(header);
		header.appendChild(catalog);
		header.appendChild(supplier);
		catalog.appendChild(language);
		catalog.appendChild(catalogId);
		catalog.appendChild(catalogVersion);
		catalog.appendChild(catalogName);
		supplier.appendChild(supplierName);
		root.appendChild(tNewCatalog);
		return document;
	}

}
