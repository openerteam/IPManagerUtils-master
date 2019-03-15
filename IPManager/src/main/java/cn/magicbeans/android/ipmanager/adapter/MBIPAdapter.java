package cn.magicbeans.android.ipmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.magicbeans.android.ipmanager.R;
import cn.magicbeans.android.ipmanager.module.MBIPInfo;
import cn.magicbeans.android.ipmanager.utils.MBIPUtils;
import cn.magicbeans.android.ipmanager.utils.MBPingUtils;
import cn.magicbeans.android.ipmanager.utils.swipe.MBItemTouchListener;
import cn.magicbeans.android.ipmanager.utils.swipe.MBSwipeItemLayout;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * date：2019/3/12 0012 on 11:36
 * desc:${}
 * author:BarryL
 */
public class MBIPAdapter extends RecyclerView.Adapter<MBIPAdapter.IPHolder> {

    private Context context;
    private MBItemTouchListener mItemTouchListener;

    private List<MBIPInfo> infos;

    private Subscription subscription;


    public MBIPAdapter(Context context, MBItemTouchListener itemTouchListener) {
        super();
        this.context = context;
        this.mItemTouchListener = itemTouchListener;
        infos = MBIPUtils.getInstance(context).queryData();
    }

    public void refresh() {
        infos.clear();
        infos = MBIPUtils.getInstance(context).queryData();
        notifyDataSetChanged();
    }

    public Subscription getSubscription() {
        return subscription;
    }

    @Override
    public IPHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mb_ip_view, parent, false);
        return new IPHolder(view);
    }

    @Override
    public void onBindViewHolder(final IPHolder holder, int position) {
        final MBIPInfo info = infos.get(position);
        if (info.isDefeault == 1) {
            holder.mSwipeItemLayout.setSwipeEnable(false);
        }else {
            holder.mSwipeItemLayout.setSwipeEnable(true);
        }

        if (info.port.equals("*")) {
            holder.ipView.setText(info.ip);
        }else {
            holder.ipView.setText(info.ip + ":" + info.port);
        }

        if (info.isDefeault == 1) {
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setImageResource(R.mipmap.mb_choose);
        }else {
            holder.status.setVisibility(View.GONE);
        }

        holder.name.setText(info.getName());
        getPinyinList(info, holder);
        if (mItemTouchListener != null) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.mSwipeItemLayout.isOpen()) {
                        holder.mSwipeItemLayout.close();
                    }else {
                        mItemTouchListener.onItemClick(info);
                    }
                }
            });

            if (holder.mRightMenu != null) {
                holder.mRightMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemTouchListener.onRightMenuClick(info);
                        holder.mSwipeItemLayout.close();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return infos == null ? 0 : infos.size();
    }

    class IPHolder extends RecyclerView.ViewHolder{
        private MBSwipeItemLayout mSwipeItemLayout;
        private View mRightMenu;
        private TextView name, ipView;
        private ImageView validImage, status;
        public IPHolder(View itemView) {
            super(itemView);
            mSwipeItemLayout = (MBSwipeItemLayout) itemView.findViewById(R.id.mb_swipe_layout);
            mRightMenu =  itemView.findViewById(R.id.mb_right_menu);
            name = (TextView) itemView.findViewById(R.id.mb_name_TextView);
            ipView = (TextView) itemView.findViewById(R.id.mb_ip_TextView);
            validImage = (ImageView) itemView.findViewById(R.id.mb_valid_ImageView);
            status = (ImageView) itemView.findViewById(R.id.mb_status_ImageView);
        }
    }


    private void getPinyinList(final MBIPInfo info, final IPHolder holder) {
         subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(MBPingUtils.ping1(info.ip));
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean b) {
                        if (b) {
                            holder.validImage.setImageResource(R.mipmap.mb_vaild);
                        }else {
                            holder.validImage.setImageResource(R.mipmap.mb_invalid);
                        }
//                        if (info.isDefeault == 1) {
//                            holder.status.setVisibility(View.VISIBLE);
//                            holder.status.setImageResource(R.mipmap.mb_choose);
//                        }else {
//                            holder.status.setVisibility(View.GONE);
//                        }
                    }
                });
    }


}
