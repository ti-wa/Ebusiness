package de.htwg_konstanz.ebut.main;

import java.util.ArrayList;
import java.util.List;


import de.htwg_konstanz.ebus.framework.wholesaler.api.bo.BOSupplier;

/**simple DTO Result Object
 * @author tim 
 */
public class BmecatOperationsResult {
	private boolean supplierExists = false;
	private boolean supplierListIsEmpty = false;
	private String suppliername = "";
	private BOSupplier boSupplier = null;
	private List<String> notImportetProduct = new ArrayList<String>();
	private int productCounter;

	public int getProductCounter() {
		return productCounter;
	}

	protected void setProductCounter(int productCounter) {
		this.productCounter = productCounter;
	}

	protected BOSupplier getBoSupplier() {
		return boSupplier;
	}

	protected void setBoSupplier(BOSupplier boSupplier) {
		this.boSupplier = boSupplier;
	}

	public String getSuppliername() {
		return suppliername;
	}

	protected void setSuppliername(String suppliername) {
		this.suppliername = suppliername;
	}

	public boolean isSupplierListIsEmpty() {
		return supplierListIsEmpty;
	}

	protected void setSupplierListIsEmpty(boolean supplierListIsEmpty) {
		this.supplierListIsEmpty = supplierListIsEmpty;
	}

	public boolean isSupplierExists() {
		return supplierExists;
	}

	protected void setSupplierExists(boolean supplierExists) {
		this.supplierExists = supplierExists;
	}

	protected void addProductToNotImportetProduct(String product) {
		notImportetProduct.add(product);
	}

	public List<String> getNonImportetProducts() {
		if (notImportetProduct.isEmpty())
			return null;
		return notImportetProduct;
	}
}
