package com.duoyou.develop.adapter;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.duoyou.develop.base.BaseFragment;

import java.util.List;

public class CurrencyFragmentAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragmentList;
    private final FragmentManager manager;

    public CurrencyFragmentAdapter(FragmentManager fm, List<BaseFragment> lists) {
        super(fm);
        manager = fm;
        fragmentList = lists;
    }

    public void setData(List<BaseFragment> list) {
        fragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentList.get(position);
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString("id", "" + position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = fragmentList.get(position);
        if (fragment.getArguments() == null) {
            return "";
        }
        return fragment.getArguments().getString("title");
    }
    @Override
    public int getItemPosition(Object object) {
        // 最简单解决 notifyDataSetChanged() 页面不刷新问题的方法
        return POSITION_NONE;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 将实例化的fragment进行显示即可。
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        manager.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);// 注释父类方法
        if (position > fragmentList.size() - 1) {
            return;
        }
        Fragment fragment = fragmentList.get(position);// 获取要销毁的fragment
        manager.beginTransaction().hide(fragment).commitAllowingStateLoss();// 将其隐藏即可，并不需要真正销毁，这样fragment状态就得到了保存
    }

}