package model.providers;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class journalObject extends dataObject {

	private String Operation;
	private String checkNum;
	private String date;
	private String buyer="";
	
	String Totaloverride = "0";
	public String getBuyer() {
		return buyer;
	}
	
	@Override
	public String getTotal() {
		return Totaloverride;
	}
	
	
	public void setTotal(String s){
		Totaloverride = s;
	}
	
	

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}

	public String getOperation() {
		return Operation;
	}

	public void setOperation(String operation) {
		this.Operation = operation;
	}

	public journalObject(String crossID, String name, String supplier, String quantaty, String price, String pricebuy,String operation,String checknum, String date,String byuer) {
		super(crossID, name, supplier, quantaty, price, pricebuy);
		this.Operation = operation;
		this.date = date;
		this.checkNum= checknum;
		this.buyer = byuer;
		
	}

}
