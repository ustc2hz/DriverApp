package ustc.sse.water.data;

import java.util.Comparator;

/**
 * 比较类. <br>
 * 可以在list中用对象的元素做比较，然后再list中排序.
 * <p>
 * Copyright: Copyright (c) 2015年4月6日 下午9:30:10
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 黄志恒 sa614399@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class MyComparetor implements Comparator {

	@Override
	public int compare(Object lhs, Object rhs) {
		MarkerDistance md1 = (MarkerDistance) lhs;
		MarkerDistance md2 = (MarkerDistance) rhs;
		return (int) (md1.distance - md2.distance);
	}

}
