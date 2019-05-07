package com.example.tellh.fragmentlazyloading.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.tellh.fragmentlazyloading.R;
import com.example.tellh.fragmentlazyloading.been.Model;

import java.util.List;

public class MyAdapter extends BaseQuickAdapter<Model, BaseViewHolder> {

    public MyAdapter(@LayoutRes int layoutResId, @Nullable List<Model> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Model item) {
        //可链式调用赋值
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_content, item.getContent());
        // 设置图片
        Glide.with(mContext).load(item.getImgUrl()).into((ImageView) helper.getView(R.id.iv_img));

        //获取当前item的position
        //int position = helper.getLayoutPosition();
    }
}
