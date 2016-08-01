package com.example.wangjun.mytestdemo.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.activity.WebViewActivity;
import com.example.wangjun.mytestdemo.entity.MessageEntity;
import com.example.wangjun.mytestdemo.http.API;
import com.example.wangjun.mytestdemo.utils.SpecialViewUtil;
import com.example.wangjun.mytestdemo.utils.TimeUtil;
import com.github.library.bubbleview.BubbleTextVew;

import java.util.List;

public class ChatMessageAdapter extends BaseListAdapter<MessageEntity> {

    private Context mContext;

    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;

    public ChatMessageAdapter(Context context, List<MessageEntity> list) {
        super(context, list);
        mContext = context;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getType() == TYPE_LEFT) {
            return TYPE_LEFT;
        }
        return TYPE_RIGHT;
    }

    private View createViewByType(int position) {
        if (getItem(position).getType() == TYPE_LEFT) {
            return mInflater.inflate(R.layout.item_conversation_left, null);
        }
        return mInflater.inflate(R.layout.item_conversation_right, null);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createViewByType(position);
        }

        final MessageEntity entity = getItem(position);

        TextView tvTime = ViewHolder.get(convertView, R.id.tv_time);
        BubbleTextVew btvMessage = ViewHolder.get(convertView, R.id.btv_message);

        if (isDisplayTime(position)) {
            tvTime.setVisibility(View.VISIBLE);
            tvTime.setText(TimeUtil.friendlyTime(mContext, entity.getTime()));
        } else {
            tvTime.setVisibility(View.GONE);
        }

        switch (entity.getCode()) {
            case API.TulingCode.URL:
                btvMessage.setText(SpecialViewUtil.getSpannableString(entity.getText(), entity.getUrl()));
                break;
            case API.TulingCode.NEWS:
                btvMessage.setText(SpecialViewUtil.getSpannableString(entity.getText(), "点击查看"));
                break;
            default:
                btvMessage.setText(entity.getText());
                break;
        }

        btvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (entity.getCode()) {

                    case API.TulingCode.URL:
                        Bundle bundle = new Bundle();
                        bundle.putInt("item", 300);
                        bundle.putString("url", entity.getUrl());
                        bundle.putString("imageUrl",entity.getUrl());
                        bundle.putString("title",entity.getText());
                        bundle.putString("content",entity.getText());
                        Intent intent  = new Intent(mContext, WebViewActivity.class);
                        intent .putExtras(bundle);
                        mContext.startActivity(intent);
                        break;
                    case API.TulingCode.NEWS:
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("item", 200);
                        bundle2.putString("url", entity.getList().get(position).getDetailurl());
                        bundle2.putString("imageUrl",entity.getList().get(position).getDetailurl());
                        bundle2.putString("title","新闻");
                        bundle2.putString("content",entity.getText());
                        Intent intent2 = new Intent(mContext, WebViewActivity.class);
                        intent2.putExtras(bundle2);
                        mContext.startActivity(intent2);
                        break;
                }
            }
        });

        btvMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyDeleteDialog(mContext, entity);
                return false;
            }
        });

        return convertView;
    }

    //  一分钟内的请求与回复不显示时间
    public boolean isDisplayTime(int position) {
        if (position > 0) {
            if ((getItem(position).getTime() - getItem(position-1).getTime()) > 60 * 1000) {
                return true;
            } else {
                return false;
            }
        } else if (position == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void copyDeleteDialog(final Context context, final MessageEntity entity) {
        new MaterialDialog.Builder(context)
                .items("复制该文本", "删除这一条")
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                cm.setText(entity.getText());
                                Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                getData().remove(entity);
                                notifyDataSetChanged();
                                break;
                        }
                    }
                })
                .show();
    }

}
