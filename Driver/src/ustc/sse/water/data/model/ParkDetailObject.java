package ustc.sse.water.data.model;

/**
 *
 * 停车场详细信息对象. <br>
 * 方便数据处理.
 * <p>
 * Copyright: Copyright (c) 2015-2-12 上午10:01:52
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 黄志恒
 * @version 1.0.0
 */
public class ParkDetailObject {
	// private String _address;// 停车场地址
	private String _location;// 坐标经纬度
	private String _name; // 停车场名
	private String orderTen;
	private String orderTri;
	private String orderTwe;
	private String parkSum;
	private String payHalPay;// 停车半个小时
	private String payMorePay;// 停车多于一个小时
	private String payOneHour;
	private String phone; // 电话

	public String get_location() {
		return _location;
	}

	public String get_name() {
		return _name;
	}

	public String getOrderTen() {
		return orderTen;
	}

	public String getOrderTri() {
		return orderTri;
	}

	public String getOrderTwe() {
		return orderTwe;
	}

	public String getParkSum() {
		return parkSum;
	}

	public String getPayHalPay() {
		return payHalPay;
	}

	public String getPayMorePay() {
		return payMorePay;
	}

	public String getPayOneHour() {
		return payOneHour;
	}

	public String getPhone() {
		return phone;
	}

	public void set_location(String _location) {
		this._location = _location;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public void setOrderTen(String orderTen) {
		this.orderTen = orderTen;
	}

	public void setOrderTri(String orderTri) {
		this.orderTri = orderTri;
	}

	public void setOrderTwe(String orderTwe) {
		this.orderTwe = orderTwe;
	}

	public void setParkSum(String parkSum) {
		this.parkSum = parkSum;
	}

	public void setPayHalPay(String payHalPay) {
		this.payHalPay = payHalPay;
	}

	public void setPayMorePay(String payMorePay) {
		this.payMorePay = payMorePay;
	}

	public void setPayOneHour(String payOneHour) {
		this.payOneHour = payOneHour;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
