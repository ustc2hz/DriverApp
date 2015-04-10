package ustc.sse.water.data.model;

import java.util.List;

/**
 * 方便订单信息的传输
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
