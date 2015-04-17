package ustc.sse.water.data.model;

/**
 * 
 * Model类. <br>
 * 驾驶员订单.
 * <p>
 * Copyright: Copyright (c) 2015-3-18 下午9:55:00
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 周晶鑫
 * @version 1.0.0
 */
public class DriverOrderShow {
	private String parkName; // 停车场名
	private String parkAddress; // 停车场地址
	private int parkNumber; // 预定车位数
	private String orderDate; // 订单日期
	private String orderDetail; // 订单详情
	private String orderPrice; // 订单总价
	private String adminPhone; // 管理员电话
	private int orderStatus; // 订单状态

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public String getParkAddress() {
		return parkAddress;
	}

	public void setParkAddress(String parkAddress) {
		this.parkAddress = parkAddress;
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

	public String getAdminPhone() {
		return adminPhone;
	}

	public void setAdminPhone(String adminPhone) {
		this.adminPhone = adminPhone;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

}
