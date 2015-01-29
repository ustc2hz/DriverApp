package ustc.sse.water.data.model;

/**
 * ParkDetailData 保存停车场具体停车数据信息的数据结构 <br>
 * 本类中包含了7个数据元素，用来保存停车场具体停车数据信息.包括：车位数，预定规则，停车收费规则
 * <p>
 * Copyright: Copyright (c) 2015年1月29日 下午1:54:25
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 黄志恒
 * @version 1.0.0
 */
public class ParkDetailData {

	// 预定10分钟的价格
	private String orderOfTen;
	// 预定30分钟的价格
	private String orderOfTri;
	// 预定20分钟的价格
	private String orderOfTwe;
	// 为了数据转换方便，所有数据均设置为String类型
	// 预定车位总数
	private String orderSpaceSum;
	// 停车半个小时的价格
	private String spaceOfHal;
	// 停车多于一个小时的价格
	private String spaceOfMor;
	// 停车一个小时的价格
	private String spaceOfOne;

	public String getOrderOfTen() {
		return orderOfTen;
	}

	public String getOrderOfTri() {
		return orderOfTri;
	}

	public String getOrderOfTwe() {
		return orderOfTwe;
	}

	public String getOrderSpaceSum() {
		return orderSpaceSum;
	}

	public String getSpaceOfHal() {
		return spaceOfHal;
	}

	public String getSpaceOfMor() {
		return spaceOfMor;
	}

	public String getSpaceOfOne() {
		return spaceOfOne;
	}

	public void setOrderOfTen(String orderOfTen) {
		this.orderOfTen = orderOfTen;
	}

	public void setOrderOfTri(String orderOfTri) {
		this.orderOfTri = orderOfTri;
	}

	public void setOrderOfTwe(String orderOfTwe) {
		this.orderOfTwe = orderOfTwe;
	}

	public void setOrderSpaceSum(String orderSpaceSum) {
		this.orderSpaceSum = orderSpaceSum;
	}

	public void setSpaceOfHal(String spaceOfHal) {
		this.spaceOfHal = spaceOfHal;
	}

	public void setSpaceOfMor(String spaceOfMor) {
		this.spaceOfMor = spaceOfMor;
	}

	public void setSpaceOfOne(String spaceOfOne) {
		this.spaceOfOne = spaceOfOne;
	}

}
