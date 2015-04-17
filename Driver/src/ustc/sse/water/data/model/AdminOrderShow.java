
package ustc.sse.water.data.model;
/**
 * 
 * Model类. <br>
 * 管理员端需要的订单信息.
 * <p>
 * Copyright: Copyright (c) 2015-3-18 下午9:55:00
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 周晶鑫
 * @version 1.0.0
 */
public class AdminOrderShow {
	private int orderId; // 订单Id
	private String driverLicence; // 预定者的车牌号
	private String driverPhone; // 预定者的电话
	private int parkNumber; // 预定的车位数
	private String orderDate; // 预定时间
	private String orderDetail; // 订单详细
	private String orderPrice; // 订单总价
	private int orderStatus; // 订单状态
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getDriverLicence() {
		return driverLicence;
	}
	public void setDriverLicence(String driverLicence) {
		this.driverLicence = driverLicence;
	}
	public String getDriverPhone() {
		return driverPhone;
	}
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
	public int getParkNumber() {
		return parkNumber;
	}
	public void setParkNumber(int parkNumber) {
		this.parkNumber = parkNumber;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail;
	}
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

}
