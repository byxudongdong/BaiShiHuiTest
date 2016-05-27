package userMessage;

import com.baishi.db.cn.FaJianSQLite;
import com.example.baishihuitong.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupWindowClas {
	private Context context;

	public PopupWindowClas() {
		// TODO Auto-generated constructor stub
	}

	
	public PopupWindowClas(Context context) {
		super();
		this.context = context;
	}
	public void showPopupWindow(View view, FaJianSQLite message,String name) {
		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.popmsg_window, null);
		final PopupWindow popupWindow = new PopupWindow(contentView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		TextView name_text = (TextView) contentView
				.findViewById(R.id.name_text);
		TextView price_text = (TextView) contentView
				.findViewById(R.id.price_text);
		TextView count_text = (TextView) contentView
				.findViewById(R.id.count_text);
		TextView message_text = (TextView) contentView
				.findViewById(R.id.message_text);
		TextView dangerous_text = (TextView) contentView
				.findViewById(R.id.dangerous_text);
		TextView scanTime_text =  (TextView) contentView
				.findViewById(R.id.scanTime_text);
		TextView renwudan_package = (TextView) contentView
				.findViewById(R.id.renwudan_package);
		
		if(name.equals("集包")){
			renwudan_package.setText("包号：");
		}
		name_text.setText(message.getNextSite());
		price_text.setText(message.getVehicleNo());
		count_text.setText(message.getBillCode());
		message_text.setText(message.getX_Auth_User() + "");
		dangerous_text.setText(message.getX_Auth_Site() + "");
		scanTime_text.setText(message.getScanDate() + " " + message.getScanTime());
		popupWindow.setTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.b));
		popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		int[] location = new int[2];  
		view.getLocationOnScreen(location);  
		
		// 设置好参数之后再show
//		popupWindow.showAsDropDown(view);
		 popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getHeight());  
	}
	
	public void setTextMessage(FaJianSQLite message){
		
	}

}
