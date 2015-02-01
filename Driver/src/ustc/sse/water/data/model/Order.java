package ustc.sse.water.data.model;
/**
 * 
 * 订单的model类. <br>
 * 订单的数据结构.
 * <p>
 * Copyright: Copyright (c) 2015-2-1 下午2:00:52
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 韩琼
 * @version 1.0.0
 */
public class Order {
	private Integer orderId;
	private Integer driverId;
	private Integer adminId;
	private String parkName;
	private String parkAddress;
	private Integer driverNum;
	private String orderDate;
	private String driverPhone;
	private String orderInfo;
	private String orderPrice;
	private Integer orderStatus;
	private Admin admin;
	private Driver driver;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
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

	public Integer getDriverNum() {
		return driverNum;
	}

	public void setDriverNum(Integer driverNum) {
		this.driverNum = driverNum;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
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
}
