package com.androidcat.jly.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.androidcat.jly.R;

import java.lang.ref.WeakReference;

/**
 * Created by androidcat on 2018/10/31.
 */

public class TestActivity extends CheckPermissionActivity {

  View clearBtn;
  TextView logTv;
  public static StringBuilder log;
  public static ActivityHandler handler;
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    logTv = findViewById(R.id.logTv);
    clearBtn = findViewById(R.id.clearBtn);

    clearBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        clear();
      }
    });

    log = new StringBuilder();
    handler = new ActivityHandler(TestActivity.this);
  }

  void handleEventMsg(Message msg){
    if (msg.what == 1){
      logTv.setText(log.toString());
    }
  }


  public static class ActivityHandler extends Handler {
    private final WeakReference<TestActivity> mInstance;

    public ActivityHandler(TestActivity instance) {
      mInstance = new WeakReference<TestActivity>(instance);
    }

    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      TestActivity activity = mInstance.get();
      if (null == activity) {
        return;
      }
      activity.handleEventMsg(msg);
    }
  }

  private void clear(){
    log = new StringBuilder();
    logTv.setText("");
  }
}
