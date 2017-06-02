package com.example.mobilepolicedevice;

import com.example.bean.Person;
import com.example.db.DatabaseUtil;
import com.example.db.MyHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity{
	
	/*界面组件*/
	private EditText et_username;
	private EditText et_password;
	private Button btn_login;
	
	private DatabaseUtil mDBUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_all);
		
		initview();
		
	}
	
	/*初始化*/
	private void initview(){
		et_username = (EditText)findViewById(R.id.login_edit_userName);
		et_password = (EditText)findViewById(R.id.login_edit_password);
		btn_login = (Button)findViewById(R.id.login_but_landing);
		//获取数据库
		mDBUtil = new DatabaseUtil(LoginActivity.this);
		
		//判断警员表内是否已经有初始化数据
		String sql = "SELECT * FROM "+ MyHelper.TABLE_NAME_PoliceInformation +";";
		Person person = new Person();
		person = mDBUtil.queryBySQL_PoliceInformation(sql);
		if(person.getUsername()==null){
			person.setUsername("000001");
			person.setPassword("123456");
			mDBUtil.Insert_PoliceInformation(person);
			
			person.setUsername("000002");
			person.setPassword("654321");
			mDBUtil.Insert_PoliceInformation(person);
		}
		
		//登录判断
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 判断账号和密码是否为空
				if(null == et_username.getText().toString().trim() || et_username.getText().toString().length() == 0){
					Toast.makeText(getApplicationContext(), "请输入账号", Toast.LENGTH_SHORT).show();
					return;
				}else if(null == et_password.getText().toString().trim() || et_password.getText().toString().length() == 0){
					Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
					return;
				}
				//根据输入账号查找警员信息
				Person person = mDBUtil.queryByUsername_PoliceInformation(et_username.getText().toString());
				//验证账户是否存在
				if(person==null){
					Toast.makeText(getApplicationContext(), "账户不存在", Toast.LENGTH_SHORT).show();
					return;
				}
				//验证输入的密码是否准确
				if(et_password.getText().toString() == person.getPassword() || et_password.getText().toString().equals(person.getPassword())){
//					Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
//					return;
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("username", et_username.getText().toString());
					intent.putExtras(bundle);
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
					return;
				}
				
			}
		});
		
	}

}
