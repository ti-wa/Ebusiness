package de.htwg_konstanz.ebut.main;

import java.util.List;

import org.w3c.dom.*;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.SupplierBOA;

/** Operation for a Bmecat XML File
 * @author tim
 *
 */
public class BmecatOperations {

	/** check if the supplier of an bmecat exist
	 * @param xmlDom
	 * @return a Result object
	 */
	public BmecatOperationsResult checkSupplierExist(org.w3c.dom.Document xmlDom) {
		//normalize dom 
		xmlDom.getDocumentElement().normalize();
		BmecatOperationsResult result = new BmecatOperationsResult();
		//find the supplier in the XML File
		try {
			NodeList supplierNodes = xmlDom.getElementsByTagName("SUPPLIER");
			Node nodeSupplier = supplierNodes.item(0);
			Element supplierName = (Element) nodeSupplier;
			String supplier = supplierName
					.getElementsByTagName("SUPPLIER_NAME").item(0)
					.getFirstChild().getNodeValue();
			if (supplier == null) {
				result.setSupplierListIsEmpty(true);
				throw new NoSupplierInXml();
			}
			supplier = supplier.trim();
			result.setSuppliername(supplier);
			//compare the found supplier with existing suppliers
			SupplierBOA foundSuppliers = SupplierBOA.getInstance();
			List<BOSupplier> boSupplier = foundSuppliers.findAll();
			for (BOSupplier bo : boSupplier) {
				String existingSupplier = bo.getCompanyname();
				existingSupplier = existingSupplier.trim();
				if (existingSupplier.contentEquals(supplier)) {
					result.setSupplierExists(true);
				}
			}
		} catch (NoSupplierInXml e) {
			result.setSupplierListIsEmpty(true);
			System.out.println("No Supplier in XML");
		}
		return result;
	}
	/**Exception to simplify the code
	 * @author tim
	 *
	 */
	public class NoSupplierInXml extends Exception{
		private static final long serialVersionUID = -3973155161758893493L;
	}
}
