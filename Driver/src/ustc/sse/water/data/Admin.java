package ustc.sse.water.data;

/**
 * 
 * Model类. <br>
 * 停车场管理员信息.
 * <p>
 * Copyright: Copyright (c) 2015-3-18 下午9:55:00
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 韩琼
 * @version 1.0.0
 */
public class Admin {

	private int adminId; // 管理员id
	private String adminName; // 管理员名字
	private String adminPassword; // 管理员密码
	private String parkPhone; // 停车场电话

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getParkPhone() {
		return parkPhone;
	}

	public void setParkPhone(String parkPhone) {
		this.parkPhone = parkPhone;
	}

}
