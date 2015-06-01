package de.htwg_konstanz.ebut.main;

import de.htwg_konstanz.ebus.wholesaler.demo.util.Constants;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * Helper for XMP Processing Data
 * 
 * @author tim
 *
 */

public class XmlParser {
	private final String PATHZUMWORKSPACE = "/Users/tim/workspace/";
	/**
	 * gets the normalized dom for a XML-InputStream
	 * 
	 * @param InputStream
	 *            InputStream (XML data)
	 * @return dom or null if not wellformed
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public org.w3c.dom.Document parseXML(InputStream is)
			throws ParserConfigurationException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document document = db.parse(is);
			document.getDocumentElement().normalize();
			return document;
		} catch (SAXException e) {
			System.out.println("parse document error");
			return null;
		}
		
	}

	/**
	 * check against bmecat xsd
	 * 
	 * @param org.w3c.dom.Document 
	 *            InputStream (XML Data)
	 * @return true or false
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public boolean isValidBmecat(org.w3c.dom.Document xmlDom)
			throws ParserConfigurationException, IOException {
		if (xmlDom != null) {
			SchemaFactory fc = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			// gets bmecat schema and load id
			InputStream input = new FileInputStream(PATHZUMWORKSPACE + "WholesalerWebDemo/xmlData/bmecat_new_catalog_1_2_simple_without_NS.xsd");
			Source schemaFile = new StreamSource(input);
			Schema schema;
			try {
				schema = fc.newSchema(schemaFile);
				Validator validator = schema.newValidator();
				//validate dom against bmecat
				validator.validate(new DOMSource(xmlDom));
				System.out.println("valid XML File");
				return true;
			} catch (SAXException e) {
				System.out.println("non Valid XML file");
				return false;
			}
		}
		return false;
	}
}
