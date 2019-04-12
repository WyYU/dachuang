package com.example.dell.dachuang.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.dachuang.Activity.ARActivity;
import com.example.dell.dachuang.Java.StepVO;
import com.example.dell.dachuang.Activity.MainActivity;
import com.example.dell.dachuang.R;

/**
 * Created by dell on 2019/4/2 0002.
 */

public class StepFragment extends Fragment{
	private String Tag = "StepFragment";
	private StepVO stepVO;
	private View view;
	Button usevrBtn;
	Button nextStepBtn;
	TextView titleView;
	TextView addressView;
	TextView desView;
	MainActivity mainActivity;
	ViewPager viewPager;
	int Pos;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		mainActivity = (MainActivity) getActivity();
		prepareData();
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.stepfragment,null);
		initView();
		return view;
	}

	private void prepareData() {
		mainActivity = (MainActivity) getActivity();
		viewPager = mainActivity.getViewPager();
		Pos = mainActivity.getTabLayout().getSelectedTabPosition();
		stepVO = mainActivity.getStepVOList(Pos);
		Log.e(Tag, "step id"+stepVO.toString()+" "+" view pos" +viewPager.getCurrentItem());
	}

	private void initView() {
		titleView = view.findViewById(R.id.title_textview);
		addressView = view.findViewById(R.id.address_textview);
		desView = view.findViewById(R.id.des_textview);
		usevrBtn = view.findViewById(R.id.use_vr_btn);
		nextStepBtn = view.findViewById(R.id.next_step_btn);
		titleView.setText("Step" + stepVO.getStepID());
		if (Pos == 7) {
			nextStepBtn.setText("完成");
		}
		addressView.setText(stepVO.getLocation().getLocationName());
		desView.setText(stepVO.getDescription());
		nextStepBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity mainActivity = (MainActivity) getActivity();
				ViewPager viewPager =mainActivity.getViewPager();
				int currPos = viewPager.getCurrentItem();
				Log.e("Pos", String.valueOf(currPos));
				viewPager.setCurrentItem(currPos+1);
			}
		});
		usevrBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ARActivity.class);
				startActivity(intent);

			}
		});

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser){
			Log.e(Tag, "isvisiable to user  view:"+Pos);
		}
	}
}
