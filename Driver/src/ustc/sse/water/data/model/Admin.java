package ustc.sse.water.data.model;
/**
 * 
 * 管理员model类. <br>
 * 管理员的数据结构.
 * <p>
 * Copyright: Copyright (c) 2015-2-1 下午1:59:25
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 韩琼
 * @version 1.0.0
 */
public class Admin {
	private Integer adminId; // 管理员id
	private String adminName; // 管理员名字
	private String adminPassword; // 管理员密码
	private String parkPhone; // 停车场电话

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
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
