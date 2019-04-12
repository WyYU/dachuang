package com.example.dell.dachuang.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2019/3/20 0020.
 */

public class TabAdapter extends FragmentPagerAdapter {
	private List<Fragment> list;

	public TabAdapter(FragmentManager fm) {
		super(fm);
		list = new ArrayList<>();
	}

	public void setData(List<Fragment> list) {
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) {

		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}
}
