package sendupSet;

import com.baishi.db.cn.SaveShardMessage;
import com.example.baishihuitong.R;
import com.example.baishihuitong.SettingActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SendupSet extends Activity implements OnClickListener {

	public TextView sendup_return_button, sendupset_btn;
	public EditText sendup_change_count_flag,change_time_flag;
	public Switch sendup_spinner;
	public SaveShardMessage saveShard;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sendupset);
		findView();
		saveShard = new SaveShardMessage(SendupSet.this);
		sendup_change_count_flag.setText(saveShard.getchange_count_flag(SendupSet.this) + "");
		change_time_flag.setText(saveShard.getchange_time_flag(SendupSet.this) + "");
		if(saveShard.getchange_sendup_flag(SendupSet.this) == 0){
			sendup_spinner.setChecked(false);
		}else{
			sendup_spinner.setChecked(true);
		}
		
		
	}

	public void findView() {
		sendup_return_button = (TextView) findViewById(R.id.sendup_return_button);
		sendupset_btn = (TextView) findViewById(R.id.sendupset_btn);
		sendup_spinner = (Switch) findViewById(R.id.sendup_spinner);
		sendup_change_count_flag = (EditText) findViewById(R.id.sendup_change_count_flag);
		change_time_flag = (EditText) findViewById(R.id.sendup_change_time_flag);
		sendup_return_button.setOnClickListener(this);
		sendupset_btn.setOnClickListener(this);
		sendup_spinner.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					saveShard.change_sendup_flag = 1;
				}else{
					saveShard.change_sendup_flag = 0;
				}
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sendup_return_button:
			finish();
			break;
		case R.id.sendupset_btn:
			ShowDialog("保存设置信息");
			break;
		}
	}

	public void ShowDialog(String message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(SendupSet.this); 
		builder.setCancelable(false);
		builder.setMessage("确认" + message + "?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				saveShard.Sharedchange_sendup_flag(saveShard.change_sendup_flag);
				saveShard.Sharedchange_count_flag(Integer.parseInt(sendup_change_count_flag.getText().toString()));
				saveShard.Sharedchange_time_flag(Integer.parseInt(change_time_flag.getText().toString()));
				finish();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
}
