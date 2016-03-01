package com.cspebank.www.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.cspebank.www.R;
import com.cspebank.www.app.CspeManager;
import com.cspebank.www.ui.activity.dialog.BaseDialogActivity;
import com.cspebank.www.utils.Constant;
import com.cspebank.www.utils.PreferencesUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseDialogActivity {

    private SplashActivity mInstance;
    private boolean IS_FIRST;
    private CspeManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mInstance = SplashActivity.this;
        IS_FIRST = PreferencesUtils.getBoolean(mInstance, Constant.IS_FIRST, true);

        //设置一个定时器5秒，用来执行重新登录操作
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                mInstance.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (IS_FIRST) {
                            //跳转到引导界面
                            startActivity(new Intent(mInstance, GuideActivity.class));
                        } else {
                            startActivity(new Intent(mInstance, LoginActivity.class));
                        }
                        mInstance.finish();
                    }
                });

            }
        }, 2000);
    }
}
