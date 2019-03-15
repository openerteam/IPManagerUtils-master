package cn.magicbeans.android.ipmanager.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.magicbeans.android.ipmanager.R;
import cn.magicbeans.android.ipmanager.module.MBIPInfo;
import cn.magicbeans.android.ipmanager.utils.MBEditUtils;
import cn.magicbeans.android.ipmanager.utils.MBIPContant;
import cn.magicbeans.android.ipmanager.utils.MBIPUtils;
import cn.magicbeans.android.ipmanager.utils.MBWindowUtil;

import static cn.magicbeans.android.ipmanager.utils.MBStatusBarUtil.StatusBarLightMode;

/**
 * IP设置
 */
public class MBIPEditActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText ipView1, ipView2, ipView3, ipView4, portView, nameView, domainView, domainPortView;

    private View ipLine1, ipLine2, ipLine3, ipLine4, portLine, domainLine, domainPoortLine;

    private TextView titleView;

    private LinearLayout ipLayout, domainLayout;

    private int type;

    private boolean isIpOrDomain = true;

    private RelativeLayout contentLayout;

    private MBIPInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MBWindowUtil.TransparentStatusBar(this);
        super.onCreate(savedInstanceState);
        StatusBarLightMode(this, StatusBarLightMode(this), true);
        setContentView(R.layout.activity_mb_ip_edit);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {

        findViewById(R.id.mb_commit_tv).setOnClickListener(this);
        findViewById(R.id.mb_edit_back_iv).setOnClickListener(this);
        findViewById(R.id.mb_ip_switch_TextView).setOnClickListener(this);
        findViewById(R.id.mb_finish_ImageView).setOnClickListener(this);
        contentLayout = (RelativeLayout) findViewById(R.id.mb_ip_content_Layout);
        titleView = (TextView) findViewById(R.id.mb_title_tv);
        ipView1 = (EditText) findViewById(R.id.mb_ip_et1);
        ipView2 = (EditText) findViewById(R.id.mb_ip_et2);
        ipView3 = (EditText) findViewById(R.id.mb_ip_et3);
        ipView4 = (EditText) findViewById(R.id.mb_ip_et4);
        portView = (EditText) findViewById(R.id.mb_port_et);
        domainView = (EditText) findViewById(R.id.mb_domain_et);
        domainPortView = (EditText) findViewById(R.id.mb_domain_port_et);
        nameView = (EditText) findViewById(R.id.mb_name_EditText);

        ipLine1 = (View) findViewById(R.id.mb_ip_line_1);
        ipLine2 = (View) findViewById(R.id.mb_ip_line_2);
        ipLine3 = (View) findViewById(R.id.mb_ip_line_3);
        ipLine4 = (View) findViewById(R.id.mb_ip_line_4);
        portLine = (View) findViewById(R.id.mb_ip_line_port);
        domainLine = (View) findViewById(R.id.mb_domain_line);
        domainPoortLine = (View) findViewById(R.id.mb_domain_line_port);

        ipLayout = (LinearLayout) findViewById(R.id.mb_ip_layout);
        domainLayout = (LinearLayout) findViewById(R.id.mb_domain_layout);


        ipView1.setOnFocusChangeListener(this);
        ipView2.setOnFocusChangeListener(this);
        ipView3.setOnFocusChangeListener(this);
        ipView4.setOnFocusChangeListener(this);
        portView.setOnFocusChangeListener(this);
        domainView.setOnFocusChangeListener(this);
        domainPortView.setOnFocusChangeListener(this);

        MBEditUtils.setEtFilter(nameView, 10);
        MBEditUtils.setEtEmojiFilter(domainView);
        nameView.requestFocus();
        ipView1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ipView1.getText().length()>=3) {
                    ipView2.requestFocus();
                }
            }
        });
        ipView2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ipView2.getText().length()>=3) {
                    ipView3.requestFocus();
                }
            }
        });
        ipView3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ipView3.getText().length()>=3) {
                    ipView4.requestFocus();
                }
            }
        });
        ipView4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ipView4.getText().length()>=3) {
                    portView.requestFocus();
                }
            }
        });

        type = getIntent().getIntExtra(MBIPContant.TYPE, MBIPContant.OPERATE.INSERT.ordinal());
        if (type == MBIPContant.OPERATE.INSERT.ordinal()) {
//            titleView.setText(getString(R.string.mb_insert));
        } else if (type == MBIPContant.OPERATE.EDIT.ordinal()) {
//            titleView.setText(getString(R.string.mb_edit));
            info = (MBIPInfo) getIntent().getSerializableExtra(MBIPContant.IP);
            if (info != null) {

                String[] ips = info.ip.split("\\.");
                if (ips != null && ips.length == 4) {
                    ipView1.setText(ips[0] + "".replace(".", ""));
                    ipView2.setText(ips[1] + "".replace(".", ""));
                    ipView3.setText(ips[2] + "".replace(".", ""));
                    ipView4.setText(ips[3] + "".replace(".", ""));

                }
                portView.setText(info.port);
            }
        }

        ipView1.setSelection(ipView1.length());
    }




    @Override
    public void onClick(View v) {
        hideKeyboard();
        if (v.getId() == R.id.mb_commit_tv) {
            if (isIpOrDomain) {
                commitIPData();
            }else {
                commitDomainData();
            }
        } else if (v.getId() == R.id.mb_edit_back_iv) {
            Intent intent = new Intent();
            intent.putExtra("type", 0);
            setResult(MBIPContant.RESULT_CODE, intent);
            finish();
        }else if (v.getId() == R.id.mb_finish_ImageView){
            Intent intent = new Intent();
            intent.putExtra("type", 1);
            setResult(MBIPContant.RESULT_CODE, intent);
            finish();
        }else if (v.getId() == R.id.mb_ip_switch_TextView){
            isIpOrDomain = !isIpOrDomain;
            showCutAnim(isIpOrDomain);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.putExtra("type", 0);
            setResult(MBIPContant.RESULT_CODE, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 保存IP数据
     */
    private void commitIPData() {

        String ip1 = ipView1.getText().toString();
        String ip2 = ipView2.getText().toString();
        String ip3 = ipView3.getText().toString();
        String ip4 = ipView4.getText().toString();


        if (TextUtils.isEmpty(ip1) || TextUtils.isEmpty(ip2) || TextUtils.isEmpty(ip3) || TextUtils.isEmpty(ip4)) {
            Toast.makeText(this, R.string.mb_empty_ip, Toast.LENGTH_SHORT).show();
            return;
        }
        String port = portView.getText().toString();

        if (TextUtils.isEmpty(port)) {
            Toast.makeText(this, R.string.mb_empty_port, Toast.LENGTH_SHORT).show();
            return;
        }

        MBIPInfo temp = null;
        if (info == null) {
            info = new MBIPInfo(ip1 + "." + ip2 + "." + ip3 + "." + ip4, port, nameView.getText().toString().trim(), 0);
        } else {
            temp = new MBIPInfo();
            temp.setIp(ip1 + "." + ip2 + "." + ip3 + "." + ip4);
            temp.setPort(port);
            temp.setName(nameView.getText().toString().trim());
            temp.setIsDefeault(info.isDefeault);
        }

        if (type == MBIPContant.OPERATE.INSERT.ordinal()) {
            MBIPUtils.getInstance(this).insertIPPort(info);
        } else if (type == MBIPContant.OPERATE.EDIT.ordinal()) {
            MBIPUtils.getInstance(this).updateIPPort(info, temp);
        }
        setData();
    }

    /**
     * 保存域名数据
     */
    private void commitDomainData() {

        String ip1 = domainView.getText().toString();

        if (TextUtils.isEmpty(ip1)) {
            Toast.makeText(this, R.string.mb_empty_ip, Toast.LENGTH_SHORT).show();
            return;
        }

        MBIPInfo temp = null;
        if (info == null) {
            info = new MBIPInfo(ip1, TextUtils.isEmpty(domainPortView.getText().toString().trim())?"*":domainPortView.getText().toString().trim(), nameView.getText().toString().trim(), 0);
        } else {
            temp = new MBIPInfo();
            temp.setIp(ip1);
            temp.setPort(TextUtils.isEmpty(domainPortView.getText().toString().trim())?"*":domainPortView.getText().toString().trim());
            temp.setName(nameView.getText().toString().trim());
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
        inputMethodManager.hideSoftInputFromWindow(portView.getWindowToken(), 0);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.mb_ip_et1) {
            focusChange(ipView1, ipLine1, hasFocus);
        }else if (v.getId() == R.id.mb_ip_et2){
            focusChange(ipView2, ipLine2, hasFocus);
        }else if (v.getId() == R.id.mb_ip_et3){
            focusChange(ipView3, ipLine3, hasFocus);
        }else if (v.getId() == R.id.mb_ip_et4){
            focusChange(ipView4, ipLine4, hasFocus);
        }else if (v.getId() == R.id.mb_port_et){
            focusChange(portView, portLine, hasFocus);
        }else if (v.getId() == R.id.mb_domain_et){
            focusChange(domainView, domainLine, hasFocus);
        }else if (v.getId() == R.id.mb_domain_port_et){
            focusChange(domainPortView, domainPoortLine, hasFocus);
        }

    }

    private void focusChange(EditText editText, View line, boolean hasFocus){
        if (hasFocus) {
            editText.setTextColor(getResources().getColor(R.color.mb_blue));
            line.setBackgroundColor(getResources().getColor(R.color.mb_blue));
        }else {
            editText.setTextColor(getResources().getColor(R.color.mb_gray));
            line.setBackgroundColor(getResources().getColor(R.color.mb_gray));
        }
    }

    public void showCutAnim(final boolean is2Second) {
        final int time = 600;
        final int distance = 15000;
        final int scaleTime = 80;
        final float scale = 0.9f;

        // 设置照相机的距离（避免旋转时高度超出屏幕高度）
        float CameraScale = getResources().getDisplayMetrics().density * distance;
        contentLayout.setCameraDistance(CameraScale);
        ObjectAnimator animatorR1;
        if (is2Second) {
            animatorR1 = ObjectAnimator.ofFloat(contentLayout, "rotationY", 0, 90).setDuration(time / 2);
        } else {
            animatorR1 = ObjectAnimator.ofFloat(contentLayout, "rotationY", 0, -90).setDuration(time / 2);
        }
        ObjectAnimator animatorR2;
        if (is2Second) {
            animatorR2 = ObjectAnimator.ofFloat(contentLayout, "rotationY", 270, 360).setDuration(time / 2);
        } else {
            animatorR2 = ObjectAnimator.ofFloat(contentLayout, "rotationY", -270, -360).setDuration(time / 2);
        }
        ObjectAnimator animatorSX1 = ObjectAnimator.ofFloat(contentLayout, "scaleX", 1.0f, scale).setDuration(scaleTime);
        ObjectAnimator animatorSY1 = ObjectAnimator.ofFloat(contentLayout, "scaleY", 1.0f, scale).setDuration(scaleTime);
        ObjectAnimator animatorSX2 = ObjectAnimator.ofFloat(contentLayout, "scaleX", scale, 1.0f).setDuration(scaleTime);
        ObjectAnimator animatorSY2 = ObjectAnimator.ofFloat(contentLayout, "scaleY", scale, 1.0f).setDuration(scaleTime);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorSX1, animatorSY1);
        animatorSet.playTogether(animatorSX2, animatorSY2);
        animatorSet.play(animatorR1).after(animatorSX1);
        animatorSet.play(animatorR1).before(animatorR2);
        animatorSet.play(animatorR2).before(animatorSX2);
        animatorSet.start();

        animatorR1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (is2Second) {
                    ipLayout.setVisibility(View.VISIBLE);
                    domainLayout.setVisibility(View.GONE);
                    titleView.setText(R.string.mb_ip_way);
                }else {
                    ipLayout.setVisibility(View.GONE);
                    domainLayout.setVisibility(View.VISIBLE);
                    titleView.setText(R.string.mb_domain_way);
                }
            }
        });
    }
}
