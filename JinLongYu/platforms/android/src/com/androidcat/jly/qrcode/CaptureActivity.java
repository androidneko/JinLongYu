package com.androidcat.jly.qrcode;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidcat.jly.R;
import com.androidcat.jly.ui.OnSingleClickListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.core.CameraManager;
import com.google.zxing.core.CaptureActivityHandler;
import com.google.zxing.core.InactivityTimer;
import com.google.zxing.core.ViewfinderView;
import com.androidcat.jly.ui.BaseActivity;
import com.androidcat.jly.utils.ResourceUtil;
import com.androidcat.jly.utils.TitleBarManager;
import com.androidcat.jly.utils.log.LogU;

import java.io.IOException;
import java.util.Vector;

/**
 * @ClassName CaptureActivity
 * @Description 条形码扫描界面
 * @author xuxiang
 * @date 2014-09-23
 */
public class CaptureActivity extends BaseActivity implements Callback {
	private static final long VIBRATE_DURATION = 200L;

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private View lightView;
	private ImageView lightIv;
	private TextView lightTv;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private CameraManager mCameraManager;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private boolean isFlashlightOn = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		BaseActivity.needOpenTimmer = true;
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.activity_capture);
		TitleBarManager.create(this).setLeftButton().setTitle("扫码").show();
		CameraManager.init(getApplication());
		mCameraManager = CameraManager.get();

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		lightView = findViewById(R.id.touchView);
		lightIv = findViewById(R.id.lightIv);
		lightTv = findViewById(R.id.lightTv);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		lightView.setOnClickListener(new OnSingleClickListener() {
			@Override
			public void onSingleClick(View view) {
				if (isFlashlightOn){
					mCameraManager.setTorch(false);
					isFlashlightOn = false;
					lightIv.setBackgroundResource(R.drawable.scan_qrcode_flash_light_off);
					lightTv.setText("打开手电筒");
				}else {
					mCameraManager.setTorch(true);
					isFlashlightOn = true;
					lightIv.setBackgroundResource(R.drawable.scan_qrcode_flash_light_on);
					lightTv.setText("关闭手电筒");
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			CameraManager.get().closeDriver();
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	public void handleDecode(final Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();
		Intent data = new Intent();
		LogU.e("扫描二维码结果", obj.getText());
		data.putExtra("result", obj.getText());
		setResult(RESULT_OK, data);
		//startActivity(data);
		finish();
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onBackPressed() {
		setResult(1);
		super.onBackPressed();
	}

}
