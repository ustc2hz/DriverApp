package ustc.sse.water.utils;

import java.util.List;

import ustc.sse.water.data.AdminOrderShow;
import ustc.sse.water.data.DriverOrderShow;

/**
 * 
 * 常量存储类. <br>
 * 暂时使用.
 * <p>
 * Copyright: Copyright (c) 2015-3-7 下午5:03:46
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫
 * @version 1.0.0
 */
public class ConstantKeep {

	// 暂存管理员的新订单
	public static List<AdminOrderShow> aos = null;

	// 暂存驾驶员的新订单
	public static List<DriverOrderShow> dos = null;

	// 暂存管理员的正在进行的订单
	public static List<AdminOrderShow> aosIng = null;

	// 暂存管理员的已经完成的订单
	public static List<AdminOrderShow> aosDown = null;

}
