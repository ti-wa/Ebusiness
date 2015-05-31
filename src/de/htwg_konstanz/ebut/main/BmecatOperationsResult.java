package de.htwg_konstanz.ebut.main;

/**
 * @author tim
 *	simple DTO Result Object
 */
public class BmecatOperationsResult {
	private boolean supplierExists = false;
	private boolean supplierListIsEmpty = false;
	private String suppliername = "";
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

}
