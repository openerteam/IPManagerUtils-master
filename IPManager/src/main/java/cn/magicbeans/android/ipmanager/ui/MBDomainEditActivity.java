package cn.magicbeans.android.ipmanager.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.magicbeans.android.ipmanager.R;
import cn.magicbeans.android.ipmanager.module.MBIPInfo;
import cn.magicbeans.android.ipmanager.utils.MBIPContant;
import cn.magicbeans.android.ipmanager.utils.MBIPUtils;

/**
 * date：2017/11/15 0015 on 13:57
 * desc:${}
 * author:BarryL
 */
public class MBDomainEditActivity extends Activity implements View.OnClickListener {

    private EditText domainView;

    private TextView titleView;

    private int type;

    private MBIPInfo info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain_edit);
        initView();
    }

    private void initView(){
        findViewById(R.id.mb_domain_commit_tv).setOnClickListener(this);
        findViewById(R.id.back_iv).setOnClickListener(this);
        titleView = (TextView) findViewById(R.id.title_tv);
        domainView = (EditText) findViewById(R.id.domian_et);

        type = getIntent().getIntExtra(MBIPContant.TYPE, MBIPContant.OPERATE.INSERT.ordinal());
        if (type == MBIPContant.OPERATE.INSERT.ordinal()) {
            titleView.setText(getString(R.string.mb_insert));
        } else if (type == MBIPContant.OPERATE.EDIT.ordinal()) {
            titleView.setText(getString(R.string.mb_edit));
            info = (MBIPInfo) getIntent().getSerializableExtra(MBIPContant.IP);
            if (info != null) {
                domainView.setText(info.ip);
//                String[] ips = info.ip.split("\\.");
//                if (ips != null && ips.length == 4) {
//                    ipView1.setText(ips[0] + "".replace(".", ""));
//                    ipView2.setText(ips[1] + "".replace(".", ""));
//                    ipView3.setText(ips[2] + "".replace(".", ""));
//                    ipView4.setText(ips[3] + "".replace(".", ""));
//
//                }
//                portView.setText(info.port);
            }
        }

//        ipView1.setSelection(ipView1.length());

    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        if (v.getId() == R.id.mb_domain_commit_tv) {
            commitData();
        } else if (v.getId() == R.id.back_iv) {
            finish();
        }
    }

    /**
     * 保存数据
     */
    private void commitData() {

        String ip1 = domainView.getText().toString();

        if (TextUtils.isEmpty(ip1)) {
            Toast.makeText(this, R.string.mb_empty_ip, Toast.LENGTH_SHORT).show();
            return;
        }
//        String port = portView.getText().toString();
//
//        if (TextUtils.isEmpty(port)) {
//            Toast.makeText(this, R.string.mb_empty_port, Toast.LENGTH_SHORT).show();
//            return;
//        }

        MBIPInfo temp = null;
        if (info == null) {
            info = new MBIPInfo(ip1, "*", 0);
        } else {
            temp = new MBIPInfo();
            temp.setIp(ip1);
            temp.setPort("*");
            temp.setIsDefeault(info.isDefeault);
        }

        if (type == MBIPContant.OPERATE.INSERT.ordinal()) {
            MBIPUtils.getInstance(this).insertIPPort(info);
        } else if (type == MBIPContant.OPERATE.EDIT.ordinal()) {
            MBIPUtils.getInstance(this).updateIPPort(info, temp);
        }
        setData();
    }

    private void setData() {
        Intent resultIntent = new Intent();
        setResult(MBIPContant.RESULT_CODE, resultIntent);
        finish();
    }

    /**
     * 关闭软键盘
     */
    private void hideKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(domainView.getWindowToken(), 0);
    }


}
