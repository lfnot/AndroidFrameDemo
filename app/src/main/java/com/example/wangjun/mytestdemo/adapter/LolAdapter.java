package com.example.wangjun.mytestdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wangjun.mytestdemo.MyApplication;
import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.entity.VideoBean;

import java.util.List;

/**
 * 当前类注释：主页的adapter
 * Author : LeonWang
 * Created by 2016/7/29.16:50
 * Discription:
 * E-mail :lijiawangjun@gmail.com
 */
public class LolAdapter extends RecyclerView.Adapter<LolAdapter.ViewHolder> implements View.OnClickListener {

    private List<VideoBean.DataEntity> mData = null;
    private Context mContext ;
    public OnRecyclerViewItemClickListener mOnItemClickListener;

    public LolAdapter(List<VideoBean.DataEntity> data, Context context) {
        this.mData = data;
        this.mContext = context;
    }

    public void clearData(){
        mData.clear();
        //notifyDataSetChanged();
        notifyItemRangeChanged(0,mData.size());
    }

    public void addData(List<VideoBean.DataEntity> data){
        addData(0,data);
    }

    public void addData(int position ,List<VideoBean.DataEntity> data){
        if (data != null && data.size() >0 ){
            mData.addAll(data);
            //notifyDataSetChanged();
            notifyItemRangeChanged(position,mData.size());
        }

    }

    public List<VideoBean.DataEntity> getDatas(){
        return mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videos, parent, false);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoBean.DataEntity dataEntity = mData.get(position);
        holder.mTvTitle.setText(dataEntity.getTitle());
        holder.mTvDesc.setText(dataEntity.getDesc());
        holder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        MyApplication.setImage(mContext,holder.mImageView,dataEntity.getPic_url());
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(dataEntity);
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
            mOnItemClickListener.onItemClick(v,(VideoBean.DataEntity)v.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvTitle;
        public TextView mTvDesc;
        public ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            mTvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            mImageView = (ImageView) itemView.findViewById(R.id.ivNews);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, VideoBean.DataEntity listBean);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
