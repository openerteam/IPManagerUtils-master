package cn.magicbeans.android.ipmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import cn.magicbeans.android.ipmanager.R;
import cn.magicbeans.android.ipmanager.utils.MBIPContant;
import cn.magicbeans.android.ipmanager.utils.MBPhoneInfoUtils;
import cn.magicbeans.android.ipmanager.utils.MBWindowUtil;

import static cn.magicbeans.android.ipmanager.utils.MBStatusBarUtil.StatusBarLightMode;

/**
 * date：2019/3/12 0012 on 17:59
 * desc:${本机信息}
 * author:BarryL
 */
public class MBPhoneInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView systemVersion, versionName, phoneName, resolutionRatio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MBWindowUtil.TransparentStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mb_phone_info);
        StatusBarLightMode(this, StatusBarLightMode(this), true);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        findViewById(R.id.mb_info_back_iv).setOnClickListener(this);
        findViewById(R.id.mb_info_finish_ImageView).setOnClickListener(this);
        systemVersion = (TextView) findViewById(R.id.mb_system_version_name);
        versionName = (TextView) findViewById(R.id.mb_app_version_name);
        phoneName = (TextView) findViewById(R.id.mb_phone_type);
        resolutionRatio = (TextView)findViewById(R.id.mb_phone_resolution_ratio);


        systemVersion.setText(MBPhoneInfoUtils.getSystemVersion());
        versionName.setText(MBPhoneInfoUtils.getVersionName(this));
        phoneName.setText(MBPhoneInfoUtils.getPhoneName());
        resolutionRatio.setText(MBPhoneInfoUtils.getResolutionRatio(this));

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mb_info_back_iv) {
            Intent intent = new Intent();
            intent.putExtra("type", 0);
            setResult(MBIPContant.REQUEST_INFO, intent);
            finish();
        } else if (i == R.id.mb_info_finish_ImageView) {
            Intent intent = new Intent();
            intent.putExtra("type", 1);
            setResult(MBIPContant.REQUEST_INFO, intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.putExtra("type", 0);
            setResult(MBIPContant.REQUEST_INFO, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
