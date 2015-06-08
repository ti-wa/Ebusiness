package de.htwg_konstanz.ebut.main;

import java.math.BigDecimal;
import java.util.List;

import org.w3c.dom.*;

import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOCountry;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOProduct;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOPurchasePrice;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSalesPrice;
import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.CountryBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.PriceBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.ProductBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.api.boa.SupplierBOA;
import de.htwg_konstanz.ebus.framework.wholesaler.vo.util.Constants;

/**
 * Operation for a Bmecat XML File
 * 
 * @author tim
 *
 */
public class BmecatOperations {
	private static BmecatOperations instance = null;
	BmecatOperationsResult result;

	private BmecatOperations() {

	}

	/**
	 * Singelton Pattern
	 */
	public static BmecatOperations getInstance() {
		if (instance == null) {
			instance = new BmecatOperations();
		}
		return instance;
	}

	/**
	 * check if the supplier of an bmecat exist in database and link it in
	 * result
	 * 
	 * @param xmlDom
	 * @return a Result object
	 */
	public BmecatOperationsResult checkSupplierExist(org.w3c.dom.Document xmlDom) {
		result = new BmecatOperationsResult();
		// find the supplier in the XML File
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
			// compare the found supplier with existing suppliers
			SupplierBOA foundSuppliers = SupplierBOA.getInstance();
			List<BOSupplier> boSuppliers = foundSuppliers.findAll();
			for (BOSupplier boSupp : boSuppliers) {
				String existingSupplier = boSupp.getCompanyname();
				existingSupplier = existingSupplier.trim();
				System.out.println(existingSupplier);
				if (existingSupplier.contentEquals(supplier)) {
					result.setBoSupplier(boSupp);
					result.setSupplierExists(true);
				}
			}
		} catch (NoSupplierInXml e) {
			result.setSupplierListIsEmpty(true);
			System.out.println("No Supplier in XML");
		}
		return result;
	}

	public BmecatOperationsResult writeXmlToDatabase(org.w3c.dom.Document xmlDom) {
		int productCounter = 0;
		NodeList articelNodes = xmlDom.getElementsByTagName("ARTICLE");
		for (int i = 0; i < articelNodes.getLength(); i++) {
			String supplier_Aid = "", description_Short = "", description_Long = "";
			Node articelNode = articelNodes.item(i);
			if (articelNode.getNodeType() == Node.ELEMENT_NODE) {
				// get product details
				Element articel = (Element) articelNode;
				supplier_Aid = articel.getElementsByTagName("SUPPLIER_AID")
						.item(0).getFirstChild().getNodeValue();

				description_Short = "";
				try {
					if (null != articel
							.getElementsByTagName("DESCRIPTION_SHORT").item(0)
							.getFirstChild().getNodeValue()) {
						description_Short = articel
								.getElementsByTagName("DESCRIPTION_SHORT")
								.item(0).getFirstChild().getNodeValue();
					}
				} catch (Exception e) {
					System.out.println("Null in Description Short");
				}

				description_Long = "";
				try {
					description_Long = articel
							.getElementsByTagName("DESCRIPTION_LONG").item(0)
							.getFirstChild().getNodeValue();
				} catch (Exception e) {
					System.out.println("Null in Description long");
				}

				/*
				 * System.out.println("-------------ARTICLE--------------");
				 * System.out.println("SUPPLIER_AID: " + supplier_Aid);
				 * System.out.println("DESCRIPTION_SHORT: " +
				 * description_Short); System.out.println("DESCRIPTION_LONG: " +
				 */
				// set Product details to bo Object

				BOProduct product = new BOProduct();
				product.setOrderNumberSupplier(supplier_Aid);
				product.setOrderNumberCustomer(supplier_Aid);
				product.setLongDescription(description_Long);
				product.setLongDescriptionCustomer(description_Long);
				product.setShortDescription(description_Short);
				product.setShortDescriptionCustomer(description_Short);
				product.setSupplier(result.getBoSupplier());
				
				// check if product is still in database
				BOProduct check = ProductBOA.getInstance()
						.findByOrderNumberCustomer(
								product.getOrderNumberCustomer());
				if (check != null) {
					System.out.println(product.getShortDescription()
							+ " already in DB");
					result.addProductToNotImportetProduct(description_Short);
					continue;
				}
				// save product
				ProductBOA.getInstance().saveOrUpdate(product);
				productCounter++;

				// Article Price
				NodeList priceNodes = articel
						.getElementsByTagName("ARTICLE_PRICE");
				for (int k = 0; k < priceNodes.getLength(); k++) {
					Node priceNode = priceNodes.item(k);
					if (priceNode.getNodeType() == Node.ELEMENT_NODE) {
						Element price = (Element) priceNode;
						/*
						 * System.out.println("--------- PRICE-----------:");
						 * System.out.println("PRICE_TYPE: " +
						 * price.getAttribute("price_type"));
						 * System.out.println("PRICE_AMOUNT: " +
						 * price.getElementsByTagName
						 * ("PRICE_AMOUNT").item(0).getFirstChild
						 * ().getNodeValue());
						 * System.out.println("PRICE_CURRENCY: " +
						 * price.getElementsByTagName
						 * ("PRICE_CURRENCY").item(0).getFirstChild
						 * ().getNodeValue()); System.out.println("TAX: " +
						 * price
						 * .getElementsByTagName("TAX").item(0).getFirstChild
						 * ().getNodeValue()); System.out.println("TERRITORY: "
						 * + price.getElementsByTagName("TERRITORY").item(0).
						 * getFirstChild().getNodeValue());
						 */
						// compute sales price (price*1.2)
						BigDecimal amount = new BigDecimal(price
								.getElementsByTagName("PRICE_AMOUNT").item(0)
								.getFirstChild().getNodeValue());
						BOSalesPrice salesPrice = new BOSalesPrice(
								computeSalesPrice(amount), new BigDecimal(price
										.getElementsByTagName("TAX").item(0)
										.getFirstChild().getNodeValue()),
								price.getAttribute("price_type"));
						// Setting the country
						BOCountry country = null;
						country = CountryBOA.getInstance().findCountry(
								price.getElementsByTagName("TERRITORY").item(0)
										.getFirstChild().getNodeValue());
						// sales Price
						salesPrice.setCountry(country);
						salesPrice.setProduct(product);
						salesPrice
								.setLowerboundScaledprice(Constants.DEFAULT_LOWERBOUND_SCALED_PRICE);
						PriceBOA.getInstance().saveOrUpdateSalesPrice(
								salesPrice);
						// purchase price
						BOPurchasePrice purchasePrice = new BOPurchasePrice(
								amount, new BigDecimal(price
										.getElementsByTagName("TAX").item(0)
										.getFirstChild().getNodeValue()),
								price.getAttribute("price_type"));
						purchasePrice.setCountry(country);
						purchasePrice.setProduct(product);
						purchasePrice.setLowerboundScaledprice(1);
						PriceBOA.getInstance().saveOrUpdatePurchasePrice(
								purchasePrice);
					}
				}
			}
		}
		result.setProductCounter(productCounter);
		return result;
	}

	/**
	 * Price * 20%.
	 *
	 * @param amount
	 *            BigDecimal
	 * @return BigDecimal
	 */
	private BigDecimal computeSalesPrice(final BigDecimal amount) {
		return amount.add(amount.multiply(new BigDecimal(0.2)));
	}

	/**
	 * Exception to simplify the code
	 * 
	 * @author tim
	 *
	 */
	public class NoSupplierInXml extends Exception {
		private static final long serialVersionUID = -3973155161758893493L;
	}
}
