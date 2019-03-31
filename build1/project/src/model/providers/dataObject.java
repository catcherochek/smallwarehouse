package model.providers;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.scene.control.Label;

/**
 * @author Клим
 * создает объект инкапсулирующий объект данных
 */
public class dataObject implements ITableRecord {
	private Label summlabel = null;
	private  String CrossID;
	private String Name;
	private  String Supplier;
	private  String Quantaty;
	private  String Price;
	protected String Total;
	private String PriceBuy="0";
	public String getPriceBuy() {
		return PriceBuy;
	}
	public void setPriceBuy(String priceBuy) {
		PriceBuy = priceBuy;
	}
	public dataObject(String crossID, String name, String supplier, String quantaty,
			String price) {
		super();
		CrossID = crossID;
		Name = name;
		Supplier = supplier;
		Quantaty = quantaty;
		Price = price;
		setTotal("0");
	}
	public dataObject(String crossID, String name, String supplier, String quantaty,
			String price, String pricebuy) {
		this(crossID,name,supplier,quantaty,price);
		this.PriceBuy = pricebuy;
		//this.summlabel = lbl;
		
	}
	/* (non-Javadoc)
	 * @see model.ITableRecord#getTotal()
	 */
	@Override
	public String getTotal() {
		double tempval1,tempval2;
		
		tempval1 = new BigDecimal(this.Price).setScale(2, RoundingMode.HALF_UP).doubleValue();
		tempval2 = new BigDecimal(this.Quantaty).setScale(2, RoundingMode.HALF_UP).doubleValue();
		//res = 
		return String.valueOf(tempval1*tempval2);
		
	}
	/* (non-Javadoc)
	 * @see model.ITableRecord#getCrossID()
	 */
	@Override
	public String getCrossID() {
		return CrossID;
	}
	/* (non-Javadoc)
	 * @see model.ITableRecord#setCrossID(java.lang.String)
	 */
	@Override
	public void setCrossID(String crossID) {
		CrossID = crossID;
	}
	/* (non-Javadoc)
	 * @see model.ITableRecord#getName()
	 */
	@Override
	public String getName() {
		return Name;
	}
	/* (non-Javadoc)
	 * @see model.ITableRecord#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		Name = name;
	}
	/* (non-Javadoc)
	 * @see model.ITableRecord#getSupplier()
	 */
	@Override
	public String getSupplier() {
		return Supplier;
	}
	/* (non-Javadoc)
	 * @see model.ITableRecord#setSupplier(java.lang.String)
	 */
	@Override
	public void setSupplier(String supplier) {
		Supplier = supplier;
	}
	/* (non-Javadoc)
	 * @see model.ITableRecord#getQuantaty()
	 */
	@Override
	public String getQuantaty() {
		return Quantaty;
	}
	/* (non-Javadoc)
	 * @see model.ITableRecord#setQuantaty(java.lang.String)
	 */
	@Override
	public void setQuantaty(String quantaty) {
		Quantaty = quantaty;
	}
	/* (non-Javadoc)
	 * @see model.ITableRecord#getPrice()
	 */
	@Override
	public String getPrice() {
		return Price;
	}
	/* (non-Javadoc)
	 * @see model.ITableRecord#setPrice(java.lang.String)
	 */
	@Override
	public void setPrice(String price) {
		Price = price;
	}
	public void setTotal(String total) {
		Total = total;
	}

}
