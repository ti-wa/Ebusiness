package de.htwg_konstanz.ebut.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSalesPrice;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.PriceBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class to generate the Bmecat
 * 
 * @author tim
 *
 */
public class BmecatExport {
	private final String PATHZUMWORKSPACE = "/Users/tim/workspace/";

	/**
	 * used to filter object to a search pattern
	 * 
	 * @param search
	 * @return list of products to export
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


	/** initiate generation of bmecat and let it transform to xhtml. poke it into http stream
	 * @param action xhtml or bmecat
	 * @param HTTP response 
	 * @param products	the filterd products
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public void export(final String action, final HttpServletResponse response,
			final Collection<BOProduct> products)
			throws ParserConfigurationException, IOException,
			TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		OutputStream out = response.getOutputStream();
		document = generateBmecatDocument(document, products);
		if (action.contentEquals("xhtml")) {
			transformToXHTML(document, response);
			return;
		}
		Result result = new StreamResult(out);
		Source source = new DOMSource(document);
		// write dom to output stream
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		transformer.transform(source, result);
		out.close();
	}

	/**Generate the XML Content in the Document
	 * @param the document to save the catalog
	 * @param products
	 * @return
	 */
	private Document generateBmecatDocument(Document document,
			final Collection<BOProduct> products) {
		// create root element and prolog
		Element root = document.createElement("BMECAT");
		root.setAttribute("version", "1.2");
		root.setAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");

		// create elements
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

		// add products to catalog
		for (BOProduct product : products) {
			Element article = document.createElement("ARTICLE");
			Element articleDetails = document.createElement("ARTICLE_DETAILS");
			Element articleOrderDetails = document
					.createElement("ARTICLE_ORDER_DETAILS");
			if (product.getOrderNumberCustomer() != null) {
				Element supplierAid = document.createElement("SUPPLIER_AID");
				supplierAid.appendChild(document.createTextNode(product
						.getOrderNumberCustomer()));
				article.appendChild(supplierAid);
			}
			article.appendChild(articleDetails);
			article.appendChild(articleOrderDetails);
			if (null != product.getShortDescriptionCustomer()) {
				Element descriptionShort = document
						.createElement("DESCRIPTION_SHORT");
				descriptionShort.appendChild(document.createTextNode(product
						.getShortDescriptionCustomer()));
				articleDetails.appendChild(descriptionShort);
			}
			if (null != product.getLongDescriptionCustomer()) {
				Element descriptionLong = document
						.createElement("DESCRIPTION_LONG");
				descriptionLong.appendChild(document.createTextNode(product
						.getShortDescriptionCustomer()));
				articleDetails.appendChild(descriptionLong);
			}

			Element ean = document.createElement("EAN");
			if (product.getOrderNumberCustomer().length() > 14) {
				String s = product.getOrderNumberCustomer().trim()
						.substring(0, 14);
				ean.appendChild(document.createTextNode(s));
			} else {
				ean.appendChild(document.createTextNode(product
						.getOrderNumberCustomer()));
			}
			articleDetails.appendChild(ean);

			Element orderUnit = document.createElement("ORDER_UNIT");
			Element noCuPerOu = document.createElement("NO_CU_PER_OU");
			orderUnit.appendChild(document.createTextNode("C62"));
			noCuPerOu.appendChild(document.createTextNode("10"));
			articleOrderDetails.appendChild(orderUnit);
			articleOrderDetails.appendChild(noCuPerOu);

			tNewCatalog.appendChild(article);

			// Price Details
			List<BOSalesPrice> salesPrices = PriceBOA.getInstance()
					.findSalesPrices(product);
			Element priceDetails = document
					.createElement("ARTICLE_PRICE_DETAILS");

			for (BOSalesPrice salesPrice : salesPrices) {

				// Create Price elements
				Element articlePrice = document.createElement("ARTICLE_PRICE");
				Element priceAmount = document.createElement("PRICE_AMOUNT");
				Element currency = document.createElement("PRICE_CURRENCY");
				Element tax = document.createElement("TAX");
				Element priceTerritory = document.createElement("TERRITORY");

				// Add the elements to the article prcie
				articlePrice.appendChild(priceAmount);
				articlePrice.appendChild(currency);
				articlePrice.appendChild(tax);
				articlePrice.appendChild(priceTerritory);

				// set the Price Type
				articlePrice.setAttribute("price_type",
						salesPrice.getPricetype());

				// Set the values of the elements
				priceAmount.appendChild(document.createTextNode(salesPrice
						.getAmount().toString()));
				currency.appendChild(document.createTextNode(salesPrice
						.getCountry().getCurrency().getCode()));
				tax.appendChild(document.createTextNode(salesPrice.getTaxrate()
						.toString()));
				priceTerritory.appendChild(document.createTextNode(salesPrice
						.getCountry().getIsocode()));

				// add Price to the element Price details
				priceDetails.appendChild(articlePrice);

			}
			// add all Prices to the Article
			article.appendChild(priceDetails);
			// add the article to the catalog
			tNewCatalog.appendChild(article);
		}

		return document;
	}

	/**Transforms the BMECAT to an XHTML
	 * @param BMECAT 
	 * @param HTTP response
	 * @return
	 */
	private OutputStream transformToXHTML(Document doc,
			HttpServletResponse response) {
		try {
			//get streams and prepare output
			OutputStream output = response.getOutputStream();
			Result result = new StreamResult(output);
			InputStream input = new FileInputStream(PATHZUMWORKSPACE
					+ "WholesalerWebDemo/xmlData/Transformation_XHTML.xsl");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Source xmlSource = new DOMSource(doc);
			Result outputTarget = new StreamResult(outputStream);
			//transform BMECAT to xhtml preparation
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = factory.newTransformer();
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer = factory.newTransformer(new StreamSource(input));
			TransformerFactory.newInstance().newTransformer()
					.transform(xmlSource, outputTarget);
			InputStream is = new ByteArrayInputStream(
					outputStream.toByteArray());
			//settings for transformation
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			//transform
			transformer.transform(new StreamSource(is), result);

			output.close();

			return output;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
