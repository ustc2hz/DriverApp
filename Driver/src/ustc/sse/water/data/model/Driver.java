package ustc.sse.water.data.model;
/**
 * 
 * Driver的Model类. <br>
 * 驾驶员的数据结构.
 * <p>
 * Copyright: Copyright (c) 2015-2-1 下午1:55:28
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 韩琼 
 * @version 1.0.0
 */
public class Driver {
	private Integer driverId; // 驾驶员id
	private String driverName; // 驾驶员名字
	private String driverPassword; // 驾驶员密码
	private String driverPhone; // 驾驶员电话

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverPassword() {
		return driverPassword;
	}

	public void setDriverPassword(String driverPassword) {
		this.driverPassword = driverPassword;
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

}
