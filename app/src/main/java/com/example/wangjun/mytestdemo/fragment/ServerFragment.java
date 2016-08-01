package com.example.wangjun.mytestdemo.fragment;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.adapter.ChatMessageAdapter;
import com.example.wangjun.mytestdemo.entity.MessageEntity;
import com.example.wangjun.mytestdemo.http.API;
import com.example.wangjun.mytestdemo.utils.KeyBoardUtil;
import com.example.wangjun.mytestdemo.utils.MyLog;
import com.example.wangjun.mytestdemo.utils.TimeUtil;
import com.lzy.okhttputils.OkHttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by wangjun on 2016/7/21.
 */
public class ServerFragment extends BaseFragment implements View.OnClickListener{


    public static final String TAG = "MoneyFragment";
    private ListView mLvMsg;
    private EditText mEtMsg;
    private ImageView mIvSendMsg;

    private List<MessageEntity> msgList = new ArrayList<>();
    private ChatMessageAdapter msgAdapter;

    @Override
    public void onFragmentCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_server,savedInstanceState);
        initView();
        initData();
        MyLog.d(TAG,"初始化");
    }

    private void initView() {
        mLvMsg = (ListView) findViewById(R.id.lv_message);
        mEtMsg = (EditText) findViewById(R.id.et_msg);
        mIvSendMsg = (ImageView) findViewById(R.id.iv_send_msg);
    }

    private void initData() {
        if (msgList.size() == 0) {
            MessageEntity entity = new MessageEntity(ChatMessageAdapter.TYPE_LEFT, TimeUtil.getCurrentTimeMillis());
            entity.setText("今日は！俺はロボットですよう！\n你觉得我帅吗？\n选择:\n1，你好帅哟！\n2，你非常帅啦");
            msgList.add(entity);
        }
        msgAdapter = new ChatMessageAdapter(mContext, msgList);
        mLvMsg.setAdapter(msgAdapter);
        mLvMsg.setSelection(msgAdapter.getCount());

        initListener();
    }

    private void initListener() {

        mIvSendMsg.setOnClickListener(this);

        mLvMsg.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                KeyBoardUtil.hideKeyboard(getActivity());
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void getNetData(String info) {
        OkHttpUtils.post(API.TULING_API)
                .params("key","c557cf19d2b30fb7b543cf107460009c")
                .params("info",info)
                .params("dtype", "json")
                .execute(new NetWorkCallBack<Object>(MessageEntity.class));
    }

    @Override
    protected void onHandleMessage(Message msg) {

    }


    @Override
    protected void onAgainLoadNetData() {

    }

    @Override
    protected <T> void onNetSucess(T t) {
        MessageEntity messageEntity = (MessageEntity) t;
        if (messageEntity ==null ) return;
        messageEntity.setTime(TimeUtil.getCurrentTimeMillis());
        messageEntity.setType(ChatMessageAdapter.TYPE_LEFT);

        switch (messageEntity.getCode()) {
            case API.TulingCode.URL:
                messageEntity.setText(messageEntity.getText() + "，点击网址查看：" + messageEntity.getUrl());
                break;
            case API.TulingCode.NEWS:
                messageEntity.setText(messageEntity.getText() + "，点击查看");
                break;
        }

        msgList.add(messageEntity);
        msgAdapter.notifyDataSetChanged();
    }

    @Override
    protected File parseleFileResponse(Response response) {
        return null;
    }

    @Override
    protected void onNetAfter() {

    }

    @Override
    protected void onNetBefore() {

    }

    @Override
    protected void onNetError() {

    }

    @Override
    protected void onNetSucess(File file,int code) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_send_msg:
                String info = mEtMsg.getText().toString().trim();
                if (!TextUtils.isEmpty(info)) {
                    MessageEntity entity = new MessageEntity(ChatMessageAdapter.TYPE_RIGHT, TimeUtil.getCurrentTimeMillis(), info);
                    msgList.add(entity);
                    msgAdapter.notifyDataSetChanged();
                    mEtMsg.setText("");
                    getNetData(info);
                } else {
                    showToast("请输入正确的用户信息，好吧。。。。");
                }
                break;
        }
    }
}
