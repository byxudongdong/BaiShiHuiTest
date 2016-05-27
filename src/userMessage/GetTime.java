package userMessage;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

public class GetTime {
public GetTime() {
	// TODO Auto-generated constructor stub
}

/**
 * 获得当前时间
 */
public String getCurrentTime(String current_time) {
	Date date = new Date();
	SimpleDateFormat formatter = new SimpleDateFormat(current_time);
	return formatter.format(date);
}
//获取昨天日期
		public String yesTerDay() {
			Format f = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			System.out.println("昨天:" + f.format(c.getTime()));
			c.add(Calendar.DAY_OF_MONTH, -1);
			String time = f.format(c.getTime());
			return time;
		}
		// 获取前天日期
		public String beforeYesterDay() {
			Format f = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, -2);
			System.out.println("前天:" + f.format(c.getTime()));
			String time = f.format(c.getTime());
			return time;
		}
	// 获取今天前第四天
			public String before4YesterDay() {
				Format f = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, -3);
				String time = f.format(c.getTime());
				return time;
			}
}
