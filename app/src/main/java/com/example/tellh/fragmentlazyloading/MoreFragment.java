package com.example.tellh.fragmentlazyloading;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class MoreFragment extends LazyFragment {
    private TextView tvLoading;
    private ImageView ivContent;
    private int tabIndex;
    public static final String INTENT_INT_INDEX="index";

    //    缩放
    private float tvWidth = -1f;//tvWidth = -1f;
    private float tvHeight = -1f;//private float tvHeight = -1f;

    private TwoFingersGestureDetector twoFingersGestureDetector;

    private float rotateDeg = 0f;
    private float scaleFactor = 1f;
    private float translateX = 0f;
    private float translateY = 0f;

    public static MoreFragment newInstance(int tabIndex,boolean isLazyLoad) {

        Bundle args = new Bundle();
        args.putInt(INTENT_INT_INDEX, tabIndex);
        args.putBoolean(LazyFragment.INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        MoreFragment fragment = new MoreFragment();
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_tabmain_item);
		tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        ivContent = (ImageView) findViewById(R.id.iv_content);
        tvLoading = (TextView) findViewById(R.id.tv_loading);
        getData();
       setScale();
    }



    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //异步处理加载数据
                //...
                //完成后，通知主线程更新UI
                handler.sendEmptyMessageDelayed(1, 2000);
            }
        }).start();
    }

    @Override
    public void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        handler.removeMessages(1);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            tvLoading.setVisibility(View.GONE);
            int id=0;
            switch (tabIndex){
                case 1:
                    id=R.drawable.a;
                    break;
                case 2:
                    id=R.drawable.b;
                    break;
                case 3:
                    id=R.drawable.c;
                    break;
                case 4:
                    id=R.drawable.d;
                    break;
            }
            ivContent.setImageResource(id);
            ivContent.setVisibility(View.VISIBLE);
        }
    };

    private void setScale() {


//        DragTouchListener dragTouchListener = new DragTouchListener();
//        dragTouchListener.setTouchTwoZoomEnable(true);
//        dragTouchListener.setCancleTouchDrag(true);
//        layout_dm.setOnTouchListener(new ZoomListenter());

        twoFingersGestureDetector = new TwoFingersGestureDetector();
        twoFingersGestureDetector.setTwoFingersGestureListener(new TwoFingersGestureDetector.TwoFingersGestureListener() {
            @Override
            public void onDown(float downX, float downY, long downTime) {
                if (tvWidth == -1f) {
                    tvWidth = ivContent.getWidth();
                    tvHeight = ivContent.getHeight();
                }
            }

            //            移动
            @Override
            public void onMoved(float deltaMovedX, float deltaMovedY, long deltaMilliseconds) {
//                layout_dm.setTranslationX(translateX += deltaMovedX);
//                layout_dm.setTranslationY(translateY += deltaMovedY);
            }

            @Override
            public void onRotated(float deltaRotatedDeg, long deltaMilliseconds) {
//                layout_dm.setRotation(rotateDeg += deltaRotatedDeg);
            }

            @Override
            public void onScaled(float deltaScaledX, float deltaScaledY, float deltaScaledDistance, long deltaMilliseconds) {
                scaleFactor += deltaScaledDistance / tvWidth;

                if (scaleFactor<=1.0){
                    scaleFactor= (float) 1f;
                }
                Log.i(TAG, "scaleFactor: "+scaleFactor);
                ivContent.setScaleX(scaleFactor);
                ivContent.setScaleY(scaleFactor);
            }

            @Override
            public void onUp(float upX, float upY, long upTime, float xVelocity, float yVelocity) {}

            @Override
            public void onCancel() {}
        });

        ((MainActivity) this.getActivity()).registerFragmentTouchListener(new MainActivity.FragmentTouchListener() {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                twoFingersGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) this.getActivity()).unRegisterFragmentTouchListener(new MainActivity.FragmentTouchListener(){
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return false;
            }


        });
    }
}