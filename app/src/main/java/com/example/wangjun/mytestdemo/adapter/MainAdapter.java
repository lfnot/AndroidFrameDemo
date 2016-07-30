package com.example.wangjun.mytestdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wangjun.mytestdemo.MyApplication;
import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.entity.WXNews;

import java.util.List;
import java.util.Random;

/**
 * 当前类注释：主页的adapter
 * Author : LeonWang
 * Created by 2016/7/29.16:50
 * Discription:
 * E-mail :lijiawangjun@gmail.com
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements View.OnClickListener {

    private List<WXNews.ResultBean.ListBean> mData = null;
    private Context mContext ;
    public OnRecyclerViewItemClickListener mOnItemClickListener;

    public MainAdapter(List<WXNews.ResultBean.ListBean> data,Context context) {
        this.mData = data;
        this.mContext = context;
    }

    public void clearData(){
        mData.clear();
        //notifyDataSetChanged();
        notifyItemRangeChanged(0,mData.size());
    }

    public void addData(List<WXNews.ResultBean.ListBean> data){
        addData(0,data);
    }

    public void addData(int position ,List<WXNews.ResultBean.ListBean> data){
        if (data != null && data.size() >0 ){
            mData.addAll(data);
            //notifyDataSetChanged();
            notifyItemRangeChanged(position,mData.size());
        }

    }

    public List<WXNews.ResultBean.ListBean> getDatas(){
        return mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_news_item, parent, false);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WXNews.ResultBean.ListBean listBean = mData.get(position);
        holder.mTvTab.setText(listBean.getSource());
        try {
            changeColor(holder.mTvTab);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.mTvTitle.setText(listBean.getTitle());
        holder.mTvDesc.setText(listBean.getTitle()+listBean.getUrl()+listBean.getFirstImg()+listBean.getTitle());
        String id = listBean.getId();//wechat_20150402028462
        if (null !=id && id.length()==21){
            String[] idArray = id.split("_", 0);
            String subTime = idArray[1].substring(0, 9);//20150402
            holder.mTvTime.setText("时间轴："+subTime.substring(0,4)+"年"+subTime.substring(4,6)+"月"+subTime.substring(6,subTime.length()-1)+"日");
        }

        holder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        MyApplication.setImage(mContext,holder.mImageView,listBean.getFirstImg());
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(listBean);
        //Glide.with(mContext).load(listBean.getFirstImg()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(WXNews.ResultBean.ListBean)v.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvTab;
        public TextView mTvTitle;
        public TextView mTvDesc;
        public TextView mTvTime;
        public ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvTab = (TextView) itemView.findViewById(R.id.tv_tab);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_item);
        }
    }

    public void changeColor(TextView view) {
        Random x = new Random();
        int red = x.nextInt(256);
        int green = x.nextInt(256);
        int blue = x.nextInt(256);
        if (red == 0 && green == 0 && blue == 0) {//防止背景色为白色
            changeColor(view);
        }else {
            view.setBackgroundColor(Color.rgb(red, green, blue));
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , WXNews.ResultBean.ListBean listBean);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
