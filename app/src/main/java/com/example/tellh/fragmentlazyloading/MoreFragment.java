package com.example.tellh.fragmentlazyloading;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.tellh.fragmentlazyloading.adapter.MyAdapter;
import com.example.tellh.fragmentlazyloading.been.Model;

import java.util.ArrayList;
import java.util.List;

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

    //recycleview
     RecyclerView recyclerView;
    private List<Model> datas;
    private List<Model> datas2;
    private MyAdapter adapter;
    private int TOTAL_COUNTER = 30;   // 总的数据
    private int mCurrentCounter = 15; // 每页显示的条数
    private boolean isErr = true;

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
       initview();
    }

    private void initview() {
        //初始化RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //模拟的数据（实际开发中一般是从网络获取的）
        datas = new ArrayList<>();
        datas2 = new ArrayList<>();
        Model model;
        for (int i = 0; i < 15; i++) {
            model = new Model();
            model.setImgUrl("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2516392654,826153290&fm=26&gp=0.jpg");
            model.setTitle("我是第" + i + "条标题");
            model.setContent("第" + i + "条内容");
            datas.add(model);
        }
        Model model2;
        for (int i = 15; i < 30; i++) {
            model2 = new Model();
            model2.setImgUrl("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2516392654,826153290&fm=26&gp=0.jpg");
            model2.setTitle("我是第" + i + "条标题");
            model2.setContent("第" + i + "条内容");
            datas2.add(model2);
        }

        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //创建适配器
        adapter = new MyAdapter(R.layout.item, datas);

//        adapter.setLoadMoreView(new CustomLoadMoreView());

        // 并未起作用
//        adapter.bindToRecyclerView(recyclerView);
//        adapter.disableLoadMoreIfNotFullPage();

        //给RecyclerView设置适配器
        recyclerView.setAdapter(adapter);



        //上拉加载（设置这个监听就表示有上拉加载功能了）
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentCounter >= TOTAL_COUNTER) {
                            //数据全部加载完毕
                            adapter.loadMoreEnd();
                        } else {
                            if (isErr) {
                                //成功获取更多数据（可以直接往适配器添加数据）
                                // adapter.addData(DataServer.getSampleData(PAGE_SIZE));
                                adapter.addData(datas2);
                                mCurrentCounter = adapter.getData().size();
                                //主动调用加载完成，停止加载
                                adapter.loadMoreComplete();
                            } else {
                                //获取更多数据失败
                                isErr = true;
                                Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_LONG).show();
                                //同理，加载失败也要主动调用加载失败来停止加载（而且该方法会提示加载失败）
                                adapter.loadMoreFail();

                            }
                        }
                    }

                }, 5000);
            }
        }, recyclerView);
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