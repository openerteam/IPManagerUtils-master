package cn.magicbeans.android.ipmanager.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.magicbeans.android.ipmanager.R;
import cn.magicbeans.android.ipmanager.adapter.MBIPAdapter;
import cn.magicbeans.android.ipmanager.module.MBIPInfo;
import cn.magicbeans.android.ipmanager.utils.MBIPContant;
import cn.magicbeans.android.ipmanager.utils.MBIPUtils;
import cn.magicbeans.android.ipmanager.utils.MBPingUtils;
import cn.magicbeans.android.ipmanager.utils.MBWindowUtil;
import cn.magicbeans.android.ipmanager.utils.swipe.MBItemTouchListener;
import cn.magicbeans.android.ipmanager.utils.swipe.MBSwipeMenuRecyclerView;
import cn.magicbeans.android.ipmanager.view.MBAlertDialog;

import static cn.magicbeans.android.ipmanager.utils.MBStatusBarUtil.StatusBarLightMode;

/**
 * IP历史记录
 */
public class MBIPActivity extends Activity implements View.OnClickListener {


//    private ListView listView;
//
//    private MBIPAdapter adapter;

    private MBSwipeMenuRecyclerView ipRecyclerView;

    private MBIPAdapter ipAdapter;

    private TextView defaultName, defaultIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MBWindowUtil.TransparentStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mb_ip);
        StatusBarLightMode(this, StatusBarLightMode(this), true);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        findViewById(R.id.mb_add_ip_ImageView).setOnClickListener(this);
        findViewById(R.id.mb_phone_info_TextView).setOnClickListener(this);
        findViewById(R.id.mb_back_iv).setOnClickListener(this);
        ipRecyclerView = (MBSwipeMenuRecyclerView) findViewById(R.id.mb_ip_recyclerView);
        defaultName = (TextView) findViewById(R.id.mb_default_name_tv);
        defaultIP = (TextView) findViewById(R.id.mb_default_ip_tv);
        setDefaultView();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ipRecyclerView.setLayoutManager(linearLayoutManager);
        ipAdapter = new MBIPAdapter(this, mItemTouchListener);
        ipRecyclerView.setAdapter(ipAdapter);


//        listView = (ListView) findViewById(R.id.ip_list_view);
//        adapter = new MBIPAdapter(this);
//        listView.setAdapter(adapter);

//        new NetPing().execute();
    }

    @Override
    public void onClick(View v) {

         if (v.getId() == R.id.mb_back_iv) {
            finish();
        }else if (v.getId() == R.id.mb_add_ip_ImageView){
            Intent intent = new Intent(this, MBIPEditActivity.class);
            intent.putExtra(MBIPContant.TYPE, MBIPContant.OPERATE.INSERT.ordinal());
            startActivityForResult(intent, MBIPContant.REQUEST_CODE);
        }else if (v.getId() == R.id.mb_phone_info_TextView){
            Intent intent = new Intent(this, MBPhoneInfoActivity.class);
            startActivityForResult(intent, MBIPContant.REQUEST_INFO);

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MBIPContant.RESULT_CODE) {

//            if (adapter != null) {
//                adapter.refresh();
//            }
            int type = data.getIntExtra("type", 0);
            if (type>0) {
                finish();
            }else {
                if (ipAdapter != null) {
                    ipAdapter.refresh();
                }
                setDefaultView();
            }
        }else if (requestCode == MBIPContant.REQUEST_INFO){
            int type = data.getIntExtra("type", 0);
            if (type>0) {
                finish();
            }
        }
    }

    MBItemTouchListener mItemTouchListener = new MBItemTouchListener() {
        @Override
        public void onItemClick(MBIPInfo mbipInfo) {
            setData(mbipInfo);
        }

        @Override
        public void onLeftMenuClick(MBIPInfo mbipInfo) {

        }

        @Override
        public void onRightMenuClick(final MBIPInfo mbipInfo) {
            MBAlertDialog dialog = new MBAlertDialog(MBIPActivity.this, R.style.mb_dialog);
            dialog.setConfirmClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MBIPUtils.getInstance(MBIPActivity.this).deleteIPPort(mbipInfo);
                    ipAdapter.refresh();
                    setDefaultView();
                }
            });
            dialog.show();
        }
    };

    private void setData(MBIPInfo info) {
        List<MBIPInfo> infos = MBIPUtils.getInstance(MBIPActivity.this).queryData();
        for (int i = 0; i < infos.size() ; i++) {
            MBIPUtils.getInstance(MBIPActivity.this).setNotDefeaultIPPort(infos.get(i));
        }

        MBIPUtils.getInstance(MBIPActivity.this).setDefeaultIPPort(info);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MBIPContant.IP, info);
        MBIPActivity.this.setResult(MBIPContant.RESULT_CODE, resultIntent);
        MBIPActivity.this.finish();
    }

    private void setDefaultView(){
        MBIPInfo newInfo = null;
        List<MBIPInfo> infos = MBIPUtils.getInstance(MBIPActivity.this).queryData();
        for (int i = 0; i < infos.size() ; i++) {
            if (infos.get(i).isDefeault == 1) {
                newInfo = infos.get(i);
            }
        }
        if (newInfo == null){
            newInfo = infos.get(0);
            MBIPUtils.getInstance(MBIPActivity.this).setDefeaultIPPort(infos.get(0));
        }
        defaultName.setText(newInfo.getName());
        if (newInfo.getPort().equals("*")) {
            defaultIP.setText(newInfo.getIp());
        }else {
            defaultIP.setText(newInfo.getIp() + ":" + newInfo.getPort());
        }
    }

    private class NetPing extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            Boolean b = MBPingUtils.ping1("61.135.169.125");
            Log.e("ping", "ping-" + b);
            return s;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ipAdapter!=null) {
            if (ipAdapter.getSubscription() != null && !ipAdapter.getSubscription().isUnsubscribed()) {
                ipAdapter.getSubscription().unsubscribe();
            }
        }
    }
}
