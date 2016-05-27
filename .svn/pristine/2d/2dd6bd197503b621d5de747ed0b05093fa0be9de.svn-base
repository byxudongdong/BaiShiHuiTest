package com.arrive;

import java.util.ArrayList;
import java.util.List;

import com.baishi.db.cn.FaJianSQLite;
import com.example.baishihuitong.R;
import com.example.baishihuitong.R.color;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PrivateListDaoAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<DaoJianSQLite> list;
	public String text_data_string, text_station_msg;

	public PrivateListDaoAdapter(Context context,List<DaoJianSQLite> list) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.list = list;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setmMessageItems(List<DaoJianSQLite> list) {
		this.list = list;
	}

	public int sending_count_n;
	public void setNowSending(int sending_count_n){
		this.sending_count_n = sending_count_n;
	}
	
	public int position_background = 0;
	public void setpostion(int position){
		position_background = position;
	}
	@Override
	public int getCount() {
		if (list == null) {
			list = new ArrayList<DaoJianSQLite>();
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position).getIndex();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	int i = 0;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// INDEX = position;
		ViewHolder holder = null;
		View itemView = mInflater.inflate(R.layout.list_send_view, null);

		holder = new ViewHolder(itemView);

		DaoJianSQLite item = list.get(position);
		
		if(position < sending_count_n){
			holder.text_data.setTextColor(mContext.getResources().getColor(R.color.red));
		}
		if(position_background == position){
			holder.text_data.setTextColor(mContext.getResources().getColor(R.color.yellow));
		}
		holder.text_data.setText(item.getBillCode() +"   "+ item.getVehicleNo());
		notifyDataSetChanged();
		return itemView;
	}

	private static class ViewHolder {
		public TextView text_data;

		ViewHolder(View view) {
			text_data = (TextView) view.findViewById(R.id.text_data);
		}
	}

}
