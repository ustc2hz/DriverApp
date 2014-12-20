package ustc.sse.water.util.zf;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * 
 * 辅助类. <br>
 * 返回Fragment显示的组件.
 * <p>
 * Copyright: Copyright (c) 2014-12-7 上午9:49:20
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 张芳  sa614296@ustc.edu.cn
 * @version 1.0.0
 */
public class DummyFragment extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_number";
	//该方法返回值就是该Fragment显示的组件
	@Override
	public View onCreateView(LayoutInflater inflater ,ViewGroup container,Bundle savedInstanceStater){
		TextView textView=new TextView(getActivity());
		textView.setGravity(Gravity.START);
		//获取创建该Fragment时传入的参数Bundle
		Bundle args = getArguments();
		//设置TextView显示的文本
		textView.setText(args.getInt(ARG_SECTION_NUMBER)+"");
		textView.setTextSize(30);
		//返回该textView
		return textView;
	}
}
  