package com.example.tellh.fragmentlazyloading;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.example.tellh.fragmentlazyloading.viewpager.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.viewPager2)
    NoScrollViewPager viewPager;
    private List<Fragment> fragmentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        fragmentList = new ArrayList<>();
        fragmentList.add(MoreFragment.newInstance(1,false));
        fragmentList.add(MoreFragment.newInstance(2,true));
        fragmentList.add(MoreFragment.newInstance(3,true));
        fragmentList.add(MoreFragment.newInstance(4,true));
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(supportFragmentManager, fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);
//        viewPager.setNoScroll(true);//setNoScroll  设置为ture禁止滑动

        viewPager.setOffscreenPageLimit(4);
    }



    //    作用为fragment添加onTouchEvent();
    private ArrayList<FragmentTouchListener> mFragmentTouchListeners = new ArrayList<>();


    public void registerFragmentTouchListener(FragmentTouchListener listener) {
        mFragmentTouchListeners.add(listener);
    }


    public void unRegisterFragmentTouchListener(FragmentTouchListener listener) {
        mFragmentTouchListeners.remove(listener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        for (FragmentTouchListener listener : mFragmentTouchListeners) {
            listener.onTouchEvent(event);
        }

        return super.dispatchTouchEvent(event);
    }

    public interface FragmentTouchListener {

        boolean onTouchEvent(MotionEvent event);
    }

}
