package com.ipmanager.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import cn.magicbeans.android.ipmanager.module.MBIPInfo;
import cn.magicbeans.android.ipmanager.ui.MBIPActivity;
import cn.magicbeans.android.ipmanager.utils.MBFloatWindowUtils;
import cn.magicbeans.android.ipmanager.utils.MBIPContant;
import cn.magicbeans.android.ipmanager.utils.MBIPUtils;
import cn.magicbeans.android.ipmanager.utils.MBShakeUtils;


public class IPSetActivity extends Activity {

    private TextView ipView;

    private static String defaultIP = "192.168.1.111";

    private static String defaultPort = "1111";

    private static String defaultName = "陈雳的服务器";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipset);

        ipView = (TextView) findViewById(R.id.ip_tv);
        findViewById(R.id.set_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIP();
            }
        });

        String ip = MBIPUtils.getInstance(this).getIPPort();

        if (TextUtils.isEmpty(ip)) {
            ipView.setText(defaultIP + ";" + defaultPort);
        } else {
            ipView.setText("默认IP地址:" + ip);
        }


        MBFloatWindowUtils floatWindowUtils= new MBFloatWindowUtils();
        floatWindowUtils.init(this, defaultIP, defaultPort, defaultName);

    }

    private void setIP() {
        Intent intent = new Intent(IPSetActivity.this, MBIPActivity.class);
        startActivityForResult(intent, MBIPContant.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MBIPContant.RESULT_CODE) {

            MBIPInfo info = (MBIPInfo) data.getSerializableExtra(MBIPContant.IP);
//            if (!TextUtils.isEmpty(info.getPort())) {
//                ipView.setText("新IP地址：" + info.ip + ":" + info.port);
//            }else {
//                ipView.setText("新IP地址：" + info.ip);
//            }
            if (!info.getPort().equals("*")) {
                ipView.setText("新IP地址：" + info.ip + ":" + info.port);
            }else {
                ipView.setText("新IP地址：" + info.ip);
            }

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        MBShakeUtils.getInstance(this).init(defaultIP, defaultPort, defaultName);

    }

    @Override
    protected void onStop() {
        super.onStop();
        MBShakeUtils.getInstance(this).unRegister();
    }


}
