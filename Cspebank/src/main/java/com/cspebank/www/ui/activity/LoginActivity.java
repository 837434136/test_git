package com.cspebank.www.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cspebank.www.R;
import com.cspebank.www.ui.activity.dialog.BaseDialogActivity;
import com.cspebank.www.utils.ToastUtils;

/**
 * Created by deng on 2016/2/29.
 */
public class LoginActivity extends BaseDialogActivity implements View.OnClickListener{

    private LoginActivity mInstance;

    private EditText loginUserName;
    private EditText loginUserPwd;
    private TextView loginForgetPwd;
    private TextView loginVisitor;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mInstance = LoginActivity.this;
        assignViews();
        ToastUtils.showShort("new add");
    }

    private void assignViews() {
        loginUserName = (EditText) findViewById(R.id.login_user_name);
        loginUserPwd = (EditText) findViewById(R.id.login_user_pwd);
        loginForgetPwd = (TextView) findViewById(R.id.login_forget_pwd);
        loginVisitor = (TextView) findViewById(R.id.login_visitor);
        loginBtn = (Button) findViewById(R.id.login_btn);
    }

    @Override
    public void onClick(View v) {

    }
}
