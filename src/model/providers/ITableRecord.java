package model.providers;

public interface ITableRecord {
	

	String getTotal();

	String getCrossID();

	void setCrossID(String crossID);

	String getName();

	void setName(String name);

	String getSupplier();

	void setSupplier(String supplier);

	String getQuantaty();

	void setQuantaty(String quantaty);

	String getPrice();

	void setPrice(String price);

}