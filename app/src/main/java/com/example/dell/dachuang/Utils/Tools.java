package com.example.dell.dachuang.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by dell on 2019/4/11 0011.
 */

public class Tools {
	private static Tools utils;
	public static synchronized Tools getInstance(){
		if(utils==null){
			utils = new Tools();
		}
		return utils;
	}
	private Tools(){

	}
	public void showNormalDialog(Context context, String msg){
		final AlertDialog.Builder normalDialog =
				new AlertDialog.Builder(context);
		normalDialog.setTitle("Alert");
		normalDialog.setMessage(msg);
		normalDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		// 显示
		normalDialog.show();
	}
}
