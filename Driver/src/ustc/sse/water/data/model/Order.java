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
	private Integer orderId; // 订单Id
	private Integer driverId; // 驾驶员Id
	private Integer adminId; // 管理员Id
	private String parkName; // 停车场名
	private String parkAddress; // 停车场地址
	private Integer driverNum; // 驾驶员数量
	private String orderDate; // 订单日期
	private String driverPhone; // 驾驶员电话
	private String orderInfo; // 订单信息
	private String orderPrice; // 订单价格
	private String Money;
	

	private Integer orderStatus; // 订单状态
	private Admin admin; // 管理员
	private Driver driver; // 驾驶员

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

	public String getMoney() {
		return Money;
	}

	public void setMoney(String money) {
		Money = money;
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
