package ustc.sse.water.data.model;

public class ParkDetailObject {
	private String _name;
	private String _address;
	private String phone;
	private String orderTen;
	private String orderTri;
	
	private String orderTwe;
	private String payHalPay;// 停车半个小时
	private String payMorePay;// 停车多于一个小时
	private String payOneHour;
	private String parkSum;

	public String get_name() {
		return _name;
	}
	public void set_name(String _name) {
		this._name = _name;
	}
	public String get_address() {
		return _address;
	}
	public void set_address(String _address) {
		this._address = _address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOrderTen() {
		return orderTen;
	}
	public void setOrderTen(String orderTen) {
		this.orderTen = orderTen;
	}
	public String getOrderTri() {
		return orderTri;
	}
	public void setOrderTri(String orderTri) {
		this.orderTri = orderTri;
	}
	public String getOrderTwe() {
		return orderTwe;
	}
	public void setOrderTwe(String orderTwe) {
		this.orderTwe = orderTwe;
	}
	public String getPayHalPay() {
		return payHalPay;
	}
	public void setPayHalPay(String payHalPay) {
		this.payHalPay = payHalPay;
	}
	public String getPayMorePay() {
		return payMorePay;
	}
	public void setPayMorePay(String payMorePay) {
		this.payMorePay = payMorePay;
	}
	public String getPayOneHour() {
		return payOneHour;
	}
	public void setPayOneHour(String payOneHour) {
		this.payOneHour = payOneHour;
	}
	public String getParkSum() {
		return parkSum;
	}
	public void setParkSum(String parkSum) {
		this.parkSum = parkSum;
	}

}
