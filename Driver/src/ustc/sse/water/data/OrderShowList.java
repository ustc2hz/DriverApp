package ustc.sse.water.data;

import java.util.List;

/**
 * 
 * Model类. <br>
 * 方便订单信息的传输.
 * <p>
 * Copyright: Copyright (c) 2015-3-18 下午9:55:00
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫
 * @version 1.0.0
 */
public class OrderShowList {

	private List<AdminOrderShow> adminShow; // 存储多个管理员订单
	private List<DriverOrderShow> driverShow; // 存储多个多个驾驶员订单

	public OrderShowList() {
		adminShow = null;
		driverShow = null;
	}

	public List<AdminOrderShow> getAdminShow() {
		return adminShow;
	}

	public void setAdminShow(List<AdminOrderShow> adminShow) {
		this.adminShow = adminShow;
	}

	public List<DriverOrderShow> getDriverShow() {
		return driverShow;
	}

	public void setDriverShow(List<DriverOrderShow> driverShow) {
		this.driverShow = driverShow;
	}

}
