package com.androidcat.jly.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidcat.jly.application.AppConstants;

/**
 * 顶部标题及Button设置工具
 *
 * @author pxw
 *
 */
public class TitleBarManager {
	private TextView titleView = null;
	private LinearLayout leftLayout = null;
	private LinearLayout rightLayout = null;
	private boolean showLeftLayout = false;
	private Activity activity = null;
	private EditText editText = null;
	private InputMethodManager imm = null;

	/**
	 * 仅设置标题-静态方法
	 *
	 * @param activity
	 *            活动
	 * @param title
	 *            标题文字
	 */
	public static void setTitle(Activity activity, String title) {
		new TitleBarManager(activity).setTitle(title).show();
	}

	/**
	 * 仅设置标题-静态方法
	 *
	 * @param activity
	 *            活动
	 * @param titleRes
	 *            标题文字资源ID
	 */
	public static void setTitle(Activity activity, int titleRes) {
		new TitleBarManager(activity).setTitle(titleRes).show();
	}

	/**
	 * 初始化构建
	 */
	protected TitleBarManager(Activity activity) {
		this.activity = activity;

		this.imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		this.leftLayout = (LinearLayout) activity.findViewById(ResourceUtil.getId("R.id.app_left_view"));
		this.rightLayout = (LinearLayout) activity.findViewById(ResourceUtil.getId("R.id.app_right_view"));
		this.titleView = (TextView) activity.findViewById(ResourceUtil.getId("R.id.app_title_text"));
		this.editText = (EditText) activity.findViewById(ResourceUtil.getId("R.id.editText1"));

		// set gone as default
		this.titleView.setVisibility(View.GONE);
		this.leftLayout.setVisibility(View.GONE);
		this.rightLayout.setVisibility(View.GONE);
	}

	/**
	 * builder
	 *
	 * @param activity
	 * @return
	 */
	public static TitleBarManager create(Activity activity) {
		return new TitleBarManager(activity);
	}

	/**
	 * 显示左边后退按键,自写监听
	 *
	 * @param onClickListener
	 * @return
	 */
	public TitleBarManager setLeftButton(final OnClickListener onClickListener) {
		this.showLeftLayout = true;
		this.leftLayout.setVisibility(View.VISIBLE);
		this.leftLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {// 关闭soft keyboard
					imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				onClickListener.onClick(v);
			}
		});

		return this;
	}

	public TitleBarManager setLeftButton() {
		setLeftButton(new SuperOnClickListener(activity, AppConstants.TRACE_BTN_BACK) {
			@Override
			public void onClick(View v) {
				try {
					activity.finish();
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
		return this;
	}

	/**
	 * 添加自定义右上方布局,并自行处理事件
	 *
	 * @param rightView
	 * @return
	 */
	public TitleBarManager setRightView(View rightView) {
		if (rightView != null) {
			this.rightLayout.addView(rightView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			this.rightLayout.setVisibility(View.VISIBLE);
		}
		return this;
	}

	/**
	 * 添加自定义右上方布局,并自行处理事件
	 * @param rightView
	 * @param layoutParams
	 * @return
	 */
	public TitleBarManager setRightView(View rightView, LayoutParams layoutParams) {
		if (rightView != null) {
			this.rightLayout.addView(rightView, layoutParams);
			this.rightLayout.setVisibility(View.VISIBLE);
		}
		return this;
	}

	/**
	 * 显示标题
	 */
	public TitleBarManager setTitle(CharSequence title) {
		titleView.setVisibility(View.VISIBLE);
		titleView.setText(title);
		return this;
	}

	/**
	 * 显示标题
	 */
	public TitleBarManager setTitle(int titleRes) {
		titleView.setVisibility(View.VISIBLE);
		titleView.setText(titleRes);
		return this;
	}

	/**
	 * 显示
	 */
	public void show() {
		if (showLeftLayout) {
			this.leftLayout.setVisibility(View.VISIBLE);
		}
		this.rightLayout.setVisibility(View.VISIBLE);
		this.titleView.setVisibility(View.VISIBLE);
	}

}
