package ustc.sse.water.data.model;

public class Order {
	private String pName; // parking name
	private String pAddress;// parking address
	private String dName; // driver name
	private String[] dLicences; // driver licences
	private String oDate; // order date
	private String dPhone; // driver phone
	private String oInfo; // order detail infomation
	private String oPrice; // order total price

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getpAddress() {
		return pAddress;
	}

	public void setpAddress(String pAddress) {
		this.pAddress = pAddress;
	}

	public String getdName() {
		return dName;
	}

	public void setdName(String dName) {
		this.dName = dName;
	}

	public String[] getdLicences() {
		return dLicences;
	}

	public void setdLicences(String[] dLicences) {
		this.dLicences = dLicences;
	}

	public String getoDate() {
		return oDate;
	}

	public void setoDate(String oDate) {
		this.oDate = oDate;
	}

	public String getdPhone() {
		return dPhone;
	}

	public void setdPhone(String dPhone) {
		this.dPhone = dPhone;
	}

	public String getoInfo() {
		return oInfo;
	}

	public void setoInfo(String oInfo) {
		this.oInfo = oInfo;
	}

	public String getoPrice() {
		return oPrice;
	}

	public void setoPrice(String oPrice) {
		this.oPrice = oPrice;
	}

}
