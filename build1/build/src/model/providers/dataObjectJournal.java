package model.providers;

public class dataObjectJournal extends dataObject {
	private String status;

	public dataObjectJournal(String crossID, String name, String supplier, String quantaty, String price, String status) {
		super(crossID, name, supplier, quantaty, price);
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
