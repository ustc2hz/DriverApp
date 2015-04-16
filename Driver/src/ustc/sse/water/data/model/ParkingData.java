package ustc.sse.water.data.model;

/**
 * ParkingData. 存数停车场名车和地址的数据结构<br>
 * 3个参数和对应的setter，getter方法.
 * <p>
 * Copyright: Copyright (c) 2015年1月27日 下午3:46:00
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 黄志恒
 * @version 1.0.0
 */

public class ParkingData {
	private String _address;
	private String _name;
	private String phone;

	public String get_address() {
		return _address;
	}

	public String get_name() {
		return _name;
	}

	public String getPhone() {
		return phone;
	}

	public void set_address(String _address) {
		this._address = _address;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return this._name + "#" + this._address + "#" + this.phone;
	}

}
