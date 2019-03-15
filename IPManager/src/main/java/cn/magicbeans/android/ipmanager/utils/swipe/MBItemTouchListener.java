package cn.magicbeans.android.ipmanager.utils.swipe;

import cn.magicbeans.android.ipmanager.module.MBIPInfo;

/**
 * Created by AItsuki on 2017/7/11.
 * MBItemTouchListener
 */
public interface MBItemTouchListener {
    void onItemClick(MBIPInfo mbipInfo);

    void onLeftMenuClick(MBIPInfo mbipInfo);

    void onRightMenuClick(MBIPInfo mbipInfo);
}
