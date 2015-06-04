package ustc.sse.water.data;

import java.util.UUID;

/**
 * 
 * Model类. <br>
 * 停车场订单信息.
 * <p>
 * Copyright: Copyright (c) 2015-3-18 下午9:56:37
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫
 * @version 1.0.0
 */
public class Order {

	private int orderId; // 订单Id
	private String uuid; // 唯一标识
	private String parkName; // 停车场名
	private String parkAddress; // 停车场地址
	private int driverNum; // 驾驶员数量
	private String orderDate; // 订单日期
	private String orderInfo; // 订单信息
	private String orderPrice; // 订单价格
	private Integer orderStatus; // 订单状态
	private Admin admin; // 管理员
	private Driver driver; // 驾驶员

	public Order() {
		uuid = UUID.randomUUID().toString();
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getDriverNum() {
		return driverNum;
	}

	public void setDriverNum(int driverNum) {
		this.driverNum = driverNum;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

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

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
