package com.androidcat.jly.qrcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.androidcat.jly.R;
import com.google.zxing.core.QrUtil;
import com.androidcat.jly.ui.BaseActivity;

/**
 * Created by androidcat on 2017/11/22.
 */

public class QrCodeDisplayActivity extends BaseActivity{

    private ImageView qrCodeIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_test);
        qrCodeIv = findViewById(R.id.qrcodeIv);
        String qrcodeStr = getIntent().getStringExtra("result");
        Bitmap bitmap = QrUtil.create2DCode(qrcodeStr);
        if (bitmap != null){
            qrCodeIv.setImageBitmap(bitmap);
        }else {
            showToast("generate qrcode failed！");
        }
    }
}
